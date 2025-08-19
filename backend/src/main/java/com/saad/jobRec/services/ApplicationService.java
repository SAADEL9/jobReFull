package com.saad.jobRec.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.saad.jobRec.entities.Application;
import com.saad.jobRec.entities.Candidat;
import com.saad.jobRec.entities.JobOffer;
import com.saad.jobRec.repositories.ApplicationRepository;
import com.saad.jobRec.repositories.CandidatRepository;
import com.saad.jobRec.repositories.JobOfferRepository;

@Service
public class ApplicationService {
    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private CandidatRepository candidatRepository;
    @Autowired
    private JobOfferRepository jobOfferRepository;

    public Application applyToJob(Long offerId, Long candidatId) {
        Optional<Candidat> candidatOpt = candidatRepository.findById(candidatId);
        Optional<JobOffer> offerOpt = jobOfferRepository.findById(offerId);
        if (candidatOpt.isEmpty() || offerOpt.isEmpty()) {
            throw new RuntimeException("Candidat or JobOffer not found");
        }
        Application application = new Application();
        application.setCandidat(candidatOpt.get());
        application.setJobOffer(offerOpt.get());
        application.setApplicationDate(LocalDateTime.now());
        application.setStatus("pending");
        return applicationRepository.save(application);
    }

    public List<Application> getApplicationsForCandidat(Long candidatId) {
        return applicationRepository.findByCandidatId(candidatId);
    }

    public List<Application> getApplicationsForRecruiter(Long recruiterId) {
        return applicationRepository.findByRecruiterId(recruiterId);
    }

    public Application updateStatus(Long applicationId, String newStatus) {
        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application not found"));
        application.setStatus(newStatus);
        return applicationRepository.save(application);
    }
}
