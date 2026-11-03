package in.busreservation.paymentservice.Service;

import in.busreservation.paymentservice.DTO.BookingPaymentDetails;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;


@Component
public class BusServiceClient {
    private final RestClient restClient;
    public BusServiceClient(@Value("${bus.service.url}" ) String url ){
        this.restClient = RestClient.builder().baseUrl(url).build();
    }
    public BookingPaymentDetails getPaymentPayment(String bookingId){
     return  restClient.get().uri("api/bookings/{bookingId}/payment-details",bookingId)
                .retrieve().body(BookingPaymentDetails.class);
    }
}
