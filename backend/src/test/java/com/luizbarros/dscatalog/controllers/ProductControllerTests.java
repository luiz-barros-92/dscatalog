package com.luizbarros.dscatalog.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.luizbarros.dscatalog.dto.ProductDTO;
import com.luizbarros.dscatalog.services.ProductService;
import com.luizbarros.dscatalog.tests.Factory;

@WebMvcTest(ProductController.class)
public class ProductControllerTests {

	@Autowired
	private MockMvc mockMvc;
	
	@MockitoBean
	private ProductService service;
	
	private ProductDTO productDTO;
	private PageImpl<ProductDTO> page;
	
	@BeforeEach
	void setUp() throws Exception {
		productDTO = Factory.createProductDTO();
		page = new PageImpl<ProductDTO>(List.of(productDTO));
		
		when(service.findAll(any())).thenReturn(page);
	}
	
}
