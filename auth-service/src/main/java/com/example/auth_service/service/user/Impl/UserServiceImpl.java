package com.example.auth_service.service.user.Impl;

import java.util.UUID;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.auth_service.model.entity.user.User;
import com.example.auth_service.model.enums.Roles;
import com.example.auth_service.model.repository.user.UserRepository;
import com.example.auth_service.service.user.UserService;
import com.example.base_module.cache.CachedPage;
import com.example.base_module.exception.AppException;
import com.example.base_module.exception.BadRequestException;
import com.example.base_module.exception.ResourceNotFoundException;
import com.example.base_module.repo.BaseRepository;
import com.example.base_module.service.impl.BaseServiceImpl;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserServiceImpl extends BaseServiceImpl<User, UUID> implements UserService {

	private final PasswordEncoder passwordEncoder;

	private final UserRepository userRepository;
	
	@Override
	protected BaseRepository<User, UUID> getMapper() {
		return userRepository;
	}

	@Override
	public CachedPage<User> getList(int page, int size, String field, String order, Class<?> type) {
		return super.getList(page, size, field, order, type);
	}

	@Override
	@Cacheable(cacheNames = "userById", key = "#id")
	public User getById(UUID id) {
		return super.getById(id);
	}

	@Override
	@Transactional
	public User create(User entity) {
		User user = userRepository.findByUsername(entity.getUsername()).orElse(null);
		if (user != null) {
			throw new BadRequestException("User already existed");
		}
		if (entity.getRole().equalsIgnoreCase(Roles.UNDEFINED.getRole())) {
			throw new BadRequestException("Role not existed");
		}
		if (entity.getRole().equalsIgnoreCase(Roles.SUPER_ADMIN.getRole())) {
			throw new BadRequestException("Invalid role");
		}
		try {
			entity.setPassword(passwordEncoder.encode(entity.getPassword()));
			return userRepository.save(entity);
		} catch (Exception ex) {
			throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating user", ex);
		}
	}

	@Override
	@Transactional
	@CacheEvict(cacheNames = "userById", key = "#id")
	public User update(UUID id, User entity) {
		User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", id));
		if (entity.getRole().equalsIgnoreCase(Roles.UNDEFINED.getRole())) {
			throw new BadRequestException("Role not existed");
		}
		if (entity.getRole().equalsIgnoreCase(Roles.SUPER_ADMIN.getRole())) {
			throw new BadRequestException("Invalid role");
		}
		try {
			entity.setPassword(user.getPassword());
			entity.setCreatedAt(user.getCreatedAt());
			return userRepository.save(entity);
		} catch (Exception ex) {
			throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating user", ex);
		}
	}

	
	@Override
	@Transactional
	@CacheEvict(cacheNames = "userById", key = "#id")
	public boolean delete(UUID id) {
		User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", id));
		if (user.getRole().equalsIgnoreCase(Roles.SUPER_ADMIN.getRole())) {
			throw new BadRequestException("Cannot delete this user");
		}
		try {
			userRepository.deleteById(id);
			return true;
		} catch (Exception ex) {
			throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting user", ex);
		}
	}
}
