package com.example.base_module.exception;

import org.springframework.http.HttpStatus;

/** 404 – resource not found (e.g. entity by id). */
public class ResourceNotFoundException extends AppException {

	public ResourceNotFoundException(String message) {
		super(HttpStatus.NOT_FOUND, message);
	}

	public ResourceNotFoundException(String resourceName, Object identifier) {
		super(HttpStatus.NOT_FOUND, resourceName + " not found: " + identifier);
	}
}
