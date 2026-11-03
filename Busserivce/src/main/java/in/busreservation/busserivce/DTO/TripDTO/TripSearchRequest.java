package in.busreservation.busserivce.DTO.TripDTO;

import java.time.LocalDate;
import java.time.LocalTime;

public class TripSearchRequest {
    LocalDate date;
    LocalTime startTime;
    LocalTime endTime;
    String startplace;
    String endplace;
    int hour_trip;
    private String adminId;

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public int getHour_trip() {
        return hour_trip;
    }

    public void setHour_trip(int hour_trip) {
        this.hour_trip = hour_trip;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getEndplace() {
        return endplace;
    }

    public void setEndplace(String endplace) {
        this.endplace = endplace;
    }

    public LocalTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime = endTime;
    }

    public String getStartplace() {
        return startplace;
    }

    public void setStartplace(String startplace) {
        this.startplace = startplace;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }
}
