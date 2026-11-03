package in.busreservation.busserivce.Service;

import in.busreservation.busserivce.DTO.TripDTO.TripCreateRequest;
import in.busreservation.busserivce.DTO.TripDTO.TripResponse;
import in.busreservation.busserivce.DTO.TripDTO.TripSearchRequest;
import in.busreservation.busserivce.DTO.TripDTO.TripUpdateRequest;
import in.busreservation.busserivce.ENUM.TripStatus;
import in.busreservation.busserivce.Entity.Bus;
import in.busreservation.busserivce.Entity.Route;
import in.busreservation.busserivce.Entity.Trip;
import in.busreservation.busserivce.Exception.BusNotFound;
import in.busreservation.busserivce.Exception.RouteNotFound;
import in.busreservation.busserivce.Exception.TripNotFound;
import in.busreservation.busserivce.MapStruct.TripMapper;
import in.busreservation.busserivce.Repository.BusRepository;
import in.busreservation.busserivce.Repository.RouteRepository;
import in.busreservation.busserivce.Repository.TripRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TripService {
//    Feature	Method	Purpose
//    Create trip	createTrip()	Admin schedules a bus on a route
//    Get trip	getTripById()	Get one particular trip
//    List trips	getAvailableTrips()	Paginated list of available/upcoming trips
//    Search trips	searchTrips()	From/to + date + time
//    Filter trips	filterTrips()	Fare, departure time, etc.
//    Update trip	updateTrip()	Admin changes schedule/fare
//    Cancel trip	cancelTrip()	Admin cancels a scheduled trip
//    Admin trips	getAdminTrips()	Admin sees trips belonging to their buses

    private final  TripRepository tripRepository;
    private final TripMapper tripMapper;
    private final BusRepository busRepository;
    private final RouteRepository routeRepository;

    TripService(TripRepository tripRepository, TripMapper tripMapper, BusRepository busRepository, RouteRepository routeRepository) {
        this.tripMapper = tripMapper;
        this.tripRepository = tripRepository;
        this.busRepository = busRepository;
        this.routeRepository = routeRepository;
    }
    public TripResponse createTrip(TripCreateRequest request){
        Trip trip = tripMapper.toEntity(request);
        String busID = request.getBusId();
        String routeId = request.getRouteId();
        Bus bus =busRepository.findById(busID).orElseThrow(()->new BusNotFound("Bus not Found "));
        Route route =routeRepository.findById(routeId).orElseThrow(()->new RouteNotFound("Route not found "));
        trip.setBus(bus);
        trip.setRoute(route);
        tripRepository.save(trip);
        return tripMapper.toResponse(trip);
    }
    public TripResponse getTripById(String id ){
       Trip trip = tripRepository.findById(id).orElseThrow(()->new TripNotFound("Trip Not Found"));
        return tripMapper.toResponse(trip);
    }
    public Page<TripResponse> getAvailableTrips(int page , int size){
        Pageable pageable = PageRequest.of(page,size);
        Page<Trip> pagetrip = tripRepository.findAll(pageable);
        return pagetrip.map(tripMapper::toResponse);
    }
    public List<TripResponse> searchTrips(TripSearchRequest request){
        List<Trip>trip =tripRepository.findByDeptDateAndStartTimeAndEndTimeAndTotalTimeAndRoute_StartplaceAndRoute_Endplace(request.getDate(),request.getStartTime(),request.getEndTime(),
                request.getHour_trip()
                ,request.getStartplace(),request.getEndplace());
        return trip.stream().map(tripMapper::toResponse).toList();
    }
    @Transactional
    public TripResponse updateTrip(String id ,TripUpdateRequest request){
        Trip trip =tripRepository.findById(id).orElseThrow(()->new TripNotFound("Trip Not Found"));
         tripMapper.toUpdateTrip(request,trip);
         return tripMapper.toResponse(trip);
    }
    @Transactional
    public void cancelTrip(String id ){
        Trip trip = tripRepository.findById(id).orElseThrow(()->new TripNotFound("Trip Not Found"));
        trip.setTripStatus(TripStatus.CANCELLED);
        tripRepository.save(trip);
    }
    public Page<TripResponse> getAdminTrips(int page , int size, String adminId ){
        Pageable pageable = PageRequest.of(page,size);
        Page<Trip> pagetrip  = tripRepository.findByadminId(pageable,adminId);
        return pagetrip.map(tripMapper::toResponse);
    }
}
