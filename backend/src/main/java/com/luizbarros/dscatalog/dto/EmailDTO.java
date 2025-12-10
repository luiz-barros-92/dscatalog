package com.luizbarros.dscatalog.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record EmailDTO(	
	@NotBlank(message = "Email is required")
	@Email(message = "not a well-formed email address")
	String email) {	
}
