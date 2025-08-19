package com.saad.jobRec.controllers;


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.saad.jobRec.entities.Candidat;
import com.saad.jobRec.services.CandidateService;

@RestController
@RequestMapping("/api/candidat")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class CandidatController {
    @Value("${file.upload-dir}")
    private String uploadDir;
    @Autowired
    private CandidateService candidateService;

    // Get logged-in candidate profile
    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(Principal principal) {
        String username = principal.getName(); // Gets the currently logged-in username
        Optional<Candidat> candidat = candidateService.getProfileByUsername(username);
        return candidat.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/profile")
    public ResponseEntity<Candidat> updateProfile(
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam(value = "age", required = false) Integer age,
            @RequestParam(value = "cvUrl", required = false) String cvUrl,
            @RequestParam(value = "cvFile", required = false) MultipartFile cvFile,
            Principal principal) {
        try {
            String username = principal.getName();
            Optional<Candidat> optCandidat = candidateService.getProfileByUsername(username);
            Candidat candidat = optCandidat.orElse(new Candidat());
            candidat.setUsername(username);
            candidat.setFirstName(firstName);
            candidat.setLastName(lastName);
            if (age != null) {
                candidat.setAge(age);
            }

            // --- Corrected File Handling ---
            if (cvFile != null && !cvFile.isEmpty()) {
                // Generate a unique filename to prevent security vulnerabilities
                String uniqueFilename = UUID.randomUUID().toString() + "_" + cvFile.getOriginalFilename();
                Path uploadPath = Paths.get(uploadDir + "cv/");

                // Ensure the directory exists
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Path filePath = uploadPath.resolve(uniqueFilename);
                Files.copy(cvFile.getInputStream(), filePath);

                candidat.setCvUrl(filePath.toString()); // Store the path
            } else if (cvUrl != null) {
                candidat.setCvUrl(cvUrl);
            }
            // --- End of corrected file handling ---

            Candidat updated = candidateService.saveOrUpdateProfile(candidat);
            return ResponseEntity.ok(updated);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}