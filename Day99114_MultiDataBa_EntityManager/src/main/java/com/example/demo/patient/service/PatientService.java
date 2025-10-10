package com.example.demo.patient.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.patient.entity.Patients;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class PatientService {
	
	@PersistenceContext(unitName = "postgres")
	private EntityManager entityManager;
	
	
	@Transactional
	public Patients savePatient(Patients patients) {
		entityManager.persist(patients);
		return patients;
	}
	
	  public List<Patients> getAllPatients() {
	        return entityManager.createQuery("SELECT p FROM Patients p", Patients.class).getResultList();
	    }

}
