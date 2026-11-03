package in.busreservation.busserivce.Controller;

import in.busreservation.busserivce.DTO.TripDTO.TripCreateRequest;
import in.busreservation.busserivce.DTO.TripDTO.TripResponse;
import in.busreservation.busserivce.DTO.TripDTO.TripUpdateRequest;
import in.busreservation.busserivce.Service.TripService;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/trips")
public class TripController{
//    POST   /api/trips
//    GET    /api/trips/{tripId}
//    GET    /api/trips
//    PUT    /api/trips/{tripId}
//    DELETE /api/trips/{tripId}
    private final TripService tripService;
    TripController(TripService tripService){
        this.tripService = tripService;
    }
    @PostMapping
    public ResponseEntity<TripResponse> createTrip(@RequestBody TripCreateRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(tripService.createTrip(request));
    }
    @GetMapping("/{tripId}")
    public ResponseEntity<TripResponse> GetTripById(@PathVariable  (name = "tripId") String id){
        return ResponseEntity.status(HttpStatus.OK).body(tripService.getTripById(id));
    }

    @GetMapping
    public ResponseEntity<Page<TripResponse>>  getAllTrip(@RequestParam int page,int size) {
       return ResponseEntity.status(HttpStatus.FOUND).body(tripService.getAvailableTrips(page, size));
    }
    @PutMapping("/{tripId}")
    public ResponseEntity<TripResponse> updateTrip(@PathVariable(name = "tripId") String tripId,@RequestBody TripUpdateRequest request){
        return ResponseEntity.ok(tripService.updateTrip(tripId,request));
    }
    @DeleteMapping("/{tripId}")
    public ResponseEntity<Void> deleteTrip(@PathVariable(name = "tripId") String tripId){
        tripService.cancelTrip(tripId);
        return ResponseEntity.ok().build();
    }
}

