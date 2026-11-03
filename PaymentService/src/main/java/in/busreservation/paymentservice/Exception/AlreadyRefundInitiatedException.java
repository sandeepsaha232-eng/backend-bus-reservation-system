package in.busreservation.paymentservice.Exception;

public class AlreadyRefundInitiatedException extends RuntimeException {
    public AlreadyRefundInitiatedException(String message) {
        super(message);
    }
}
