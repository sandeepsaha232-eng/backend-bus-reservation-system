package in.busreservation.busserivce.Repository;

import in.busreservation.busserivce.Entity.Route;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RouteRepository extends JpaRepository<Route,String> {
    public List<Route> findAllByStartplaceAndEndplace(String startplace , String endplace );
}
