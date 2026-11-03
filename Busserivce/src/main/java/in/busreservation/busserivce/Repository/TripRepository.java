package in.busreservation.busserivce.Repository;

import in.busreservation.busserivce.Entity.Trip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface TripRepository extends JpaRepository<Trip,String> {
    List<Trip> findByDeptDateAndStartTimeAndEndTimeAndTotalTimeAndRoute_StartplaceAndRoute_Endplace(
            LocalDate deptDate,
            LocalTime startTime,
            LocalTime endTime,
            int totalTime,
            String startplace,
            String endplace

    );
    Page<Trip> findByadminId(Pageable pageable, String adminId);
}
