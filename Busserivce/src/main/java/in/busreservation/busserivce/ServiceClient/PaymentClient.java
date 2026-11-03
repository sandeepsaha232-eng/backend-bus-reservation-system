package in.busreservation.busserivce.ServiceClient;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestClient;

@Configuration
public class PaymentClient {
    private final String url ;
    PaymentClient(@Value("${payment.service.url}") String url ){
        this.url = url;
    }

    @Bean
    public RestClient restClient(RestClient.Builder clientBuilder) {
        return clientBuilder.baseUrl(url).build();
    }
}
