package in.busreservation.busserivce.Repository;

import in.busreservation.busserivce.DTO.BusDTO.BusUpdateRequest;
import in.busreservation.busserivce.Entity.Bus;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusRepository  extends JpaRepository<Bus,String> {
    public  Page<Bus> findAllByadminEmailId(Pageable pageable, String adminEmailId);
}