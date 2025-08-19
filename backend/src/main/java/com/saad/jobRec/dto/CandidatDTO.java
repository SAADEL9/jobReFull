package com.saad.jobRec.dto;

import com.saad.jobRec.entities.Candidat;
import lombok.Data;

@Data
public class CandidatDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;

    public static CandidatDTO fromEntity(Candidat candidat) {
        if (candidat == null) return null;
        
        CandidatDTO dto = new CandidatDTO();
        dto.setId(candidat.getId());
        dto.setFirstName(candidat.getFirstName());
        dto.setLastName(candidat.getLastName());

        return dto;
    }
}
