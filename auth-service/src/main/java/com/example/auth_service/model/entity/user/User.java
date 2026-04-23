package com.example.auth_service.model.entity.user;

import com.example.auth_service.model.dto.user.UserDTO;
import com.example.base_module.entity.BaseEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User extends BaseEntity {

	@Column(name = "username", unique = true, nullable = false)
	private String username;

	@Column(name="password")
	private String password;
	
	@Column(name="role")
	private String role;

	@Column(name="email", unique = true)
	private String email;
	
	public User() {
		super();
	}
	
	public User (String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}
	
	public User (String username, String password, String role, String email) {
		super();
		this.username = username;
		this.password = password;
		this.role = role;
		this.email = email;
	}
	
	public UserDTO toDTO() {
		UserDTO dto = new UserDTO();
		dto.setId(getId());
		dto.setCreatedAt(getCreatedAt());
		dto.setUpdatedAt(getUpdatedAt());
		dto.setUpdateBy(getUpdateBy());
		dto.setUsername(this.username);
		dto.setPassword(this.password);
		dto.setRole(this.role);
		dto.setPassword(null);
		return dto;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
