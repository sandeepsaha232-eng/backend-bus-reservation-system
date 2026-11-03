package in.busreservation.busserivce.Controller;

import in.busreservation.busserivce.DTO.SeatDTO.SeatCreateRequest;
import in.busreservation.busserivce.DTO.SeatDTO.SeatResponse;
import in.busreservation.busserivce.DTO.SeatDTO.SeatUpdateRequest;
import in.busreservation.busserivce.Service.SeatService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.GetExchange;

@RestController
@RequestMapping("/api")
public class SeatController {
//    Admin seat configuration
//    POST   /api/buses/{busId}/seats
//    GET    /api/buses/{busId}/seats
//    PUT    /api/seats/{seatId}
//    DELETE /api/seats/{seatId}
    private final SeatService seatService;
    SeatController(SeatService seatService) {
        this.seatService = seatService;
    }
    @PostMapping("/buses/{busId}/seats")
    public ResponseEntity<SeatResponse> createSeat(@PathVariable String busId, @RequestBody SeatCreateRequest request) {
       SeatResponse seatResponse = seatService.createSeats(request,busId);
       return ResponseEntity.status(HttpStatus.CREATED).body(seatResponse);
    }
    @GetMapping("/buses/{busId}/seats")
    public ResponseEntity<Page<SeatResponse>> getSeats(@PathVariable String busId, @RequestParam  int page , @RequestParam int size) {
        return ResponseEntity.status(HttpStatus.OK).body(seatService.getTripSeats(page,size,busId));
    }
    @PutMapping("/seats/{seatId}")
    public  ResponseEntity<SeatResponse> updateSeat(@PathVariable String seatId, @RequestBody SeatUpdateRequest request) {
        return ResponseEntity.ok(seatService.updateSeat(request,seatId));
    }
    @DeleteMapping("/seats/{seatId}")
    public void deleteSeat(@PathVariable String seatId) {
        seatService.blockSeat(seatId);
    }
}
