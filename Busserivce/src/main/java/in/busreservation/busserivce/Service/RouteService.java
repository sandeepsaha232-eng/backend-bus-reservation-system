package in.busreservation.busserivce.Service;

import in.busreservation.busserivce.DTO.RouteDTO.RouteCreateRequest;
import in.busreservation.busserivce.DTO.RouteDTO.RouteResponse;
import in.busreservation.busserivce.DTO.RouteDTO.RouteUpdateRequest;
import in.busreservation.busserivce.ENUM.RouteStatus;
import in.busreservation.busserivce.Entity.Route;
import in.busreservation.busserivce.Exception.RouteNotFound;
import in.busreservation.busserivce.MapStruct.RouteMapper;
import in.busreservation.busserivce.Repository.RouteRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouteService {
//    Feature	Method	Purpose
//    Create route	createRoute()	Admin creates a route
//    Get route	getRouteById()	Get one route
//    List routes	getAllRoutes()	Paginated list of routes
//    Search routes	searchRoutes()	Find routes by start/end place
//    Update route	updateRoute()	Admin modifies route
//    Delete/deactivate route	deleteRoute()	Remove/deactivate a route
//    Check route	routeExists()	Internal validation
    private final RouteRepository routeRepository;
    private final RouteMapper routeMapper;
    RouteService(RouteMapper routeMapper,RouteRepository routeRepository) {
        this.routeMapper = routeMapper;
        this.routeRepository = routeRepository;
    }
    @Transactional
    public RouteResponse createRoute(RouteCreateRequest request){
        Route route = routeMapper.toEntity(request);
        routeRepository.save(route);
        return routeMapper.toResponse(route);
    }
    public RouteResponse getRouteById(String id ){
       Route route  = routeRepository.findById(id).orElseThrow(()->new RouteNotFound("Route Not Found"));
       return routeMapper.toResponse(route);
    }
    public Page<RouteResponse> getAllRoutes(int size , int page){
        Pageable pageable = PageRequest.of(page,size);
        Page<Route> pageroute =routeRepository.findAll(pageable);
        return pageroute.map(routeMapper::toResponse);
    }
    public List<RouteResponse> searchRoutes(String start , String end ){
        List<Route>  list = routeRepository.findAllByStartplaceAndEndplace(start, end);
        return list.stream().map(routeMapper::toResponse).toList();
    }
    @Transactional
    public RouteResponse updateRoute(String routeId,RouteUpdateRequest request){
        Route oldroute =routeRepository.findById(routeId).orElseThrow(()->new RouteNotFound("Route Not Found"));
        oldroute.setStartplace(request.getStartplace());
        oldroute.setEndplace(request.getEndplace());
        return routeMapper.toResponse(oldroute);
    }
    @Transactional
    public void deleteRoute(String id ){
       Route route = routeRepository.findById(id).orElseThrow(()->new RouteNotFound("Route Not Found"));
       route.setRouteStatus(RouteStatus.DISACTIVATE);
    }
    public boolean routeExists(String id ){
       return  routeRepository.existsById(id);
    }

}
