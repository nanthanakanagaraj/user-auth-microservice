package com.userauthmicroservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.userauthmicroservice.service.UserService;
import com.userauthmicroservice.service.UserServiceImpl;

@Configuration
public class AppConfig {


	//bean for password encoder
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}


	// Security filter chain bean to configure HTTP security
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable()) // Disable CSRF for easier testing
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/api/register", "/api/login", "/api/logout", "/api/reset", "/register.html",
								"/login.html", "/logout.html", "/reset.html", "/dashboard.html")
						.permitAll() // Allow these endpoints without authentication
						.anyRequest().authenticated() // All other endpoints require authentication
				).httpBasic(); // Use HTTP Basic auth (can change to JWT or other)

		return http.build();
	}

	@Bean
	public UserDetailsService userDetailsService(UserService userService) {
	    return (UserDetailsService) userService;
	}

}
