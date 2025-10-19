package com.luizbarros.dscatalog.dto;

import java.time.Instant;
import java.util.List;

import com.luizbarros.dscatalog.entities.Product;

public record ProductDTO(
	Long id,
	String name,
	String description,
	Double price,
	String imgUrl,
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
