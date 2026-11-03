package in.busreservation.paymentservice.Repository;

import in.busreservation.paymentservice.Entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment,String> {
    Payment findPaymentByBookingId(String bookingId);
    Optional<Payment> findByStripeCheckoutSessionId(String sessionId);

    Optional<Payment> findByStripePaymentIntentId(String paymentIntentId);

    Optional<Payment> findByStripeRefundId(String refundId);
}
