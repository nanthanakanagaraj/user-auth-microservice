package com.userauthmicroservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.userauthmicroservice.dto.LoginRequest;
import com.userauthmicroservice.dto.LogoutRequest;
import com.userauthmicroservice.dto.RegisterRequest;
import com.userauthmicroservice.dto.ResetPasswordRequest;
import com.userauthmicroservice.dto.UserResponse;
import com.userauthmicroservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;


@WebMvcTest(UserController.class)
public class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Test
	@WithMockUser
	public void testRegister() throws Exception {
		RegisterRequest request = new RegisterRequest();
		request.setUsername("testuser");
		request.setPassword("password123");

		Mockito.when(userService.registerUser(Mockito.any())).thenReturn(null);

		mockMvc.perform(post("/api/register").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)).with(csrf())).andExpect(status().isOk());
	}

	@Test
	@WithMockUser
	public void testLogin() throws Exception {
		LoginRequest request = new LoginRequest();
		request.setUsername("testuser");
		request.setPassword("password123");

		Mockito.when(userService.loginUser(Mockito.any())).thenReturn(null);

		mockMvc.perform(post("/api/login").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)).with(csrf())).andExpect(status().isOk());
	}

	@Test
	@WithMockUser
	public void testLogout() throws Exception {
		LogoutRequest request = new LogoutRequest();
		request.setUsername("testuser");

		Mockito.when(userService.logoutUser(Mockito.any())).thenReturn(new UserResponse("Logout successful"));

		mockMvc.perform(post("/api/logout").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)).with(csrf())).andExpect(status().isOk());
	}

	@Test 
	@WithMockUser
	public void testResetPassword() throws Exception {
		ResetPasswordRequest request = new ResetPasswordRequest();
		request.setUsername("testuser");
		request.setOldPassword("oldpass");
		request.setNewPassword("newpass");

		Mockito.when(userService.resetPassword(Mockito.any()))
				.thenReturn(new UserResponse("Password reset successful"));

		mockMvc.perform(post("/api/reset").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(request)).with(csrf())).andExpect(status().isOk());
	}

}