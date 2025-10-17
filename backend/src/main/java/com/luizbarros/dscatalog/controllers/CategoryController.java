package com.luizbarros.dscatalog.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.luizbarros.dscatalog.dto.CategoryDTO;
import com.luizbarros.dscatalog.services.CategoryService;

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
}
