package in.busreservation.busserivce.DTO.PaymentDTO;

public record PaymentCreateRequest(String bookingId,
                                   String userId,
                                   long amount,
                                   String currency) {
}
