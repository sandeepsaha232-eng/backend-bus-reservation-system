package in.busreservation.busserivce.DTO.PaymentDTO;

public record PaymentResponse( String paymentId,
                               String stripePaymentIntentId,
                               String clientSecret,
                               String paymentStatus) {
}
