package com.example.demo.doctor.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.doctor.entity.Doctorr;
import com.example.demo.doctor.service.DoctorService;

@RestController

public class DoctorController {

	private final DoctorService service;

	public DoctorController(@Qualifier("oracle")DoctorService service) {
		this.service = service;
	}
	
	
	@PostMapping("doctors")
	public Doctorr addDoctorr(@RequestBody Doctorr doctorr) {
		return service.saveDoctorr(doctorr);
		
	}
	
	@GetMapping("doctors")
	public List<Doctorr> getAllDoctorrs(){
		return service.getAllDoctors();
	}

}
