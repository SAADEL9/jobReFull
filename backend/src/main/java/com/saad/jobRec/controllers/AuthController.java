package com.saad.jobRec.controllers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.saad.jobRec.dtos.AuthRequest;
import com.saad.jobRec.dtos.AuthResponse;
import com.saad.jobRec.dtos.MessageResponse;
import com.saad.jobRec.dtos.RegisterRequest;
import com.saad.jobRec.services.AuthService;


@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest loginRequest) {
        System.out.println(">>> Received login: " + loginRequest);
        AuthResponse response = authService.authenticate(loginRequest);

        // Get the user's role from the authentication response
        String role = response.getRole(); // Assuming AuthResponse has a getRole() method
        System.out.println(">>> User " + loginRequest.getUsername() + " logged in with role: " + role);


        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<MessageResponse> registerUser(@RequestBody RegisterRequest request) {
        System.out.println(">>> Received registration: " + request);
        MessageResponse response = authService.register(request);
        if (response.getMessage().startsWith("Error")) {
            return ResponseEntity.badRequest().body(response);
        }
        return ResponseEntity.ok(response);
    }
}