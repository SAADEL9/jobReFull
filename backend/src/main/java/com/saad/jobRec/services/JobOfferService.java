package com.saad.jobRec.services;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.saad.jobRec.entities.JobOffer;
import com.saad.jobRec.entities.Recruiter;
import com.saad.jobRec.repositories.JobOfferRepository;
import com.saad.jobRec.repositories.RecruiterRepository;

@Service
public class JobOfferService {

    private final JobOfferRepository jobofferRepository;
    private final RecruiterRepository recruiterRepository;

    @Autowired
    public JobOfferService(JobOfferRepository jobofferRepository, RecruiterRepository recruiterRepository) {
        this.jobofferRepository = jobofferRepository;
        this.recruiterRepository = recruiterRepository;
    }

    public JobOffer createOffer(JobOffer offer, Long recruiterId) {
        Recruiter recruiter = recruiterRepository.findById(recruiterId)
                .orElseThrow(() -> new RuntimeException("Recruiter not found"));
        offer.setRecruiter(recruiter);
        offer.setCreatedAt(new Date());
        return jobofferRepository.save(offer);
    }

    public JobOffer createOfferForUsername(JobOffer offer, String username) {
        Recruiter recruiter = recruiterRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Recruiter not found for username: " + username));
        offer.setRecruiter(recruiter);
        offer.setCreatedAt(new Date());
        return jobofferRepository.save(offer);
    }

    public List<JobOffer> getAllOffers() {
        return jobofferRepository.findAll();
    }
    public Optional<JobOffer> getJobOfferById(Long id)
    {
        return jobofferRepository.findById(id);
    }

    public JobOffer updateOfferForUsername(Long offerId, JobOffer updatedOffer, String username) {
        JobOffer existing = jobofferRepository.findById(offerId)
                .orElseThrow(() -> new RuntimeException("Offer not found"));

        Recruiter currentRecruiter = recruiterRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Recruiter not found for username: " + username));

        if (existing.getRecruiter() == null || existing.getRecruiter().getId() == null
                || !existing.getRecruiter().getId().equals(currentRecruiter.getId())) {
            throw new AccessDeniedException("You do not have permission to modify this offer");
        }

        existing.setTitle(updatedOffer.getTitle());
        existing.setDescription(updatedOffer.getDescription());
        existing.setLocation(updatedOffer.getLocation());
        existing.setType(updatedOffer.getType());

        return jobofferRepository.save(existing);
    }
    public void deleteOffer(Long id)
    {
        jobofferRepository.deleteById(id);
    }

    public void deleteOfferForUsername(Long offerId, String username) {
        JobOffer existing = jobofferRepository.findById(offerId)
                .orElseThrow(() -> new RuntimeException("Offer not found"));

        Recruiter currentRecruiter = recruiterRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Recruiter not found for username: " + username));

        if (existing.getRecruiter() == null || existing.getRecruiter().getId() == null
                || !existing.getRecruiter().getId().equals(currentRecruiter.getId())) {
            throw new AccessDeniedException("You do not have permission to delete this offer");
        }

        jobofferRepository.deleteById(offerId);
    }
}