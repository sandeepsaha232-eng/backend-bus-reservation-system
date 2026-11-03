package in.busreservation.busserivce.Exception;

public class PaymentFailed extends RuntimeException {
    public PaymentFailed(String message) {
        super(message);
    }
}
