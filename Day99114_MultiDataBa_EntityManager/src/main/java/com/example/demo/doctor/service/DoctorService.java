package com.example.demo.doctor.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.doctor.entity.Doctorr;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service("oracle")
public class DoctorService {
		
	
    
	@PersistenceContext(unitName = "oracle")		// injects the Oracle-specific EntityManager
    private EntityManager entityManager;
    
    
    
    // Save Doctor using EntityManager
    @Transactional
    public Doctorr saveDoctorr(Doctorr doctorr) {
    	entityManager.persist(doctorr);		// Insert into Oracle DB
    	System.out.println("Saved Doc data in oracle");
    	return doctorr;
    }
    
    
    public List<Doctorr> getAllDoctors(){
    	
    	// JPQL query must match entity name "Doctorr"
    	return entityManager.createQuery("SELECT d FROM Doctorr d",Doctorr.class).getResultList();
    }

}
