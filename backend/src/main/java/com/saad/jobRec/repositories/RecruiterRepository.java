package com.saad.jobRec.repositories;
import java.util.Optional;
import com.saad.jobRec.entities.Recruiter;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruiterRepository extends JpaRepository<Recruiter, Long> {
    Optional<Recruiter> findByUsername(String username);
}