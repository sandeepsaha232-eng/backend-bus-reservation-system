package in.busreservation.busserivce.DTO.BookingDTO;

import in.busreservation.busserivce.Entity.Route;
import in.busreservation.busserivce.Entity.Seat;
import in.busreservation.busserivce.Entity.Trip;

public class BookingValidateRequest {
    private String userId;
    private String seatId;
    private int fare;
    private String routeId;

    private String TripId;

    public int getFare() {
        return fare;
    }

    public void setFare(int fare) {
        this.fare = fare;
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
