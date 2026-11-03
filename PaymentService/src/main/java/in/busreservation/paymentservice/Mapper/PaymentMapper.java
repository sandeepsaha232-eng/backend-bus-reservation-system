package in.busreservation.paymentservice.Mapper;

import in.busreservation.paymentservice.DTO.PaymentResponse;
import in.busreservation.paymentservice.Entity.Payment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    PaymentResponse toResponse(Payment payment);
}
