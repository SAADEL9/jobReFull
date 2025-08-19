package com.saad.jobRec.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.saad.jobRec.entities.Application;
import com.saad.jobRec.services.ApplicationService;

@RestController
@RequestMapping("/api/applications")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class ApplicationController {
    @Autowired
    private ApplicationService applicationService;

    @PostMapping("/apply")
    @PreAuthorize("hasRole('CANDIDAT')")
    public ResponseEntity<Application> applyToOffer(@RequestParam Long offerId, @RequestParam Long candidatId) {
        Application saved = applicationService.applyToJob(offerId, candidatId);
        return ResponseEntity.ok(saved);
    }
    @GetMapping("/candidat/{candidatId}")
    @PreAuthorize("hasRole('CANDIDAT')")
    public ResponseEntity<List<Application>> getByCandidat(@PathVariable Long candidatId) {
        return ResponseEntity.ok(applicationService.getApplicationsForCandidat(candidatId));
    }
    @GetMapping("/recruiter/{recruiterId}")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<List<Application>> getByRecruiter(@PathVariable Long recruiterId , Authentication authentication) {
        String username = authentication.getName();
        return ResponseEntity.ok(applicationService.getApplicationsForRecruiter(recruiterId));
    }

    @PutMapping("/{applicationId}/status")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<Application> updateStatus(@PathVariable Long applicationId, @RequestParam String newStatus) {
        Application application = applicationService.updateStatus(applicationId, newStatus);
        return ResponseEntity.ok(application);
    }
}
