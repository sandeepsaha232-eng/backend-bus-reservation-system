package in.busreservation.busserivce.MapStruct;
import in.busreservation.busserivce.DTO.BusDTO.BusCreateRequest;
import in.busreservation.busserivce.DTO.BusDTO.BusUpdateRequest;
import in.busreservation.busserivce.DTO.BusDTO.BusValidateRequest;
import org.mapstruct.Mapper;
import in.busreservation.busserivce.DTO.BusDTO.BusResponse;
import in.busreservation.busserivce.Entity.Bus;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BusMapper {
    BusResponse toResponse(Bus bus);
    Bus toEntity(BusCreateRequest busCreateRequest);
    Bus toEntity(BusUpdateRequest busUpdateRequest);
    Bus toEntity(BusValidateRequest busValidateRequest);
    @Mapping(target = "busid", ignore = true)
    void updateBusFromRequest(
            BusUpdateRequest request,
            @MappingTarget Bus bus
    );
}
