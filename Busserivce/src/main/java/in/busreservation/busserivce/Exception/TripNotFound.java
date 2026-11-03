package in.busreservation.busserivce.Exception;

public class TripNotFound extends  RuntimeException {
    public TripNotFound(String message) {
        super(message);
    }
}
