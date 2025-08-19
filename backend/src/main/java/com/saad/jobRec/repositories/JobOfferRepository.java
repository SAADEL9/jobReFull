package com.saad.jobRec.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saad.jobRec.entities.JobOffer;

public interface JobOfferRepository extends JpaRepository<JobOffer, Long> {
}
