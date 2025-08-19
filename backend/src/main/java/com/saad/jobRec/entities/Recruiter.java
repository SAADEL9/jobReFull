package com.saad.jobRec.entities;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class Recruiter extends User{
private String entreprise;
}
