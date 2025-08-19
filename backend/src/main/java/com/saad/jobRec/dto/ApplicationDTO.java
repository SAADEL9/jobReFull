package com.saad.jobRec.dto;

import com.saad.jobRec.entities.Application;
import com.saad.jobRec.entities.Candidat;
import com.saad.jobRec.entities.JobOffer;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApplicationDTO {
    private Long id;
    private String status;
    private LocalDateTime applicationDate;
    private CandidatDTO candidat;
    private JobOfferDTO jobOffer;

    public static ApplicationDTO fromEntity(Application application) {
        ApplicationDTO dto = new ApplicationDTO();
        dto.setId(application.getId());
        dto.setStatus(application.getStatus());
        dto.setApplicationDate(application.getApplicationDate());
        
        if (application.getCandidat() != null) {
            dto.setCandidat(CandidatDTO.fromEntity(application.getCandidat()));
        }
        
        if (application.getJobOffer() != null) {
            dto.setJobOffer(JobOfferDTO.fromEntity(application.getJobOffer()));
        }
        
        return dto;
    }
}
