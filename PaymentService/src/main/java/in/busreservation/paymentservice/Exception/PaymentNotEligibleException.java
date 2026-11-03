package in.busreservation.paymentservice.Exception;

public class PaymentNotEligibleException extends RuntimeException {
    public PaymentNotEligibleException(String message) {
        super(message);
    }
}
