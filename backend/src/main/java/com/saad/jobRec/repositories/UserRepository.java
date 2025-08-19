package com.saad.jobRec.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saad.jobRec.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);
}