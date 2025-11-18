package com.luizbarros.dscatalog.dto;

import java.time.Instant;
import java.util.List;

import com.luizbarros.dscatalog.entities.Product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ProductDTO(
	Long id,
	@Size(min = 5, max = 60, message = "name must have between 5 and 60 characters ")
	@NotBlank(message = "Name is required")
	String name,
	@NotBlank(message = "Description is required")
	String description,
	@Positive(message = "Price must be positive")
	Double price,
	String imgUrl,
	@PastOrPresent(message = "Product date must not be in future")
	Instant date,
	List<CategoryDTO> categories) {
	
	public ProductDTO(Product entity) {
		this(
			entity.getId(),
			entity.getName(),
			entity.getDescription(),
			entity.getPrice(),
			entity.getImgUrl(),
			entity.getDate(),
			entity.getCategories().stream().map(CategoryDTO::new).toList());
	}
}
