package in.busreservation.busserivce.DTO.BookingDTO;

import in.busreservation.busserivce.Entity.Route;
import in.busreservation.busserivce.Entity.Seat;
import in.busreservation.busserivce.Entity.Trip;

public class BookingResponse {
    String book_id;
    private int amount ;
    private String userId;
    private String seatId;

    private String routeId;

    private String TripId;

    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
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
