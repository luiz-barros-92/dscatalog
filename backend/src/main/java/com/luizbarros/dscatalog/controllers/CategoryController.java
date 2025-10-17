package com.luizbarros.dscatalog.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.luizbarros.dscatalog.dto.CategoryDTO;
import com.luizbarros.dscatalog.services.CategoryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/categories")
public class CategoryController {
	
	private final CategoryService service;

	public CategoryController(CategoryService service) {
		this.service = service;
	}
	
	@GetMapping
	public ResponseEntity<List<CategoryDTO>> findAll() {
		return ResponseEntity.ok().body(service.findAll());		
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<CategoryDTO> findById(@PathVariable Long id) {
		return ResponseEntity.ok().body(service.findById(id));		
	}
	
	@PostMapping
	public ResponseEntity<CategoryDTO> insert(@Valid @RequestBody CategoryDTO dto) {
		dto = service.insert(dto);
		URI uri = ServletUriComponentsBuilder
			.fromCurrentRequest()
			.path("/{id}")
			.buildAndExpand(dto.id()).toUri();
		return ResponseEntity.created(uri).body(dto);
	}
}
