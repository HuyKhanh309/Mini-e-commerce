package com.example.base_module.exception;

import org.springframework.http.HttpStatus;

/**
 * Base exception for application errors. Handled by {@link com.example.base_module.exception.GlobalExceptionHandler}
 * and mapped to a structured error response.
 */
public class AppException extends RuntimeException {

	private final HttpStatus status;

	public AppException(String message) {
		this(HttpStatus.INTERNAL_SERVER_ERROR, message);
	}

	public AppException(HttpStatus status, String message) {
		super(message);
		this.status = status;
	}

	public AppException(HttpStatus status, String message, Throwable cause) {
		super(message, cause);
		this.status = status;
	}

	public HttpStatus getStatus() {
		return status;
	}

	public int getCode() {
		return status.value();
	}
}
