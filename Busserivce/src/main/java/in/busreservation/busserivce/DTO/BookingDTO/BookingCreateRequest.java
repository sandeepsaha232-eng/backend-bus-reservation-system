package in.busreservation.busserivce.DTO.BookingDTO;

import in.busreservation.busserivce.Entity.Route;
import in.busreservation.busserivce.Entity.Seat;
import in.busreservation.busserivce.Entity.Trip;
import jakarta.persistence.OneToOne;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

public class BookingCreateRequest {

    private String userId;
    private String seatId;
    private int fare;
    private String routeId;

    private String TripId;
    @CreationTimestamp
    private LocalDateTime createdAt;

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public int getFare() {
        return fare;
    }

    public void setFare(int fare) {
        this.fare = fare;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getRouteId() {
        return routeId;
    }

    public void setRouteId(String routeId) {
        this.routeId = routeId;
    }

    public String getSeatId() {
        return seatId;
    }

    public void setSeatId(String seatId) {
        this.seatId = seatId;
    }

    public String getTripId() {
        return TripId;
    }

    public void setTripId(String tripId) {
        TripId = tripId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


}
