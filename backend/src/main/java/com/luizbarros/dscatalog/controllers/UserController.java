package com.luizbarros.dscatalog.controllers;

import java.net.URI;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.luizbarros.dscatalog.dto.UserDTO;
import com.luizbarros.dscatalog.dto.UserInsertDTO;
import com.luizbarros.dscatalog.dto.UserUpdateDTO;
import com.luizbarros.dscatalog.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/users")
public class UserController {
	
	private final UserService service;

	public UserController(UserService service) {
		this.service = service;
	}
	
	@GetMapping
	public ResponseEntity<Page<UserDTO>> findAll(Pageable pageable){
		return ResponseEntity.ok().body(service.findAll(pageable));		
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<UserDTO> findById(@PathVariable Long id) {
		return ResponseEntity.ok().body(service.findById(id));		
	}
	
	@PostMapping
	public ResponseEntity<UserDTO> insert(@Valid @RequestBody UserInsertDTO dto) {
		UserDTO userDTO= service.insert(dto);
		URI uri = ServletUriComponentsBuilder
			.fromCurrentRequest()
			.path("/{id}")
			.buildAndExpand(userDTO.getId()).toUri();
		return ResponseEntity.created(uri).body(userDTO);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<UserDTO> update(@PathVariable Long id, @Valid @RequestBody UserUpdateDTO dto) {
		UserDTO newDto = service.update(id, dto);		
		return ResponseEntity.ok().body(newDto);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		service.delete(id);
		return ResponseEntity.noContent().build();
	}	
}
