package in.busreservation.paymentservice.Exception;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.StripeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log =
            LoggerFactory.getLogger(GlobalExceptionHandler.class);
    @ExceptionHandler(PaymentNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handlePaymentNotFound(
            PaymentNotFoundException ex) {

        return buildResponse(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(AlreadyRefundInitiatedException.class)
    public ResponseEntity<Map<String, Object>> handleRefundAlreadyInitiated(
            AlreadyRefundInitiatedException ex) {

        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(PaymentNotEligibleException.class)
    public ResponseEntity<Map<String, Object>> handlePaymentNotEligible(
            PaymentNotEligibleException ex) {

        return buildResponse(HttpStatus.CONFLICT, ex.getMessage());
    }

    @ExceptionHandler(SignatureVerificationException.class)
    public ResponseEntity<Map<String, Object>> handleInvalidStripeSignature(
            SignatureVerificationException ex) {

        return buildResponse(
                HttpStatus.BAD_REQUEST,
                "Invalid Stripe webhook signature"
        );
    }

    @ExceptionHandler(StripeException.class)
    public ResponseEntity<Map<String, Object>> handleStripeException(
            StripeException ex) {

        return buildResponse(
                HttpStatus.BAD_GATEWAY,
                "Stripe payment operation failed"
        );
    }

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<Map<String, Object>> handleGenericException(
//            Exception ex) {
//
//        return buildResponse(
//                HttpStatus.INTERNAL_SERVER_ERROR,
//                "An unexpected error occurred"
//        );
//    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(
            Exception ex) {

        log.error("Unexpected error in Payment Service", ex);

        return buildResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "An unexpected error occurred"
        );
    }

    private ResponseEntity<Map<String, Object>> buildResponse(
            HttpStatus status,
            String message) {

        Map<String, Object> body = Map.of(
                "timestamp", Instant.now().toString(),
                "status", status.value(),
                "error", status.getReasonPhrase(),
                "message", message
        );

        return ResponseEntity.status(status).body(body);
    }

}
