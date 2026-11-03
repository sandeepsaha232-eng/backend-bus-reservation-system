package in.busreservation.busserivce.Entity;

import in.busreservation.busserivce.ENUM.BusStatus;
import jakarta.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
public class Bus {

    @Id @GeneratedValue(strategy= GenerationType.UUID)
     private  String busid;
     private  String name ;
     private  int total_seats;
     private String bus_Type;
     private BusStatus status;
    @CreationTimestamp
     private LocalDateTime created_at;
    @UpdateTimestamp
     private LocalDateTime updated_at;

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public LocalDateTime getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(LocalDateTime updated_at) {
        this.updated_at = updated_at;
    }

    public BusStatus getStatus() {
        return status;
    }

    public void setStatus(BusStatus status) {
        this.status = status;
    }

    private String adminEmailId;

    public String getAdminEmailId() {
        return adminEmailId;
    }

    public void setAdminEmailId(String adminEmailId) {
        this.adminEmailId = adminEmailId;
    }

    public String getBus_Type() {
        return bus_Type;
    }

    public void setBus_Type(String bus_Type) {
        this.bus_Type = bus_Type;
    }

    public String getBusid() {
        return busid;
    }

    public void setBusid(String busid) {
        this.busid = busid;
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
