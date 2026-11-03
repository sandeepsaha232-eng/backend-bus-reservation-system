package in.busreservation.busserivce.DTO.TripDTO;

import in.busreservation.busserivce.ENUM.TripStatus;

import java.time.LocalDate;
import java.time.LocalTime;

public class TripResponse {
    private String TripId;
    private LocalTime start_time;
    private  LocalTime end_time;
    private LocalDate dept_Date;
    private  int total_time;
    private TripStatus tripStatus;
    private String adminId;
    public TripStatus getTripStatus() {
        return tripStatus;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public void setTripStatus(TripStatus tripStatus) {
        this.tripStatus = tripStatus;
    }

    public LocalDate getDept_Date() {
        return dept_Date;
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

    public String getTripId() {
        return TripId;
    }

    public void setTripId(String tripId) {
        TripId = tripId;
    }
}
