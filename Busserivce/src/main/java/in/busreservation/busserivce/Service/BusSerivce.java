package in.busreservation.busserivce.Service;

import in.busreservation.busserivce.DTO.BusDTO.BusCreateRequest;
import in.busreservation.busserivce.DTO.BusDTO.BusRequest;
import in.busreservation.busserivce.DTO.BusDTO.BusResponse;
import in.busreservation.busserivce.DTO.BusDTO.BusUpdateRequest;
import in.busreservation.busserivce.ENUM.BusStatus;
import in.busreservation.busserivce.Entity.Bus;
import in.busreservation.busserivce.Exception.AdminNotFound;
import in.busreservation.busserivce.Exception.BusNotFound;
import in.busreservation.busserivce.MapStruct.BusMapper;
import in.busreservation.busserivce.Repository.BusRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;



@Service
public class BusSerivce {
//    Feature	Method	Purpose
//    Create bus	createBus()	Admin registers a bus
//    Get bus	getBusById()	Get one bus
//    List buses	getAllBuses()	Paginated bus list
//    Get admin buses	getAdminBuses()	List buses belonging to an admin
//    Update bus	updateBus()	Change bus details
//    Deactivate bus	deactivateBus()	Take bus out of service
//    Check bus	busExists()	Internal validation
    private final BusRepository busRepository;
    private final BusMapper busMapper;
    BusSerivce(BusRepository busRepository,BusMapper busMapper) {
        this.busRepository = busRepository;
        this.busMapper=busMapper;
    }
    @Transactional
    public BusResponse createBus(BusCreateRequest busCreateRequest) {
        Bus bus = busMapper.toEntity(busCreateRequest);
        busRepository.save(bus);
        return busMapper.toResponse(bus);
    }

    public BusResponse  getBusById(String id){
       Bus bus = busRepository.findById(id).orElseThrow(()-> new BusNotFound("Bus Not Found"));
       return busMapper.toResponse(bus);
    }

    public Page<BusResponse> getAllBuses(int page , int size){// page -> page no like 1 , 2 ..and size , mean how many data in the single page
        Pageable pageable = PageRequest.of(page,size);
        Page<Bus> pagebus =  busRepository.findAll(pageable);
         return pagebus.map(busMapper::toResponse);
//        return pagebus.map(bus-> busMapper.toResponse(bus));
    }
    public Page<BusResponse> getAdminBuses(int page , int size, String  id ){
        Pageable pageable = PageRequest.of(page,size);
        Page<Bus> pagebus = busRepository.findAllByadminEmailId(pageable,id);
        return pagebus.map(busMapper::toResponse);
    }
    @Transactional
    public BusResponse updateBus(String busId,BusUpdateRequest request) {
       Bus oldbus=busRepository.findById(busId).orElseThrow(()-> new BusNotFound("Bus Not Found"));
       busMapper.updateBusFromRequest(request,oldbus);
       return busMapper.toResponse(oldbus);
    }
    @Transactional
    public BusStatus deactivateBus(String id  ){
        Bus bus =busRepository.findById(id).orElseThrow(()->new AdminNotFound("Admin doesnt exsit "));
        bus.setStatus(BusStatus.DEACTIVATE);
        return BusStatus.DEACTIVATE;
    }
    public boolean busExists(BusRequest request){
       return  busRepository.existsById(request.getAdminEmailId());
    }
}
