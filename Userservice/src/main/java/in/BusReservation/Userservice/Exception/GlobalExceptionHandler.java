package in.BusReservation.Userservice.Exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler   {

    @ExceptionHandler(NameIncorrectException.class)
    public ResponseEntity<String> handleNameIncorrectException(NameIncorrectException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> handleUserAlreadyExistsException(UserAlreadyExistsException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
    @ExceptionHandler(PasswordIncorrectException.class)
    public ResponseEntity<String> handlePasswordIncorrectException(PasswordIncorrectException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
    @ExceptionHandler(PhoneNoIncorrectException.class)
    public ResponseEntity<String> handlePhoneNoIncorrectException(PhoneNoIncorrectException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
