package com.example.auth_service.model.dto.user;

import com.example.auth_service.model.entity.user.User;
import com.example.base_module.dto.BaseDTO;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotBlank;

public class UserDTO extends BaseDTO{
	
	@NotBlank(message = "Username not empty")
	private String username;
	
	@NotBlank(message = "Password not empty")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;
	
	@NotBlank(message = "Role not empty")
	private String role;
	
	public UserDTO() {
		super();
	}
	
	public UserDTO(String username, String password, String role) {
		super();
		this.username = username;
		this.password = password;
		this.role = role;
	}

	public User toEntity() {
		User user = new User();
		user.setId(getId());
		user.setCreatedAt(getCreatedAt());
		user.setUpdatedAt(getUpdatedAt());
		user.setUpdateBy(getUpdateBy());
		user.setUsername(this.username);
		user.setPassword(this.password);
		user.setRole(this.role);
		return user;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}
