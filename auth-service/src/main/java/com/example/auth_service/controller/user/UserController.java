package com.example.auth_service.controller.user;

import java.util.UUID;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.auth_service.PageResponse;
import com.example.auth_service.annotation.Role;
import com.example.auth_service.controller.ApiResponse;
import com.example.auth_service.model.dto.user.UserDTO;
import com.example.auth_service.model.entity.user.User;
import com.example.auth_service.service.user.UserService;
import com.example.base_module.exception.ResourceNotFoundException;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@AllArgsConstructor
public class UserController {

	private final UserService userService;

	@Role("ADMIN")
	@GetMapping()
	public ResponseEntity<ApiResponse> getList(
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "20") int size, 
			@RequestParam(defaultValue = "id") String sortField,
			@RequestParam(defaultValue = "desc") String sortOrder) {
		ApiResponse resp = new ApiResponse(null);

		PageResponse<User> userList = new PageResponse<>(userService.getList(page-1, size, sortField, sortOrder, User.class).toPage());
		resp.setData("Users", userList.getContent().stream().map(u -> u.toDTO()).toList());

		return new ResponseEntity<>(resp, HttpStatusCode.valueOf(resp.getCode()));
	}

	@Role("ADMIN")
	@GetMapping("/{userId}")
	public ResponseEntity<ApiResponse> getById(@PathVariable UUID userId) {
		ApiResponse resp = new ApiResponse(null);

		User user = userService.getById(userId);
		if (user == null) {
			throw new ResourceNotFoundException("User", userId);
		}
		resp.setData("User", user.toDTO());
		return new ResponseEntity<>(resp, HttpStatusCode.valueOf(resp.getCode()));
	}
	
	@Role("SUPER_ADMIN")
	@PostMapping()
	public ResponseEntity<ApiResponse> create(@Valid @RequestBody UserDTO dto) {
		ApiResponse resp = new ApiResponse(null);

		User user = userService.create(dto.toEntity());
		
		resp.setData("User", user.toDTO());
		return new ResponseEntity<>(resp, HttpStatusCode.valueOf(resp.getCode()));
	}
	
	@Role("SUPER_ADMIN")
	@PutMapping("/{userId}")
	public ResponseEntity<ApiResponse> update(@PathVariable UUID userId, @Valid @RequestBody UserDTO dto) {
		ApiResponse resp = new ApiResponse(null);

		User user = userService.update(userId, dto.toEntity());
		
		resp.setData("User", user.toDTO());
		return new ResponseEntity<>(resp, HttpStatusCode.valueOf(resp.getCode()));
	}
	
	@Role("SUPER_ADMIN")
	@DeleteMapping("/{userId}")
	public ResponseEntity<ApiResponse> delete(@PathVariable UUID userId) {
		ApiResponse resp = new ApiResponse(null);

		userService.delete(userId);

		resp.code(200).message("Delete user success");
		return new ResponseEntity<>(resp, HttpStatusCode.valueOf(resp.getCode()));
	}

}
