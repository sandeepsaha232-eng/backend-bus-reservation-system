package in.busreservation.busserivce.Exception;

public class SeatNotFound extends RuntimeException {
    public SeatNotFound(String message) {
        super(message);
    }
}
