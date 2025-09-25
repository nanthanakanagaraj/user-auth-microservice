package com.userauthmicroservice.service;

import com.userauthmicroservice.dto.LoginRequest;
import com.userauthmicroservice.dto.RegisterRequest;
import com.userauthmicroservice.dto.UserResponse;
import com.userauthmicroservice.exception.CustomException;
import com.userauthmicroservice.model.User;
import com.userauthmicroservice.repository.UserRepository;
import com.userauthmicroservice.util.PasswordUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

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
}
