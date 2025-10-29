package com.luizbarros.dscatalog.services;

import static org.junit.jupiter.api.Assertions. *;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.luizbarros.dscatalog.dto.ProductDTO;
import com.luizbarros.dscatalog.entities.Product;
import com.luizbarros.dscatalog.repositories.ProductRepository;
import com.luizbarros.dscatalog.services.exceptions.DatabaseException;
import com.luizbarros.dscatalog.services.exceptions.ResourceNotFoundException;
import com.luizbarros.dscatalog.tests.Factory;


@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

	@InjectMocks
	private ProductService service;
	
	@Mock
	private ProductRepository repository;
	
	private Long existingId;
	private Long nonExistingId;
	private Long dependentId;
	private PageImpl<Product> page;
	private Product product;
	
	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 1000L;
		dependentId = 3L;
		product = Factory.createProduct();
		page = new PageImpl<>(List.of(product));
		
		when(repository.findAll((Pageable)ArgumentMatchers.any())).thenReturn(page);
		
		when(repository.save(ArgumentMatchers.any())).thenReturn(product);
		
		when(repository.findById(existingId)).thenReturn(Optional.of(product));
		when(repository.findById(nonExistingId)).thenReturn(Optional.empty());
		
		when(repository.existsById(existingId)).thenReturn(true);
		when(repository.existsById(nonExistingId)).thenReturn(false);
		when(repository.existsById(dependentId)).thenReturn(true);
		
		doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExistingId);
		doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
	}
	
	@Test
	public void findByIdShouldReturnProductDTOWhenIdExists() {
		ProductDTO dto = service.findById(existingId);
		assertNotNull(dto);
		assertEquals(existingId, dto.id());
		verify(repository).findById(existingId);
	}
	
	@Test
	public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		assertThrows(ResourceNotFoundException.class, () -> service.findById(nonExistingId));
		verify(repository).findById(nonExistingId);
	}
	
	@Test
	public void findAllPagedShouldReturnPage() {
		Pageable pageable = PageRequest.of(0, 10);
		Page<ProductDTO> result = service.findAll(pageable);
		assertNotNull(result);
		verify(repository).findAll(pageable);
	}
	
	@Test
	public void deleteShouldThrowDatabaseExceptionWhenDependentId() {
		assertThrows(DatabaseException.class, () -> service.delete(dependentId));
	}
	
	@Test
	public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {
		assertThrows(ResourceNotFoundException.class, () -> service.delete(nonExistingId));
	}
	
	@Test
	public void deleteShouldDoNothingWhenIdExists() {		
		assertDoesNotThrow(() -> service.delete(existingId));
		verify(repository, times(1)).deleteById(existingId);
	}
}
