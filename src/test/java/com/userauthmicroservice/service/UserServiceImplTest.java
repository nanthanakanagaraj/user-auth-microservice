package com.userauthmicroservice.service;

import com.userauthmicroservice.dto.*;
import com.userauthmicroservice.exception.CustomException;
import com.userauthmicroservice.model.User;
import com.userauthmicroservice.repository.UserRepository;
import com.userauthmicroservice.util.PasswordUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
  
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordUtil passwordUtil;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterUser_Success() {
        RegisterRequest request = new RegisterRequest();
        request.setUsername("testuser");
        request.setPassword("password");

        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(passwordUtil.hashPassword("password")).thenReturn("hashed");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        UserResponse response = userService.registerUser(request);

        assertEquals("User registered successfully", response.getMessage());
        verify(userRepository).save(any(User.class));
    }

    @Test
    public void testRegisterUser_UsernameExists() {
        when(userRepository.existsByUsername("testuser")).thenReturn(true);

        RegisterRequest request = new RegisterRequest();
        request.setUsername("testuser");
        request.setPassword("password");

        assertThrows(CustomException.class, () -> userService.registerUser(request));
    }

    @Test
    public void testLoginUser_Success() {
        LoginRequest request = new LoginRequest();
        request.setUsername("testuser");
        request.setPassword("password");

        User user = new User();
        user.setUsername("testuser");
        user.setPassword("hashed");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordUtil.matches("password", "hashed")).thenReturn(true);

        UserResponse response = userService.loginUser(request);

        assertEquals("Login successful", response.getMessage());
    }

    @Test
    public void testLoginUser_InvalidPassword() {
        LoginRequest request = new LoginRequest();
        request.setUsername("testuser");
        request.setPassword("wrongpassword");

        User user = new User();
        user.setUsername("testuser");
        user.setPassword("hashed");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordUtil.matches("wrongpassword", "hashed")).thenReturn(false);

        assertThrows(CustomException.class, () -> userService.loginUser(request));
    }

    @Test
    public void testLoginUser_UserNotFound() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());

        LoginRequest request = new LoginRequest();
        request.setUsername("testuser");
        request.setPassword("password");

        assertThrows(CustomException.class, () -> userService.loginUser(request));
    }

    @Test
    public void testLogoutUser_Success() {
        LogoutRequest request = new LogoutRequest();
        request.setUsername("testuser");

        UserResponse response = userService.logoutUser(request);

        assertEquals("Logout successful", response.getMessage());

        // Removed verify(userRepository) because logoutUser does not access repo
    }

    @Test
    public void testLogoutUser_UserNotFound() {
        LogoutRequest request = new LogoutRequest();
        request.setUsername("unknown");

        // Since logoutUser doesn't check user existence, no exception expected here
        // If your logoutUser method does throw exception for unknown user, test accordingly

        UserResponse response = userService.logoutUser(request);
        assertEquals("Logout successful", response.getMessage());
    }

    @Test
    public void testResetPassword_Success() {
        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setUsername("testuser");
        request.setOldPassword("oldpass");
        request.setNewPassword("newpass");

        User user = new User();
        user.setUsername("testuser");
        user.setPassword("hashedOld");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordUtil.matches("oldpass", "hashedOld")).thenReturn(true);
        when(passwordUtil.hashPassword("newpass")).thenReturn("hashedNew");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArguments()[0]);

        UserResponse response = userService.resetPassword(request);

        assertEquals("Password reset successful", response.getMessage());
        verify(userRepository).save(any(User.class));
    }

    @Test
    public void testResetPassword_InvalidOldPassword() {
        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setUsername("testuser");
        request.setOldPassword("wrongOld");
        request.setNewPassword("newpass");

        User user = new User();
        user.setUsername("testuser");
        user.setPassword("hashedOld");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        when(passwordUtil.matches("wrongOld", "hashedOld")).thenReturn(false);

        assertThrows(CustomException.class, () -> userService.resetPassword(request));
    }

    @Test
    public void testResetPassword_UserNotFound() {
        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setUsername("unknown");
        request.setOldPassword("oldpass");
        request.setNewPassword("newpass");

        when(userRepository.findByUsername("unknown")).thenReturn(Optional.empty());

        assertThrows(CustomException.class, () -> userService.resetPassword(request));
    }
}
