package com.userauthmicroservice.service;

import com.userauthmicroservice.dto.LoginRequest;
import com.userauthmicroservice.dto.LogoutRequest;
import com.userauthmicroservice.dto.RegisterRequest;
import com.userauthmicroservice.dto.ResetPasswordRequest;
import com.userauthmicroservice.dto.UserResponse;
import com.userauthmicroservice.exception.CustomException;
import com.userauthmicroservice.model.User;
import com.userauthmicroservice.repository.UserRepository;
import com.userauthmicroservice.util.PasswordUtil;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

	private final UserRepository userRepository;
	private final PasswordUtil passwordUtil;
	private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

	public UserServiceImpl(UserRepository userRepository, PasswordUtil passwordUtil) {
		this.userRepository = userRepository;
		this.passwordUtil = passwordUtil;
	}

	@Override
	public UserResponse registerUser(RegisterRequest request) {
		if (userRepository.existsByUsername(request.getUsername())) {
			throw new CustomException("Username already exists");
		}
		User user = new User();
		user.setUsername(request.getUsername());
		user.setPassword(passwordUtil.hashPassword(request.getPassword()));
		userRepository.save(user);
		log.info("User registered: {}", request.getUsername());
		return new UserResponse("User registered successfully");
	}

	@Override
	public UserResponse loginUser(LoginRequest request) {
		User user = userRepository.findByUsername(request.getUsername())
				.orElseThrow(() -> new CustomException("Invalid username or password"));
		if (!passwordUtil.matches(request.getPassword(), user.getPassword())) {
			throw new CustomException("Invalid username or password");
		}
		log.info("User logged in: {}", request.getUsername());
		return new UserResponse("Login successful");
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username)
				.orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

		return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), // Password
																												// is
																												// already
																												// encoded
				Collections.emptyList() // Add authorities/roles here if you have them
		);
	}

	@Override
	public UserResponse logoutUser(LogoutRequest request) {
		log.info("User {} logged out", request.getUsername());
		// HTTP Basic Auth is stateless; just return success message
		return new UserResponse("Logout successful");
	}

	@Override
	public UserResponse resetPassword(ResetPasswordRequest request) {
		User user = userRepository.findByUsername(request.getUsername())
				.orElseThrow(() -> new CustomException("User not found"));

		if (!passwordUtil.matches(request.getOldPassword(), user.getPassword())) {
			throw new CustomException("Old password is incorrect");
		}

		user.setPassword(passwordUtil.hashPassword(request.getNewPassword()));
		userRepository.save(user);

		log.info("User {} password reset successful", request.getUsername());

		return new UserResponse("Password reset successful");
	}

}
