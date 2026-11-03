package in.busreservation.busserivce.Controller;

import in.busreservation.busserivce.DTO.BookingDTO.BookingCreateRequest;
import in.busreservation.busserivce.DTO.BookingDTO.BookingResponse;
import in.busreservation.busserivce.ENUM.BookingStatus;
import in.busreservation.busserivce.Service.BookingService;
import in.busreservation.busserivce.Service.BusSerivce;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookings")
public class BookingController {
//    Booking
//    POST /api/bookings
//    GET  /api/bookings/{id}
//    GET  /api/bookings/user/{userId}
//    POST /api/bookings/{id}/cancel
    private  final BookingService bookingService;
    private final BusSerivce busSerivce;

    BookingController(BookingService bookingService, BusSerivce busSerivce) {
        this.bookingService = bookingService;
        this.busSerivce = busSerivce;
    }
    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(@RequestBody BookingCreateRequest request) throws Exception {
       BookingResponse bookingResponse= bookingService.createBooking(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingResponse);
    }
    @GetMapping("/{id}")
    public ResponseEntity<BookingResponse> getBooking(@PathVariable String id){
       return ResponseEntity.status(HttpStatus.OK).body(bookingService.getBookingById(id));
    }
    @GetMapping("/user/{user_id}")
    public ResponseEntity<Page<BookingResponse>> getBookingByUserId(@RequestParam(defaultValue = "0") int page
            , @RequestParam(defaultValue = "10") int size ,@PathVariable String user_id){
        Page<BookingResponse> bookingResponsePage= bookingService.getUserBookings(page,size,user_id);
        return ResponseEntity.status(HttpStatus.OK).body(bookingResponsePage);
    }

    @PostMapping("/{id}/cancel")
    public ResponseEntity<Void>  cancelBooking(@PathVariable String id){
        if(bookingService.cancelBooking(id)==BookingStatus.CANCELLED){
            return ResponseEntity.ok().build();
        }
       return ResponseEntity.badRequest().build();
    }
    @GetMapping("/{bookingId}/payment-details")
    public ResponseEntity<BookingResponse> getPaymentDetails(@PathVariable String bookingId){
       return  ResponseEntity.ok(bookingService.getBookingById(bookingId));
    }
}
