package in.busreservation.busserivce.DTO.TripDTO;

import java.time.LocalDate;
import java.time.LocalTime;

public class TripDeleteRequest {
    private String TripId;
    private LocalTime start_time;
    private  LocalTime end_time;
    private LocalDate dept_Date;
    private  int total_time;

    public LocalDate getDept_Date() {
        return dept_Date;
    }

    public String getTripId() {
        return TripId;
    }

    public void setTripId(String tripId) {
        TripId = tripId;
    }

    public void setDept_Date(LocalDate dept_Date) {
        this.dept_Date = dept_Date;
    }

    public LocalTime getEnd_time() {
        return end_time;
    }

    public void setEnd_time(LocalTime end_time) {
        this.end_time = end_time;
    }

    public LocalTime getStart_time() {
        return start_time;
    }

    public void setStart_time(LocalTime start_time) {
        this.start_time = start_time;
    }

    public int getTotal_time() {
        return total_time;
    }

    public void setTotal_time(int total_time) {
        this.total_time = total_time;
    }
}
