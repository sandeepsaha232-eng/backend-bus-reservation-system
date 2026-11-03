package in.busreservation.busserivce.Exception;

import in.busreservation.busserivce.Entity.Route;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
       return  ResponseEntity.badRequest().body(e.getMessage());
    }
    @ExceptionHandler(AdminNotFound.class)
    public ResponseEntity<String> handleAdminNotFound(AdminNotFound e){
        return  ResponseEntity.badRequest().body(e.getMessage());
    }
    @ExceptionHandler(BookingNotFound.class)
    public ResponseEntity<String> handleBookingNotFound(BookingNotFound e){
        return  ResponseEntity.badRequest().body(e.getMessage());
    }
    @ExceptionHandler(BusNotFound.class)
    public ResponseEntity<String> handleBusNotFound(BusNotFound e){
        return  ResponseEntity.badRequest().body(e.getMessage());
    }
    @ExceptionHandler(RouteNotFound.class)
    public ResponseEntity<String> handleRouteNotFound(RouteNotFound e){
        return  ResponseEntity.badRequest().body(e.getMessage());
    }
    @ExceptionHandler(SeatNotFound.class)
    public ResponseEntity<String> handleSeatNotFound(SeatNotFound e){
        return  ResponseEntity.badRequest().body(e.getMessage());
    }
    @ExceptionHandler(TripNotFound.class)
    public  ResponseEntity<String> handleTripNotFound(TripNotFound e){
        return  ResponseEntity.badRequest().body(e.getMessage());
    }
}

