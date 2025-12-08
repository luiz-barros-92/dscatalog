package com.luizbarros.dscatalog.dto;

import com.luizbarros.dscatalog.services.validation.UserInsertValid;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@UserInsertValid
public class UserInsertDTO extends UserDTO {
	
	@NotBlank(message = "Password required")
	@Size(min = 8, message = "Password must have at least 8 characters")
	private String password;
	
	UserInsertDTO() {
		super();
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
}
