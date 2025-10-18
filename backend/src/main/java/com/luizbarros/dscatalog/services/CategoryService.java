package com.luizbarros.dscatalog.services;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.luizbarros.dscatalog.dto.CategoryDTO;
import com.luizbarros.dscatalog.entities.Category;
import com.luizbarros.dscatalog.repositories.CategoryRepository;
import com.luizbarros.dscatalog.services.exceptions.DatabaseException;
import com.luizbarros.dscatalog.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

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

	@Transactional(readOnly = true)
	public CategoryDTO findById(Long id) {
		return repository
				.findById(id)
				.map(CategoryDTO::new)
				.orElseThrow(() -> new ResourceNotFoundException("Resource not found"));		
	}

	@Transactional
	public CategoryDTO insert(CategoryDTO dto) {
		return new CategoryDTO(repository.save(new Category(null, dto.name())));
	}

	@Transactional
	public CategoryDTO update(Long id, CategoryDTO dto) {
		try {
			Category entity = repository.getReferenceById(id);
			entity.setName(dto.name());
			entity = repository.save(entity);
			return new CategoryDTO(entity);
		}
		catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Id - " + id + ": not found");
		}		
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	public void delete(Long id) {
		if (!repository.existsById(id)) {
			throw new ResourceNotFoundException("Resource not found");
		}
		try {
	        	repository.deleteById(id);    		
		}
	    	catch (DataIntegrityViolationException e) {
	        	throw new DatabaseException("Referential integrity violation");
	   	}
	}
}
