package com.saad.jobRec.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.saad.jobRec.entities.JobOffer;
import com.saad.jobRec.services.JobOfferService;

@RestController
@RequestMapping("/api")
public class JobOfferController {

    private final JobOfferService jobofferService;

    @Autowired
    public JobOfferController(JobOfferService jobofferService) {
        this.jobofferService = jobofferService;
    }

    @GetMapping("/offers")
    public ResponseEntity<List<JobOffer>> getAllOffers() {
        return ResponseEntity.ok(jobofferService.getAllOffers());
    }

    @PostMapping("/recruiter/offers")
    public ResponseEntity<JobOffer> createOfferForRecruiter(@RequestBody JobOffer offer,
                                                            Principal principal) {
        JobOffer savedOffer = jobofferService.createOfferForUsername(offer, principal.getName());
        return ResponseEntity.ok(savedOffer);
    }

    // Legacy style (kept for compatibility if needed)
    @PostMapping("/offers/create")
    public ResponseEntity<JobOffer> createOffer(@RequestBody JobOffer offer,
                                                @RequestParam Long recruiterId) {
        JobOffer savedOffer = jobofferService.createOffer(offer, recruiterId);
        return ResponseEntity.ok(savedOffer);
    }
    // Public: offer details by id (idiomatic REST)
    @GetMapping("/offers/{id}")
    public ResponseEntity<JobOffer> getOfferById(@PathVariable Long id) {
        return jobofferService.getJobOfferById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Legacy compatibility: accepts an offer in body and returns its details
    @PostMapping("/offers/detail")
    public ResponseEntity<JobOffer> jobOfferDetail(@RequestBody JobOffer offer) {
        if (offer == null || offer.getId() == null) {
            return ResponseEntity.badRequest().build();
        }
        return jobofferService.getJobOfferById(offer.getId())
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
    
    // Secure update for recruiters: only the owner can update
    @PutMapping("/recruiter/offers/{id}")
    public ResponseEntity<JobOffer> updateOfferForRecruiter(@PathVariable Long id,
                                                            @RequestBody JobOffer offer,
                                                            Principal principal) {
        JobOffer updated = jobofferService.updateOfferForUsername(id, offer, principal.getName());
        return ResponseEntity.ok(updated);
    }
    @DeleteMapping("/recruiter/offers/{id}")
    public ResponseEntity<Void> deleteOfferForRecruiter(@PathVariable Long id, Principal principal)
    {
       jobofferService.deleteOfferForUsername(id, principal.getName());
       return ResponseEntity.noContent().build();
    }
}