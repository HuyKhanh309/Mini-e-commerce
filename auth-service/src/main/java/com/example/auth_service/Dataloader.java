package com.example.auth_service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.auth_service.model.entity.user.User;
import com.example.auth_service.model.enums.Roles;
import com.example.auth_service.model.repository.user.UserRepository;

@Component
public class Dataloader{

	@Autowired
    private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository userRepository;
	
	@EventListener(ApplicationReadyEvent.class)
	public void loadAfterAppReady(){
		if(userRepository.count() == 0) {
			try {
				userRepository.save(new User("Super_Admin" ,passwordEncoder.encode("khanh.lieu") ,Roles.SUPER_ADMIN.getRole(), "lieuhuykhanh@gmail.com"));
				userRepository.save(new User("Admin",passwordEncoder.encode("12345678") ,Roles.ADMIN.getRole(), "admin@example.com"));
				userRepository.save(new User("User",passwordEncoder.encode("12345678") ,Roles.USER.getRole(), "user@example.com"));	
			}
			catch (Exception ex) {
				System.err.println("Error loading data: " + ex.getMessage());
			}
		}
	}
}
