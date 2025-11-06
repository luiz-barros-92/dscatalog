package com.luizbarros.dscatalog.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.luizbarros.dscatalog.dto.ProductDTO;
import com.luizbarros.dscatalog.tests.Factory;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProductControllerIT {
	
	@Autowired
	private MockMvc mockMvc;
	
	private Long existingId;
	private Long nonExistingId;
	private Long countTotalProducts;
	private ProductDTO productDTO;
	private String expectedName;
	private String expectedDescription;
	
	@Autowired
	private ObjectMapper objectMapper;
	
	@BeforeEach
	void setUp() throws Exception {
		existingId = 1L;
		nonExistingId = 1000L;
		countTotalProducts = 25L;
		productDTO = Factory.createProductDTO();
		expectedName = productDTO.name();
		expectedDescription = productDTO.description();
	}
	
	@Test
	public void findAllShouldReturnSortedPageWhenSortByName() throws Exception{
		mockMvc.perform(get("/products?page=0&size=12&sort=name,asc")
		.accept(MediaType.APPLICATION_JSON))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.content").exists())
		.andExpect(jsonPath("$.totalElements").value(countTotalProducts))
		.andExpect(jsonPath("$.content[0].name").value("Macbook Pro"))
		.andExpect(jsonPath("$.content[1].name").value("PC Gamer"))
		.andExpect(jsonPath("$.content[2].name").value("PC Gamer Alfa"))
		.andExpect(jsonPath("$.content[3].name").value("PC Gamer Boo"))
		.andExpect(jsonPath("$.content[4].name").value("PC Gamer Card"))
		.andExpect(jsonPath("$.content[5].name").value("PC Gamer Er"));
	}
	
	@Test
	public void updateShouldReturnProductDTOWhenIdExists() throws Exception  {
		String jsonBody = objectMapper.writeValueAsString(productDTO);
		mockMvc.perform(put("/products/{id}", existingId)
			.content(jsonBody)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.id").value(existingId))
			.andExpect(jsonPath("$.name").value(expectedName))
			.andExpect(jsonPath("$.description").value(expectedDescription));
	}
	
	@Test
	public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception  {
		String jsonBody = objectMapper.writeValueAsString(productDTO);
		mockMvc.perform(put("/products/{id}", nonExistingId)
			.content(jsonBody)
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound());
	}
}
