package in.busreservation.paymentservice.DTO;


import in.busreservation.paymentservice.ENUM.PaymentStatus;

public class BookingPaymentDetails {
    private String bookingId;
    private int amount;
    private String currency;
    private PaymentStatus status;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getBookingId() {
        return bookingId;
    }

    public void setBookingId(String bookingId) {
        this.bookingId = bookingId;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public void setStatus(PaymentStatus status) {
        this.status = status;
    }
}
