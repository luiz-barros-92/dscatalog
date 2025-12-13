package com.luizbarros.dscatalog.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NewPasswordDTO(
	@NotBlank(message = "Password required")
	String token,
	
	@NotBlank(message = "Password required")
	@Size(min = 8, message = "Password must have at least 8 characters")
	String password) {
}
