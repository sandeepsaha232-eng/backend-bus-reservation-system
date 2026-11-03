package in.busreservation.paymentservice.Service;

import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.model.Refund;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.param.RefundCreateParams;
import in.busreservation.paymentservice.DTO.BookingPaymentDetails;
import in.busreservation.paymentservice.DTO.PaymentCreateRequest;
import in.busreservation.paymentservice.DTO.PaymentResponse;
import in.busreservation.paymentservice.ENUM.PaymentStatus;
import in.busreservation.paymentservice.Entity.Payment;
import in.busreservation.paymentservice.Exception.AlreadyRefundInitiatedException;
import in.busreservation.paymentservice.Exception.PaymentNotEligibleException;
import in.busreservation.paymentservice.Exception.PaymentNotFoundException;
import in.busreservation.paymentservice.Mapper.PaymentMapper;
import in.busreservation.paymentservice.Repository.PaymentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.stripe.param.checkout.SessionCreateParams;

import java.math.BigDecimal;
import java.util.Locale;


@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final BusServiceClient busServiceClient;

    public PaymentService(PaymentRepository paymentRepository, PaymentMapper paymentMapper, BusServiceClient busServiceClient) {
        this.paymentRepository = paymentRepository;
        this.paymentMapper = paymentMapper;
        this.busServiceClient = busServiceClient;
    }
    public PaymentResponse createPayment(PaymentCreateRequest request) throws StripeException {
        // Fetch the real amount and currency from Busservice.
        BookingPaymentDetails details =
                busServiceClient.getPaymentPayment(request.getBookingId());
        // Create Stripe Checkout Session using these details.
        Session session = createCheckoutSession(details);
        Payment payment = new Payment();
        payment.setBookingId(details.getBookingId());
        payment.setAmount(details.getAmount());
        payment.setCurrency(details.getCurrency());
        // Save the Session ID so the webhook can find this payment.
        payment.setStripeCheckoutSessionId(session.getId());
        // Use your own initial payment status, not the booking status.
        payment.setPaymentStatus(PaymentStatus.PENDING);
        paymentRepository.save(payment);
        return paymentMapper.toResponse(payment);
    }
    public PaymentResponse getPaymentById(String paymentId) throws Exception {
        Payment payment = paymentRepository.findById(paymentId).orElseThrow(()->new PaymentNotFoundException("Payment not found"));
        return paymentMapper.toResponse(payment);
    }

    public PaymentResponse getPaymentBybookingId(String bookingId) throws  Exception {
       Payment payment = paymentRepository.findPaymentByBookingId(bookingId);
       if(payment==null)
           throw new PaymentNotFoundException("Payment not found");
        return paymentMapper.toResponse(payment);
    }
    @Transactional
    public PaymentResponse cancelRefundPayment(String paymentId) throws Exception{
        Payment payment = paymentRepository.findById(paymentId).orElseThrow(()->new RuntimeException("Payment not found"));
        //call to stripe to initial the refund payment
        if(payment.getPaymentStatus()==PaymentStatus.REFUND_PENDING ||payment.getPaymentStatus() == PaymentStatus.REFUNDED)
            throw new AlreadyRefundInitiatedException("Refund is already under the process");
        if(!(payment.getPaymentStatus() ==PaymentStatus.SUCCESS)){
            throw new PaymentNotEligibleException("Payment status is not SUCCESS,only sucessfull payment can processed");
        }
        payment.setPaymentStatus(PaymentStatus.REFUND_PENDING);
        paymentRepository.save(payment);
        String paymentStripeId = payment.getStripePaymentIntentId();
        RefundCreateParams ref =RefundCreateParams.builder().setPaymentIntent(paymentStripeId).setReason(RefundCreateParams.Reason.REQUESTED_BY_CUSTOMER).build();
        Refund refund = Refund.create(ref);
        payment.setStripeRefundId(refund.getPaymentIntent());
        if(refund.getStatus().equals("succeeded")){
            payment.setPaymentStatus(PaymentStatus.REFUNDED);
        }else if(refund.getStatus().equals("failed")||refund.getStatus().equals("canceled")){
            payment.setPaymentStatus(PaymentStatus.FAILED);
        }
       paymentRepository.save(payment);
        return paymentMapper.toResponse(payment);
    }


    public void handleStripeEvent(Event event) {

        switch (event.getType()) {
            // CASE 1: Customer payment succeeds
            case "checkout.session.completed",
                 "checkout.session.async_payment_succeeded" -> {
                StripeObject object = getStripeEventObject(event);
                if (!(object instanceof Session session)) {
                    throw new IllegalStateException(
                            "Expected a Checkout Session");
                }
                // Checkout completion does not always mean payment succeeded.
                if (!"paid".equals(session.getPaymentStatus())) {
                    return;
                }
                // Find our Payment using the Checkout Session ID.
                Payment payment = paymentRepository
                        .findByStripeCheckoutSessionId(session.getId())
                        .orElseThrow(() ->
                                new RuntimeException("Payment not found"));

                // Avoid processing the same successful payment twice.
                if (payment.getPaymentStatus() == PaymentStatus.SUCCESS) {
                    return;
                }
                // Store the ID of the actual Stripe payment.
                payment.setStripePaymentIntentId(
                        session.getPaymentIntent());

                payment.setPaymentStatus(PaymentStatus.SUCCESS);
                paymentRepository.save(payment);

                // TODO: Notify Busservice to confirm the booking.
            }

            // CASE 2: An asynchronous payment fails
            case "checkout.session.async_payment_failed" -> {
                StripeObject object = getStripeEventObject(event);
                if (!(object instanceof Session session)) {
                    throw new IllegalStateException(
                            "Expected a Checkout Session");
                }
                Payment payment = paymentRepository
                        .findByStripeCheckoutSessionId(session.getId())
                        .orElseThrow(() ->
                                new RuntimeException("Payment not found"));

                // Do not overwrite a successful payment.
                if (payment.getPaymentStatus() != PaymentStatus.SUCCESS) {
                    payment.setPaymentStatus(PaymentStatus.FAILED);
                    paymentRepository.save(payment);
                }
            }

            // CASE 3: A refund is created or its status changes
            case "refund.created",
                 "refund.updated",
                 "refund.failed" -> {
                StripeObject object = getStripeEventObject(event);

                if (!(object instanceof Refund refund)) {
                    throw new IllegalStateException(
                            "Expected a Refund object");
                }
                handleRefundEvent(refund);
            }
            // CASE 4: Other Stripe events are not handled here
            default -> {
                // Ignore events this service does not use.
            }
        }
    }
    // Converts Stripe's event data into its Java object.
    private StripeObject getStripeEventObject(Event event) {
        return event.getDataObjectDeserializer()
                .getObject()
                .orElseThrow(() ->
                        new IllegalStateException(
                                "Could not deserialize Stripe event: "
                                        + event.getType()));
    }
    // Processes updates to a refund.
    private void handleRefundEvent(Refund refund) {
        // First, try to find the payment using the refund ID.
        Payment payment = paymentRepository
                .findByStripeRefundId(refund.getId())
                .orElseGet(() -> {
                    // Fallback: find it using the original PaymentIntent.
                    String paymentIntentId = refund.getPaymentIntent();
                    if (paymentIntentId == null) {
                        throw new IllegalStateException(
                                "Refund has no PaymentIntent ID");
                    }
                    return paymentRepository
                            .findByStripePaymentIntentId(paymentIntentId)
                            .orElseThrow(() ->
                                    new RuntimeException(
                                            "Payment for refund not found"));
                });

        // Save Stripe's refund ID.
        payment.setStripeRefundId(refund.getId());

        // Update our status based on Stripe's actual refund status.
        switch (refund.getStatus()) {
            case "succeeded" ->
                    payment.setPaymentStatus(PaymentStatus.REFUNDED);
            case "failed", "canceled" ->
                    payment.setPaymentStatus(PaymentStatus.REFUND_FAILED);
            case "pending" ->
                    payment.setPaymentStatus(PaymentStatus.REFUND_PENDING);
            default -> {
                // Do not assume an unknown status means success.
            }
        }

        paymentRepository.save(payment);
    }

    // Place this method inside PaymentService
    private Session createCheckoutSession(
            BookingPaymentDetails details) throws StripeException {

        // Convert amount from rupees to paise.
        // Example: ₹500.00 -> 50000 paise.
        BigDecimal amount = new BigDecimal(
                details.getAmount()
        );

        long amountInPaise = amount
                .movePointRight(2)
                .longValueExact();

        // Build the Checkout Session request.
        SessionCreateParams params =
                SessionCreateParams.builder()

                        // This is a one-time payment.
                        .setMode(SessionCreateParams.Mode.PAYMENT)

                        // Redirect URLs after checkout.
                        .setSuccessUrl(
                                "http://localhost:3000/payment/success"
                                        + "?session_id={CHECKOUT_SESSION_ID}"
                        )
                        .setCancelUrl(
                                "http://localhost:3000/payment/cancel"
                        )
                        // Associate the session with your booking.
                        .setClientReferenceId(details.getBookingId())
                        // Describe what the customer is paying for.
                        .addLineItem(
                                SessionCreateParams.LineItem.builder()
                                        .setQuantity(1L)
                                        .setPriceData(
                                                SessionCreateParams.LineItem
                                                        .PriceData.builder()
                                                        .setCurrency(
                                                                details.getCurrency() == null
                                                                        || details.getCurrency().isBlank()
                                                                        ? "inr"
                                                                        : details.getCurrency().toLowerCase(Locale.ROOT)
                                                        )
                                                        .setUnitAmount(amountInPaise)
                                                        .setProductData(
                                                                SessionCreateParams
                                                                        .LineItem
                                                                        .PriceData
                                                                        .ProductData
                                                                        .builder()
                                                                        .setName(
                                                                                "Bus Reservation - Booking "
                                                                                        + details.getBookingId()
                                                                        )
                                                                        .build()
                                                        )
                                                        .build()
                                        )
                                        .build()
                        )

                        .build();

        // Send the request to Stripe and receive the Session.
        return Session.create(params);
    }


}
