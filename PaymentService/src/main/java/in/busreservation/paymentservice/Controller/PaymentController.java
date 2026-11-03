package in.busreservation.paymentservice.Controller;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import in.busreservation.paymentservice.DTO.PaymentCreateRequest;
import in.busreservation.paymentservice.DTO.PaymentResponse;
import in.busreservation.paymentservice.Entity.Payment;
import in.busreservation.paymentservice.Service.PaymentService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
//    POST   /api/payments
//    GET    /api/payments/{paymentId}
//    GET    /api/payments/booking/{bookingId}
//    POST   /api/payments/{paymentId}/cancel
//    POST   /api/payments/webhook

    private final PaymentService paymentService;
    @Value("${stripe.webhook.secret}")
    private String endpointSecret;
    PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }
    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment( @RequestBody PaymentCreateRequest request) throws StripeException {
       return ResponseEntity.status(HttpStatus.CREATED).body(paymentService.createPayment(request));
    }
    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponse> getPayment(@PathVariable String paymentId) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(paymentService.getPaymentById(paymentId));
    }
    @GetMapping("/booking/{bookingId}")
    public ResponseEntity<PaymentResponse> getPaymentByBookingId(@PathVariable String bookingId) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(paymentService.getPaymentBybookingId(bookingId));
    }
    @PostMapping("/{paymentId}/cancel")
    public ResponseEntity<PaymentResponse> cancelPayment(@PathVariable String paymentId) throws Exception {
        return ResponseEntity.status(HttpStatus.OK).body(paymentService.cancelRefundPayment(paymentId));
    }
    @PostMapping("/webhook")
    public ResponseEntity<String> handleStripeWebhook(@RequestBody String payload,@RequestHeader("Stripe-Signature")String signature) throws  SignatureVerificationException{
        Event event = Webhook.constructEvent(payload,signature,endpointSecret);
        try {
            paymentService.handleStripeEvent(event);
            return ResponseEntity.ok("WebHook is processed ");
        }
       catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
       }

    }
}
