package com.example.demo.patient.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.patient.entity.Patients;

public interface PatientRepository extends JpaRepository<Patients, Integer> {

}
