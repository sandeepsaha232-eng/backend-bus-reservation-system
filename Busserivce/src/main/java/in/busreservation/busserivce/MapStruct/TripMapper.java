package in.busreservation.busserivce.MapStruct;

import in.busreservation.busserivce.DTO.TripDTO.TripCreateRequest;
import in.busreservation.busserivce.DTO.TripDTO.TripResponse;
import in.busreservation.busserivce.DTO.TripDTO.TripUpdateRequest;
import in.busreservation.busserivce.Entity.Trip;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface TripMapper {
     Trip toEntity(TripCreateRequest request);
     TripResponse toResponse(Trip trip);
     void toUpdateTrip(TripUpdateRequest request, @MappingTarget Trip trip);
}
