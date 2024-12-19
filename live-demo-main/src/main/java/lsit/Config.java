package lsit;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;

import static org.springframework.security.config.Customizer.withDefaults;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
@EnableWebSecurity
public class Config {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(c -> c.disable()) // Disable CSRF for simplicity
            .oauth2Login(withDefaults()) // Enable OAuth2 login
            .authorizeHttpRequests(a -> a
                // General endpoints
                .requestMatchers("/user").authenticated() // Secure user endpoint
                
                // Beverage endpoints
                .requestMatchers(HttpMethod.GET, "/beverages/**").authenticated()
                .requestMatchers(HttpMethod.POST, "/beverages/**").authenticated()
                .requestMatchers(HttpMethod.PUT, "/beverages/**").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/beverages/**").authenticated()
                
                // Contract endpoints
                .requestMatchers(HttpMethod.GET, "/contracts/**").authenticated()
                .requestMatchers(HttpMethod.POST, "/contracts/**").authenticated()
                .requestMatchers(HttpMethod.PUT, "/contracts/**").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/contracts/**").authenticated()
                
                // Customer endpoints
                .requestMatchers(HttpMethod.GET, "/customers/**").authenticated()
                .requestMatchers(HttpMethod.POST, "/customers/**").authenticated()
                .requestMatchers(HttpMethod.PUT, "/customers/**").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/customers/**").authenticated()
                
                // Stock endpoints
                .requestMatchers(HttpMethod.POST, "/stock/**").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/stock/**").authenticated()
                .requestMatchers(HttpMethod.PUT, "/stock/**").authenticated()
                
                // Supplier endpoints
                .requestMatchers(HttpMethod.GET, "/suppliers/**").authenticated()
                .requestMatchers(HttpMethod.POST, "/suppliers/**").authenticated()
                .requestMatchers(HttpMethod.PUT, "/suppliers/**").authenticated()
                .requestMatchers(HttpMethod.DELETE, "/suppliers/**").authenticated()
                
                // Default rules
                .anyRequest().permitAll() // Permit all other requests
            );

        return http.build();
    }
}
