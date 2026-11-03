package in.busreservation.busserivce.DTO.SeatDTO;



public class SeatCreateRequest {
    private  String seat_type;
    private int fare;

    public int getFare() {
        return fare;
    }

    public void setFare(int fare) {
        this.fare = fare;
    }

    public String getSeat_type() {
        return seat_type;
    }

    public void setSeat_type(String seat_type) {
        this.seat_type = seat_type;
    }
}
