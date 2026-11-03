package in.busreservation.busserivce.Exception;

public class RouteNotFound extends RuntimeException {
    public RouteNotFound(String message) {
        super(message);
    }
}
