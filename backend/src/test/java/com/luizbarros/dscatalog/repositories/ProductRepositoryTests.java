package com.luizbarros.dscatalog.repositories;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.luizbarros.dscatalog.entities.Product;
import com.luizbarros.dscatalog.tests.Factory;

@DataJpaTest
public class ProductRepositoryTests {
	
	@Autowired
	private ProductRepository repository;
	
	private Long existingId;
	private Product product;
	private Long countTotalProducts;

	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		product = Factory.createProduct();
		countTotalProducts = 25L;
	}
	
	@Test
	public void saveShouldPersistWithAutoincrementWhenIdIsNull() {
		product.setId(null);
		product = repository.save(product);
		assertNotNull(product.getId());
		assertEquals(countTotalProducts + 1, product.getId());
	}
	
	@Test
	public void deleteShouldDeleteObjectWhenIdExists() {
		repository.deleteById(existingId);		
		Optional<Product> result = repository.findById(existingId);
		assertFalse(result.isPresent());
	}
}
