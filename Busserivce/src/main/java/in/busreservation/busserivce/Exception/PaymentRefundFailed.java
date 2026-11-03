package in.busreservation.busserivce.Exception;

public class PaymentRefundFailed extends RuntimeException {
    public PaymentRefundFailed(String message) {
        super(message);
    }
}
