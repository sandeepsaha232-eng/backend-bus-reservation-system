package in.busreservation.paymentservice.DTO;

import in.busreservation.paymentservice.ENUM.PaymentStatus;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class PaymentCreateRequest {

    @NotBlank(message = "Booking ID is required")
    private String bookingId;
    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }
}
