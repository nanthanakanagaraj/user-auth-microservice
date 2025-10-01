package com.userauthmicroservice.service;

import com.userauthmicroservice.dto.LoginRequest;
import com.userauthmicroservice.dto.LogoutRequest;
import com.userauthmicroservice.dto.RegisterRequest;
import com.userauthmicroservice.dto.ResetPasswordRequest;
import com.userauthmicroservice.dto.UserResponse;

public interface UserService {
	UserResponse registerUser(RegisterRequest request);

	UserResponse loginUser(LoginRequest request);

	UserResponse logoutUser(LogoutRequest request);

	UserResponse resetPassword(ResetPasswordRequest request);

}
