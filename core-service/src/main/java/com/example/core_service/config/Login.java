package com.example.core_service.config;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class Login implements UserDetails {

	private static final long serialVersionUID = 1L;

	private final UUID id;
	private final String username;
	private final Collection<? extends GrantedAuthority> authorities;

	public Login(UUID id, String username, Collection<? extends GrantedAuthority> authorities) {
		this.id = id;
		this.username = username;
		this.authorities = authorities == null ? List.of() : authorities;
	}

	public UUID getId() {
		return id;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public String getPassword() {
		return null;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
}
