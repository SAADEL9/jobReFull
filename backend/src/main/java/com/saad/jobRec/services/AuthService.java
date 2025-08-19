package com.saad.jobRec.services;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.saad.jobRec.dtos.AuthRequest;
import com.saad.jobRec.dtos.AuthResponse;
import com.saad.jobRec.dtos.MessageResponse;
import com.saad.jobRec.dtos.RegisterRequest;
import com.saad.jobRec.entities.Candidat;
import com.saad.jobRec.entities.ERole;
import com.saad.jobRec.entities.Recruiter;
import com.saad.jobRec.entities.Role;
import com.saad.jobRec.entities.User;
import com.saad.jobRec.repositories.CandidatRepository;
import com.saad.jobRec.repositories.RecruiterRepository;
import com.saad.jobRec.repositories.RoleRepository;
import com.saad.jobRec.repositories.UserRepository;
import com.saad.jobRec.utils.JwtUtil;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    // Correctly declare the repositories as class fields
    private final CandidatRepository candidatRepository;
    private final RecruiterRepository recruiterRepository;

    // Use the constructor to inject all dependencies
    @Autowired
    public AuthService(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserRepository userRepository,
                       RoleRepository roleRepository, PasswordEncoder passwordEncoder,
                       CandidatRepository candidatRepository, RecruiterRepository recruiterRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.candidatRepository = candidatRepository;
        this.recruiterRepository = recruiterRepository;
    }

    public AuthResponse authenticate(AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        // DEBUG: Print roles at login
        System.out.println("[DEBUG] UserDetails authorities at login: " + userDetails.getAuthorities());
        String role = userDetails.getAuthorities().iterator().next().getAuthority();

        String token = jwtUtil.generateToken(userDetails);

        return new AuthResponse(token, role);
    }

    public MessageResponse register(RegisterRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            return new MessageResponse("Error: Username is already taken!");
        }


        Set<String> strRoles = request.getRoles();
        Set<Role> roles = new HashSet<>();
        String primaryRole = "ROLE_CANDIDAT";
        if (strRoles == null || strRoles.isEmpty()) {
            Role defaultRole = roleRepository.findByName(ERole.ROLE_CANDIDAT)
                    .orElseThrow(() -> new RuntimeException("Error: Role not found."));
            roles.add(defaultRole);
        } else {
            for (String role : strRoles) {
                switch (role.toLowerCase()) {
                    case "admin":
                        roles.add(roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role not found.")));
                        primaryRole = "ROLE_ADMIN";
                        break;
                    case "recruter":
                    case "recruiter":
                        roles.add(roleRepository.findByName(ERole.ROLE_RECRUITER)
                                .orElseThrow(() -> new RuntimeException("Error: Role not found.")));
                        primaryRole = "ROLE_RECRUITER";
                        break;
                    default:
                        roles.add(roleRepository.findByName(ERole.ROLE_CANDIDAT)
                                .orElseThrow(() -> new RuntimeException("Error: Role not found.")));
                        primaryRole = "ROLE_CANDIDAT";
                }
            }
        }

        // DEBUG: Print roles at registration
        System.out.println("[DEBUG] Roles assigned at registration: " + roles);
        User user;
        switch (primaryRole) {
            case "ROLE_CANDIDAT" -> {
                Candidat candidat = new Candidat();
                candidat.setAge(request.getAge());
                candidat.setCvUrl(request.getCvUrl());
                user = candidat;
            }
            case "ROLE_RECRUITER" -> {
                Recruiter recruiter = new Recruiter();
                recruiter.setEntreprise(request.getEntreprise());
                user = recruiter;
            }
            default -> {
                user = new User();
            }
        }

        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRoles(roles);
        user.setFirstName(request.getFirstName()); // Optional fields
        user.setLastName(request.getLastName());

        userRepository.save(user); // Save correct subclass

        return new MessageResponse("User registered successfully!" + user.getRoles());
    }

}