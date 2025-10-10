package com.example.demo.patient.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "patients")
public class Patients {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer patientId;
    private String name;
    private int age;
    private String gender;

	
}
