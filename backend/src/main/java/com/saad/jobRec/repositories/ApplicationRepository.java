package com.saad.jobRec.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.saad.jobRec.entities.Application;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    @Query("SELECT a FROM Application a WHERE a.candidat.id = :candidatId")
    List<Application> findByCandidatId(@Param("candidatId") Long candidatId);

    @Query("SELECT a FROM Application a WHERE a.jobOffer.recruiter.id = :recruiterId")
    List<Application> findByRecruiterId(@Param("recruiterId") Long recruiterId);
}
