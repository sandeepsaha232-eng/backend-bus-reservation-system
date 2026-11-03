package in.busreservation.busserivce.Controller;


import in.busreservation.busserivce.DTO.RouteDTO.RouteCreateRequest;
import in.busreservation.busserivce.DTO.RouteDTO.RouteResponse;
import in.busreservation.busserivce.DTO.RouteDTO.RouteUpdateRequest;
import in.busreservation.busserivce.Service.RouteService;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/routes")
public class RouteController {
//    POST /api/routes
//    GET  /api/routes/{routeId}
//    GET  /api/routes
//    PUT  /api/routes/{routeId}
//    DELETE /api/routes/{routeId}
    private  final  RouteService serivce;
    RouteController (RouteService serivce) {
        this.serivce = serivce;
    }
    @PostMapping
    public ResponseEntity<RouteResponse> createRoute(@RequestBody RouteCreateRequest request) {
      return  ResponseEntity.status(HttpStatus.CREATED).body(serivce.createRoute(request));
    }
    @GetMapping("/{routeId}")
    public ResponseEntity<RouteResponse> getRoute(@PathVariable("routeId") String routeId) {
        return ResponseEntity.ok(serivce.getRouteById(routeId));
    }
    @GetMapping()
    public ResponseEntity<Page<RouteResponse>> getAllRoute(@RequestParam int size, @RequestParam int page ){
        return ResponseEntity.status(HttpStatus.OK).body(serivce.getAllRoutes(size, page));
    }
    @PutMapping("/{routeId}")
    public ResponseEntity<RouteResponse> updateRoute( @PathVariable String routeId,@RequestBody RouteUpdateRequest request) {
       return ResponseEntity.ok(serivce.updateRoute(routeId,request));
    }
    @DeleteMapping("/{routeId}")
    public ResponseEntity<Void> deleteRoute(@PathVariable("routeId") String routeId) {
        serivce.deleteRoute(routeId);
        return ResponseEntity.ok().build();
    }
}
