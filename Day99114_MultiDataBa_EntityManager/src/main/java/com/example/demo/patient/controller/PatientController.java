package com.example.demo.patient.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.patient.entity.Patients;
import com.example.demo.patient.service.PatientService;

@RestController
public class PatientController {
	
	private final PatientService service;

	public PatientController(PatientService service) {
		this.service = service;
	}
	
	@PostMapping("patients")
	public Patients addPatients(@RequestBody Patients patients) {
		
		return service.savePatient(patients);
	}
	
	@GetMapping("patients")
	public List<Patients> getAllPatients(){
		return service.getAllPatients();
	}
	
	
	
	

}
