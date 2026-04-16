package com.example.auth_service.service.user;

import java.util.UUID;

import com.example.auth_service.model.entity.user.User;
import com.example.base_module.service.BaseService;

public interface UserService extends BaseService<User, UUID> {
	
	public User create(User entity);

	public User update(UUID id, User entity);

	public boolean delete(UUID id);
}
