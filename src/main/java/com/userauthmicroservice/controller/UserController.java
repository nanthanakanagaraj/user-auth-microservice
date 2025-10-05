package com.userauthmicroservice.controller;

import com.userauthmicroservice.dto.*;
import com.userauthmicroservice.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import com.userauthmicroservice.dto.LogoutRequest;
import com.userauthmicroservice.dto.ResetPasswordRequest;
import org.springframework.web.bind.annotation.*;
@Tag(name = "User Authentication APIs", description = "Endpoints for user operations")
@RestController
@RequestMapping("/api")

public class UserController {

	private final UserService userService;
	private static final Logger log = LoggerFactory.getLogger(UserController.class);

	public UserController(UserService userService) {
		this.userService = userService;
	}
	
	@PostMapping("/register")
	public ResponseEntity<UserResponse> register(@RequestBody RegisterRequest request) {
		log.info("Register API called");
		UserResponse response = userService.registerUser(request);
		return ResponseEntity.ok(response);
	}
    
	
	@PostMapping("/login")
	public ResponseEntity<UserResponse> login(@RequestBody LoginRequest request) {
		log.info("Login API called");
		UserResponse response = userService.loginUser(request);
		return ResponseEntity.ok(response);
	}
	@Operation(summary = "Logout user", description = "Ends user session or invalidates token.")
	@PostMapping("/logout")
	public ResponseEntity<UserResponse> logout(@RequestBody LogoutRequest request) {
		log.info("Logout API called for user: {}", request.getUsername());
		UserResponse response = userService.logoutUser(request);
		return ResponseEntity.ok(response);
	}
	@Operation(summary = "Reset user password", description = "Allows user to reset their password.")
	@PostMapping("/reset")
	public ResponseEntity<UserResponse> resetPassword(@RequestBody ResetPasswordRequest request) {
		log.info("Reset Password API called for user: {}", request.getUsername());
		UserResponse response = userService.resetPassword(request);
		return ResponseEntity.ok(response);
	}

}
