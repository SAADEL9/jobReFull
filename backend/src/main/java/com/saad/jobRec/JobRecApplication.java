package com.saad.jobRec;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.CommandLineRunner;

import org.springframework.context.annotation.Bean;

import com.saad.jobRec.entities.ERole;
import com.saad.jobRec.entities.Role;
import com.saad.jobRec.repositories.RoleRepository;
@SpringBootApplication
public class JobRecApplication {

	public static void main(String[] args) {
		SpringApplication.run(JobRecApplication.class, args);
	}
	@Bean
	CommandLineRunner initRoles(RoleRepository roleRepository) {
		return args -> {
			if (!roleRepository.existsByName(ERole.ROLE_ADMIN)) {
				roleRepository.save(new Role(null, ERole.ROLE_ADMIN));
			}
			if (!roleRepository.existsByName(ERole.ROLE_RECRUITER)) {
				roleRepository.save(new Role(null, ERole.ROLE_RECRUITER));
			}
			if (!roleRepository.existsByName(ERole.ROLE_CANDIDAT)) {
				roleRepository.save(new Role(null, ERole.ROLE_CANDIDAT));
			}
		};
	}


}
