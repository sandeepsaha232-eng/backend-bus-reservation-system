package in.busreservation.busserivce.Controller;

import in.busreservation.busserivce.DTO.BusDTO.BusCreateRequest;
import in.busreservation.busserivce.DTO.BusDTO.BusResponse;
import in.busreservation.busserivce.DTO.BusDTO.BusUpdateRequest;
import in.busreservation.busserivce.ENUM.BusStatus;
import in.busreservation.busserivce.Service.BusSerivce;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/buses")
public class BusController {
//    Admin APIs
//    POST   /api/buses
//    GET    /api/buses/{busId}
//    GET    /api/buses
//    PUT    /api/buses/{busId}
//    DELETE /api/buses/{busId}
    private final BusSerivce busSerivce;
    BusController(BusSerivce busSerivce) {
        this.busSerivce = busSerivce;
    }
    @PostMapping
    public ResponseEntity<BusResponse> createBus(@RequestBody  BusCreateRequest request){
       return  ResponseEntity.status(HttpStatus.CREATED).body(busSerivce.createBus(request));
    }
    @GetMapping("/{bus_Id}")
    public ResponseEntity<BusResponse> getBus(@PathVariable("bus_Id") String bus_Id){
        return ResponseEntity.ok(busSerivce.getBusById(bus_Id));
    }
    @GetMapping
    public ResponseEntity<Page<BusResponse>> getAllAdminBus(@RequestParam int page,@RequestParam int size) {
        return ResponseEntity.ok(busSerivce.getAllBuses(page, size));
    }
    @PutMapping("/{busId}")
    public ResponseEntity<BusResponse> updateBus(@PathVariable String busId,@RequestBody BusUpdateRequest request){
       return ResponseEntity.ok(busSerivce.updateBus(busId,request));
    }
    @DeleteMapping("/{bus_id}")
    public  ResponseEntity<Void> deleteBus(@PathVariable("bus_id") String bus_id){
        if(busSerivce.deactivateBus(bus_id)== BusStatus.DEACTIVATE)
        return ResponseEntity.ok().build();
        return ResponseEntity.badRequest().build();
    }
}
