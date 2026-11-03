package in.busreservation.busserivce.DTO.BusDTO;

import in.busreservation.busserivce.ENUM.BusStatus;

public class BusResponse {
    private  String busid;
    private  String name ;
    private  int total_seats;
    private String bus_Type;
    private String adminEmailId;

    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getAdminEmailId() {
        return adminEmailId;
    }

    public void setAdminEmailId(String adminEmailId) {
        this.adminEmailId = adminEmailId;
    }
    public String getBus_Type() {
        return bus_Type;
    }

    public String getBusid() {
        return busid;
    }

    public void setBusid(String busid) {
        this.busid = busid;
    }

    public void setBus_Type(String bus_Type) {
        this.bus_Type = bus_Type;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotal_seats() {
        return total_seats;
    }

    public void setTotal_seats(int total_seats) {
        this.total_seats = total_seats;
    }
}
