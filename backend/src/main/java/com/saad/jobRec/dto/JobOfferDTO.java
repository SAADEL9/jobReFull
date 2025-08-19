package com.saad.jobRec.dto;

import com.saad.jobRec.entities.JobOffer;
import lombok.Data;

import java.time.LocalDate;

@Data
public class JobOfferDTO {
    private Long id;
    private String title;
    private String description;
    private String location;
    private String type;
    private LocalDate deadline;
    private String salary;
    private String experienceLevel;
    private String skillsRequired;

    public static JobOfferDTO fromEntity(JobOffer jobOffer) {
        if (jobOffer == null) return null;
        
        JobOfferDTO dto = new JobOfferDTO();
        dto.setId(jobOffer.getId());
        dto.setTitle(jobOffer.getTitle());
        dto.setDescription(jobOffer.getDescription());
        dto.setLocation(jobOffer.getLocation());

        return dto;
    }
}
