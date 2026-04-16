package com.example.auth_service.config;

import java.util.TreeSet;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.example.auth_service.model.enums.Roles;


@Component("roleGuard")
public class RoleGuard {
	
	public boolean check(String roleName) {
		
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		
		if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) return false;

        // Check authorities
        var authorities = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(r -> Roles.parseToCode(r.replace("ROLE_", "")))
                .collect(Collectors.toCollection(TreeSet::new)).getFirst();

        return authorities >= Roles.parseToCode(roleName);
	}
}
