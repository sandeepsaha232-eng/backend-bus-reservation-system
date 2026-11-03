package in.busreservation.busserivce.Service;

import in.busreservation.busserivce.DTO.SeatDTO.SeatCreateRequest;

import in.busreservation.busserivce.DTO.SeatDTO.SeatResponse;
import in.busreservation.busserivce.DTO.SeatDTO.SeatUpdateRequest;
import in.busreservation.busserivce.ENUM.SeatStatus;
import in.busreservation.busserivce.Entity.Bus;
import in.busreservation.busserivce.Entity.Seat;
import in.busreservation.busserivce.Exception.BusNotFound;
import in.busreservation.busserivce.Exception.SeatNotFound;
import in.busreservation.busserivce.MapStruct.SeatMapper;
import in.busreservation.busserivce.Repository.BusRepository;
import in.busreservation.busserivce.Repository.SeatRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SeatService {
//    Feature	Method	Purpose
//    Create seats	createSeats()	Create/configure seats for a bus
//    Get seats for trip	getTripSeats()	Display seat layout/availability
//    Get particular seat	getSeatById()	Get one seat
//    Block seat	blockSeat()	Admin temporarily/permanently blocks a seat
//    Unblock seat	unblockSeat()	Admin makes blocked seat available
//    Update seat	updateSeat()	Change seat information
//    Check availability	isSeatAvailable()	Check whether a seat can be booked

    private final SeatRepository seatRepository;
    private  final BusRepository busRepository;
    private  final  SeatMapper seatMapper;
    SeatService(SeatRepository seatRepository,SeatMapper seatMapper,BusRepository busRepository) {
        this.seatRepository = seatRepository;
        this.seatMapper = seatMapper;
        this.busRepository=busRepository;
    }
    @Transactional
    public SeatResponse createSeats(SeatCreateRequest seatCreateRequest,String busId){
        Seat seat = seatMapper.toEntity(seatCreateRequest);
        Bus bus = busRepository.findById(busId).orElseThrow(()->new BusNotFound("Bus not found "));
        seat.setBus(bus);;
        seatRepository.save(seat);
        return seatMapper.toResponse(seat);
    }
    public List<SeatResponse> getAvailableSeats(String busId){
      List<Seat> seatlist   =  seatRepository.findSeatByBusBusid(busId);
       seatlist  =seatlist.stream()
               .filter(
               seat->{ return seat.getSeatStatus() == SeatStatus.AVAILABLE;}
               )
               .toList();
      return seatlist.stream().map(seatMapper::toResponse).toList();
    }
    public Page<SeatResponse> getTripSeats(int page ,int size,String busId){
        Pageable pageable = PageRequest.of(page,size);
        Page<Seat> seatPage  =  seatRepository.findSeatByBusBusid(pageable,busId);
       return seatPage.map(seatMapper::toResponse);
    }

    public SeatResponse getSeatById(String id) {
        Seat seat = seatRepository.findById(id)
                .orElseThrow(() -> new SeatNotFound("Seat Not Found"));
        return seatMapper.toResponse(seat);
    }
    @Transactional
    public SeatResponse blockSeat(String id ){
       Seat seat  =seatRepository.findById(id).orElseThrow(()->new SeatNotFound("Seat Not Found"));
       seat.setSeatStatus(SeatStatus.BLOCK);
       return  seatMapper.toResponse(seat);
    }
    @Transactional
    public SeatResponse unblockSeat(String id ){
        Seat seat  =seatRepository.findById(id).orElseThrow(()->new SeatNotFound("Seat Not Found"));
        seat.setSeatStatus(SeatStatus.AVAILABLE);
        return  seatMapper.toResponse(seat);
    }
    @Transactional
    public SeatResponse updateSeat(SeatUpdateRequest request,String id){
       Seat  newseat = seatMapper.toEntity(request);
       Seat oldseat  =seatRepository.findById(id).orElseThrow(()->new SeatNotFound("Seat Not Found"));
       seatMapper.updateEntity(newseat,oldseat);
        return seatMapper.toResponse(oldseat);
    }
    public boolean isSeatAvailable(String seatid){
        Seat seat =seatRepository.findById(seatid).orElseThrow(()->new SeatNotFound("Seat Not Found"));
        SeatStatus status = seat.getSeatStatus();
        if(status == SeatStatus.AVAILABLE){
            return true;
        }
        return false;
    }
}
