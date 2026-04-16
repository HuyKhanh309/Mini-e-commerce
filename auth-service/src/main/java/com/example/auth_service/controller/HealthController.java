package com.example.auth_service.controller;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/health")
public class HealthController {
	
	@GetMapping()
	public ResponseEntity<ApiResponse> getHealth() {
		ApiResponse resp = new ApiResponse(null);
		resp.code(200).message("Server is running");
		
		return new ResponseEntity<>(resp, HttpStatusCode.valueOf(resp.getCode()));
	}
}
