package in.busreservation.busserivce.MapStruct;

import in.busreservation.busserivce.DTO.RouteDTO.RouteCreateRequest;
import in.busreservation.busserivce.DTO.RouteDTO.RouteResponse;
import in.busreservation.busserivce.DTO.RouteDTO.RouteUpdateRequest;
import in.busreservation.busserivce.Entity.Route;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RouteMapper {
     Route toEntity(RouteCreateRequest request);
    Route toEntity(RouteUpdateRequest request);
     RouteResponse toResponse(Route route);

}
