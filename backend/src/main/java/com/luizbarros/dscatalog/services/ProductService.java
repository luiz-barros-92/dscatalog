package com.luizbarros.dscatalog.services;

import java.util.Arrays;
import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.luizbarros.dscatalog.dto.CategoryDTO;
import com.luizbarros.dscatalog.dto.ProductDTO;
import com.luizbarros.dscatalog.entities.Category;
import com.luizbarros.dscatalog.entities.Product;
import com.luizbarros.dscatalog.projections.ProductProjection;
import com.luizbarros.dscatalog.repositories.ProductRepository;
import com.luizbarros.dscatalog.services.exceptions.DatabaseException;
import com.luizbarros.dscatalog.services.exceptions.ResourceNotFoundException;
import com.luizbarros.dscatalog.util.Utils;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ProductService {

	private final ProductRepository repository;

	public ProductService(ProductRepository repository) {
		this.repository = repository;
	}

	@Transactional(readOnly = true)
	public Page<ProductDTO> findAll(Pageable pageable) {
		Page<Product> page = repository.findAll(pageable);
		return page.map(ProductDTO::new);
	}

	@Transactional(readOnly = true)
	public ProductDTO findById(Long id) {
		return repository.findById(id).map(ProductDTO::new)
				.orElseThrow(() -> new ResourceNotFoundException("Resource not found"));
	}

	@Transactional
	public ProductDTO insert(ProductDTO dto) {
		Product entity = new Product();
		DtoToEntity(dto, entity);
		entity = repository.save(entity);
		return new ProductDTO(entity);
	}

	@Transactional
	public ProductDTO update(Long id, ProductDTO dto) {
		try {
			Product entity = repository.getReferenceById(id);
			DtoToEntity(dto, entity);
			entity = repository.save(entity);
			return new ProductDTO(entity);
		} catch (EntityNotFoundException e) {
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
		} catch (DataIntegrityViolationException e) {
			throw new DatabaseException("Referential integrity violation");
		}
	}

	private void DtoToEntity(ProductDTO dto, Product entity) {
		entity.setName(dto.name());
		entity.setDescription(dto.description());
		entity.setPrice(dto.price());
		entity.setImgUrl(dto.imgUrl());
		entity.setDate(dto.date());
		entity.getCategories().clear();
		for (CategoryDTO catDto : dto.categories()) {
			Category cat = new Category();
			cat.setId(catDto.id());
			entity.getCategories().add(cat);
		}
	}

	@SuppressWarnings("unchecked")
	@Transactional(readOnly = true)
	public Page<ProductDTO> findAllPaged(String name, String categoryId, Pageable pageable) {
		List<Long> categoryIds = null;
		if(!"0".equals(categoryId) && categoryId != null && !categoryId.isBlank()) {
			categoryIds = Arrays.asList(categoryId.split(","))
			.stream()
			.map(Long::parseLong)
			.toList();
		}		
		Page <ProductProjection> page = repository.searchProducts(categoryIds, name.trim(), pageable);
		List<Long> productIds = page.map(x -> x.getId()).toList();		
		List<Product> entities = repository.searchProductsWithCategories(productIds);
		entities = (List<Product>) Utils.replace(page.getContent(), entities);
		List<ProductDTO> dtos = entities.stream().map(p -> new ProductDTO(p)).toList();
		return new PageImpl<>(dtos, page.getPageable(), page.getTotalElements());
	}
}
