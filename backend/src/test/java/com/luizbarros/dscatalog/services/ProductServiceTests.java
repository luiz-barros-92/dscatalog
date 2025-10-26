package com.luizbarros.dscatalog.services;

import static org.junit.jupiter.api.Assertions. *;
import static org.mockito.Mockito.times;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.luizbarros.dscatalog.repositories.ProductRepository;


@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

	@InjectMocks
	private ProductService service;
	
	@Mock
	private ProductRepository repository;
	
	private Long existingId;
	
	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		Mockito.when(repository.existsById(existingId)).thenReturn(true);		
	}
	
	@Test
	public void deleteShouldDoNothingWhenIdExists() {		
		assertDoesNotThrow(() -> {service.delete(existingId);});
		Mockito.verify(repository, times(1)).deleteById(existingId);
	}
}
