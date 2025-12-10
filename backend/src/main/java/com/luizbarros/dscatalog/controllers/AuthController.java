package com.luizbarros.dscatalog.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.luizbarros.dscatalog.dto.EmailDTO;
import com.luizbarros.dscatalog.services.AuthService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {
	
	private final AuthService service;
	
	public AuthController(AuthService service) {
		this.service = service;
	}

	@PostMapping("/recover-token")
	public ResponseEntity<Void> createRecoverToken(@Valid @RequestBody EmailDTO body){
		service.createRecoverToken(body);
		return ResponseEntity.noContent().build();		
	}	
}
