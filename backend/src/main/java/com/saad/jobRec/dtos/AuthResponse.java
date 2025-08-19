package com.saad.jobRec.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data


public class AuthResponse {
    private String token;
    private String role; // Add this field

    public AuthResponse(String token, String role) {
        this.token = token;
        this.role = role;
    }
    // Constructor, getters, and setters
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRole() { // Add this getter
        return role;
    }

    public void setRole(String role) { // Add this setter
        this.role = role;
    }
}