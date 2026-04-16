package com.example.auth_service.model.enums;

import java.util.stream.Stream;

import org.springframework.security.core.GrantedAuthority;

public enum Roles implements GrantedAuthority {
	USER(1000, "USER"), 
	ADMIN(2000, "ADMIN"), 
	SUPER_ADMIN(3000, "SUPER_ADMIN"),

	UNDEFINED(0, "UNDEFINED");

	private final int code;

	private final String role;

	Roles(int code, String role) {
		this.code = code;
		this.role = role;
	}

	public int getCode() {
		return code;
	}

	public String getRole() {
		return role;
	}

	public static int parseToCode(String role) {
		return Stream.of(Roles.values())
				.filter(v -> v.getRole().equalsIgnoreCase(role)).findFirst().orElse(UNDEFINED)
				.getCode();
	}

	@Override
	public String getAuthority() {
		return this.name();
	}

}
