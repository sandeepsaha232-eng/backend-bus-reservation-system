package in.busreservation.busserivce.MapStruct;

import in.busreservation.busserivce.DTO.BookingDTO.BookingCreateRequest;
import in.busreservation.busserivce.DTO.BookingDTO.BookingResponse;
import in.busreservation.busserivce.Entity.Booking;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface BookingMapper {
    BookingResponse toResponse(Booking booking);

    Booking toEntity(BookingCreateRequest request);
}
