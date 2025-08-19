package com.saad.jobRec.services;

import java.util.Date;
import java.util.Optional;

import com.saad.jobRec.entities.Application;
import com.saad.jobRec.entities.JobOffer;
import com.saad.jobRec.repositories.ApplicationRepository;
import com.saad.jobRec.repositories.JobOfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.saad.jobRec.entities.Candidat;
import com.saad.jobRec.repositories.CandidatRepository;

@Service
public class CandidateService {

    @Autowired
    private CandidatRepository candidatRepository;
private JobOfferRepository jobofferRepository;
private ApplicationRepository applicationRepository;
    // Create or Update candidate profile
    public Candidat saveOrUpdateProfile(Candidat candidat) {
        return candidatRepository.save(candidat);
    }

    // Get candidate profile by username
    public Optional<Candidat> getProfileByUsername(String username) {
        return candidatRepository.findByUsername(username);
    }

    // Get profile by ID (optional)
    public Optional<Candidat> getProfileById(Long id) {
        return candidatRepository.findById(id);
    }

}
