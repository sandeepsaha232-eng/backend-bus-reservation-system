package in.busreservation.busserivce.MapStruct;

import in.busreservation.busserivce.DTO.SeatDTO.SeatCreateRequest;
import in.busreservation.busserivce.DTO.SeatDTO.SeatResponse;
import in.busreservation.busserivce.DTO.SeatDTO.SeatUpdateRequest;
import in.busreservation.busserivce.Entity.Seat;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SeatMapper {
    Seat toEntity(SeatCreateRequest request);
    Seat toEntity(SeatUpdateRequest request);
    SeatResponse toResponse(Seat seat);
    void updateEntity(Seat source, @MappingTarget Seat target);
}
