package com.userauthmicroservice.dto;
public class UserResponse {
    private String message;

    public UserResponse(String message) {
        this.message = message;
    }

    // Getter and Setter
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
}
