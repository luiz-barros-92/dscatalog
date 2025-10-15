package com.luizbarros.dscatalog.services;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luizbarros.dscatalog.dto.CategoryDTO;
import com.luizbarros.dscatalog.repositories.CategoryRepository;

@Service
public class CategoryService {	
	
	private final CategoryRepository repository;
		
	public CategoryService(CategoryRepository repository) {
		this.repository = repository;
	}

	@Transactional(readOnly = true)
	public List<CategoryDTO> findAll(){
		return repository.findAll().stream().map(CategoryDTO::new).toList();	
	}
}
