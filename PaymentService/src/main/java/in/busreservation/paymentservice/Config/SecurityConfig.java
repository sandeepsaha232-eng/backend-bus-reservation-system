package in.busreservation.paymentservice.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        return http
                .csrf(csrf ->
                        csrf.ignoringRequestMatchers("/api/payments/**")
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/payments/**").permitAll()
                        .anyRequest().authenticated()
                )
                .build();
    }
}