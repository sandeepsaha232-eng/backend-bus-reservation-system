package in.busreservation.busserivce.Repository;

import in.busreservation.busserivce.Entity.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking,String> {
    Page<Booking> findAllByUserId(Pageable pageble, String userId);
}
