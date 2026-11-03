package in.busreservation.busserivce.Repository;

import in.busreservation.busserivce.DTO.SeatDTO.SeatResponse;
import in.busreservation.busserivce.Entity.Seat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat,String> {
    List<Seat> findSeatByBusBusid(String busid);
    Page<Seat> findSeatByBusBusid(Pageable pageable, String busid);
}
