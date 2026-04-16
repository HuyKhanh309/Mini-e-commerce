package com.example.auth_service.config;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;

import com.example.auth_service.annotation.Role;


@Aspect
@Component
public class RoleAspect {
	
	private final RoleGuard roleGuard;
	
    protected RoleAspect(RoleGuard roleGuard) {
    	this.roleGuard = roleGuard;
    }

    @Around("@annotation(role)")
    public Object check(ProceedingJoinPoint pjp, Role role) throws Throwable {
        if (!roleGuard.check(role.value())) {
            throw new AccessDeniedException("Forbidden");
        }
        return pjp.proceed();
    }
}
