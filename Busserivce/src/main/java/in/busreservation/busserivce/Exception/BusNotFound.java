package in.busreservation.busserivce.Exception;

public class BusNotFound extends RuntimeException {
    public BusNotFound(String message) {
        super(message);
    }
}
