package in.busreservation.busserivce.Service;

import in.busreservation.busserivce.DTO.BookingDTO.BookingCreateRequest;
import in.busreservation.busserivce.DTO.BookingDTO.BookingResponse;
import in.busreservation.busserivce.DTO.PaymentDTO.PaymentCreateRequest;
import in.busreservation.busserivce.DTO.PaymentDTO.PaymentResponse;
import in.busreservation.busserivce.ENUM.BookingStatus;
import in.busreservation.busserivce.ENUM.SeatStatus;
import in.busreservation.busserivce.Entity.Booking;
import in.busreservation.busserivce.Entity.Seat;
import in.busreservation.busserivce.Exception.BookingNotFound;
import in.busreservation.busserivce.Exception.PaymentFailed;
import in.busreservation.busserivce.Exception.PaymentRefundFailed;
import in.busreservation.busserivce.Exception.SeatNotFound;
import in.busreservation.busserivce.MapStruct.BookingMapper;
import in.busreservation.busserivce.Repository.BookingRepository;
import in.busreservation.busserivce.Repository.SeatRepository;
import in.busreservation.busserivce.ServiceClient.PaymentClient;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;


@Service
public class BookingService {
//    Feature	Method	Purpose
//    Create booking	createBooking()	Reserve selected seat
//    Get booking	getBookingById()	View one booking
//    Get user bookings	getUserBookings()	Passenger's booking history
//    Cancel booking	cancelBooking()	Cancel a reservation
//    Get booking status	getBookingStatus()	Check current state
//    Confirm booking	confirmBooking()	Confirm after successful payment
//    Check booking	bookingExists()	Internal validation

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final SeatRepository seatRepository;
    private final RestClient restClient;

    BookingService(BookingRepository bookingRepository, BookingMapper bookingMapper, SeatRepository seatRepository, RestClient restClient) {
        this.bookingRepository = bookingRepository;
        this.bookingMapper = bookingMapper;
        this.seatRepository = seatRepository;
        this.restClient = restClient;
    }
    @Transactional
    public BookingResponse createBooking(BookingCreateRequest request) throws  Exception {
        //  check for the availability of the seat
          Booking booking = bookingMapper.toEntity(request);
          SeatStatus status = seatRepository.findById(request.getSeatId()).orElseThrow(()->new SeatNotFound("SeatNotFound")).getSeatStatus();
        boolean b = status == SeatStatus.AVAILABLE;
        if (b) {
            PaymentCreateRequest paymentRequest =
                    new PaymentCreateRequest(
                            booking.getBook_id(),
                            booking.getUserId(),
                            booking.getFare(),
                            "INR"
                    );
            PaymentResponse paymentResponse = restClient
                                                        .post()
                                                                .uri("/api/payments")
                                                                        .body(paymentRequest).retrieve().body(PaymentResponse.class);
            //call the payment method
            if(paymentResponse.paymentStatus().equals("SUCCESS")){
                booking.setStatus(BookingStatus.CONFIRMED);
                bookingRepository.save(booking);
            }else{
                booking.setStatus(BookingStatus.REJECTED);
                throw new PaymentFailed("Payment failed please try again ");
            }
          }else {
            throw  new SeatNotFound("Seat is already occupied");
        }
        return  bookingMapper.toResponse(booking);
    }
    public BookingResponse getBookingById(String Bookid){
        Booking booking =bookingRepository.findById(Bookid).orElseThrow(()-> new BookingNotFound("Booking Not Found"));
       return bookingMapper.toResponse(booking);
    }

    public Page<BookingResponse> getUserBookings(int page,int size,String userid){
        Pageable pageble = PageRequest.of(page,size);
        Page<Booking> bookings =bookingRepository.findAllByUserId(pageble,userid);
        return bookings.map(bookingMapper::toResponse);
    }

    public BookingStatus cancelBooking(String bookId){
        Booking booking= bookingRepository.findById(bookId).orElseThrow(()-> new BookingNotFound("Booking Not Found"));
        PaymentResponse response  =restClient.get().uri("/api/payments/booking/"+booking.getBook_id()).retrieve().body(PaymentResponse.class);
        if (response == null) {
            throw new PaymentRefundFailed(
                    "Payment details not found");
        }
        PaymentResponse paymentResponse  = restClient.post().uri("/api/payments/"+response.paymentId()+"/cancel").retrieve().body(PaymentResponse.class);
        if (paymentResponse == null) {
            throw new PaymentRefundFailed(
                    "No response received from PaymentService");
        }
        String status = paymentResponse.paymentStatus();
        if (status == null){
            throw  new PaymentFailed("Payment Internal error");
        }
        if(status.equals("REFUNDED")
                ||status.equals("REFUND_PENDING")
                || status.equals("CANCELLED")){
            booking.setStatus(BookingStatus.CANCELLED);
           Seat seat = booking.getSeat();
           seat.setSeatStatus(SeatStatus.AVAILABLE);
           seatRepository.save(seat);
            bookingRepository.save(booking);
        }
        else {
           throw new PaymentRefundFailed("Payment cancellation/refund failed ");
        }
        return booking.getStatus();
    }

    public BookingStatus getBookingStatus(String bookId){
        Booking booking = bookingRepository.findById(bookId).orElseThrow(()-> new BookingNotFound(" BOOKING NOT FOUND "));
        return booking.getStatus();
    }

    @Transactional
    public BookingStatus confirmBooking(String bookId){
      Booking  booking = bookingRepository.findById(bookId).orElseThrow(()-> new BookingNotFound("Booking Not Found"));
        booking.setStatus(BookingStatus.CONFIRMED);
        return booking.getStatus();
    }
    public BookingResponse bookingExists(String bookId){
        Booking  booking = bookingRepository.findById(bookId).orElseThrow(()-> new BookingNotFound("Booking Not Found"));
        return bookingMapper.toResponse(booking);
    }

}
