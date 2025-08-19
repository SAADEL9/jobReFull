package com.saad.jobRec.controllers;


import com.saad.jobRec.entities.Recruiter;
import com.saad.jobRec.services.RecruiterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("api/recruiter")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class RecruiterController {
    @Autowired
    private RecruiterService recruiterService;

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(Principal principal) {
        String username = principal.getName(); // Gets the currently logged-in username
        Optional<Recruiter> recruiter = recruiterService.getProfileByUsername(username);
        return recruiter.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/profile")
    public ResponseEntity<Recruiter> updateProfile(@RequestBody Recruiter recruiter, Principal principal) {
        recruiter.setUsername(principal.getName()); // Securely set username from logged-in user
        Recruiter updated = recruiterService.saveOrUpdateProfile(recruiter);
        return ResponseEntity.ok(updated);
    }
}
