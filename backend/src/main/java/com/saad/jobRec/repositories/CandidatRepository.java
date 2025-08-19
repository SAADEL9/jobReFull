package com.saad.jobRec.repositories;
import java.util.Optional;
import com.saad.jobRec.entities.Candidat;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidatRepository extends JpaRepository<Candidat, Long> {
    Optional<Candidat> findByUsername(String username);
}