package in.busreservation.busserivce.DTO.RouteDTO;

import in.busreservation.busserivce.ENUM.RouteStatus;

public class RouteUpdateRequest {
    private  String startplace;
    private  String endplace;

    private String adminEmailId;
    private String routeStatus;

    public String getRouteStatus() {
        return routeStatus;
    }

    public void setRouteStatus(String routeStatus) {
        this.routeStatus = routeStatus;
    }


    public String getAdminEmailId() {
        return adminEmailId;
    }

    public void setAdminEmailId(String adminEmailId) {
        this.adminEmailId = adminEmailId;
    }
    public String getEndplace() {
        return endplace;
    }

    public void setEndplace(String endplace) {
        this.endplace = endplace;
    }


    public String getStartplace() {
        return startplace;
    }

    public void setStartplace(String startplace) {
        this.startplace = startplace;
    }
}
