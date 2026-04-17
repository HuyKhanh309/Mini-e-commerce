package com.example.core_service.config;

import java.util.TreeSet;
import java.util.stream.Collectors;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.example.core_service.model.enums.Roles;

@Component("roleGuard")
public class RoleGuard {

    public boolean check(String roleName) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            return false;
        }

        var authorities = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(role -> Roles.parseToCode(role.replace("ROLE_", "")))
                .collect(Collectors.toCollection(TreeSet::new));

        if (authorities.isEmpty()) {
            return false;
        }

        return authorities.getLast() >= Roles.parseToCode(roleName);
    }
}
