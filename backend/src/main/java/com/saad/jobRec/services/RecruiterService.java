package com.saad.jobRec.services;

import com.saad.jobRec.entities.Recruiter;

import com.saad.jobRec.repositories.RecruiterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RecruiterService {
    @Autowired
    private RecruiterRepository recruiterRepository;
    public Recruiter saveOrUpdateProfile(Recruiter recruiter){
        return recruiterRepository.save(recruiter);
    }
    public Optional<Recruiter> getProfileByUsername(String username){
        return recruiterRepository.findByUsername(username);
    }
    public Optional<Recruiter> getProfileById(Long id){
        return recruiterRepository.findById(id);
    }

}