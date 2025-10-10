package com.example.demo.doctor.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.doctor.entity.Doctorr;

public interface DoctorRepository extends JpaRepository<Doctorr, Integer> {

}
