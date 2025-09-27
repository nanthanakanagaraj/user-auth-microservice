package com.userauthmicroservice.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.userauthmicroservice.service.UserServiceImpl;

@Configuration
public class AppConfig {

    // Example bean for password encoder
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
 // Security filter chain bean to configure HTTP security
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())  // Disable CSRF for easier testing, enable in production appropriately
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/register","/api/login", "/api/logout", "/api/reset" ).permitAll()  // Allow register endpoint without auth
                .anyRequest().authenticated()                 // All other endpoints require authentication
            )
            .httpBasic();  // Use HTTP Basic auth (can be changed to JWT etc. depending on your setup)

        return http.build();
    }
    @Bean
    public UserDetailsService userDetailsService(UserServiceImpl userServiceImpl) {
        return userServiceImpl;
    }
    


    

}
