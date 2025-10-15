package com.luizbarros.dscatalog.dto;

import com.luizbarros.dscatalog.entities.Category;

public record CategoryDTO(Long id, String name) {
	public CategoryDTO(Category entity) {
		this(entity.getId(), entity.getName());
	}
}
