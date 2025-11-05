package com.luizbarros.dscatalog.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import com.luizbarros.dscatalog.dto.ProductDTO;
import com.luizbarros.dscatalog.repositories.ProductRepository;
import com.luizbarros.dscatalog.services.exceptions.ResourceNotFoundException;

@SpringBootTest
@Transactional
public class ProductServiceIT {

	@Autowired
	private ProductService service;
	
	@Autowired
	private ProductRepository repository;
	
	private Long existingId;
	private Long nonExistingId;
	private Long countTotalProducts;
	
	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 1000L;
		countTotalProducts = 25L;		
	}
	
	@Test
	public void deleteShouldDeleteResourceWHenIdExists() {
		service.delete(existingId);
		assertEquals(countTotalProducts - 1, repository.count());
	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		assertThrows(ResourceNotFoundException.class, () -> service.findById(nonExistingId));
	}
	
	@Test
	public void findAllPagedShouldReturnPageWithCorrectPaginationDetails() {
		PageRequest pageRequest = PageRequest.of(0, 10);
		Page<ProductDTO> result = service.findAll(pageRequest);
		assertFalse(result.isEmpty());
		assertEquals(0, result.getNumber());
		assertEquals(10, result.getSize());
		assertEquals(countTotalProducts, result.getTotalElements());
	}
	
	@Test
	public void findAllPagedShouldReturnEmptyPageWhenPageDoesNotExist() {
		PageRequest pageRequest = PageRequest.of(50, 10);
		Page<ProductDTO> result = service.findAll(pageRequest);
		assertTrue(result.isEmpty());
	}
	
	@Test
	public void findAllPagedShouldReturnSortedPageWhenSortedByName() {
		PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("name"));
		Page<ProductDTO> result = service.findAll(pageRequest);
		assertFalse(result.isEmpty());
		assertEquals("Macbook Pro", result.getContent().get(0).name());
		assertEquals("PC Gamer", result.getContent().get(1).name());
		assertEquals("PC Gamer Alfa", result.getContent().get(2).name());
		assertEquals("PC Gamer Boo", result.getContent().get(3).name());
		assertEquals("PC Gamer Card", result.getContent().get(4).name());
		assertEquals("PC Gamer Er", result.getContent().get(5).name());
	}
}
