package com.example.base_module.exception;

import java.time.Instant;
import java.util.Map;

/**
 * Structured error body returned by {@link GlobalExceptionHandler} (code, message, path, timestamp, optional validation errors).
 */
public class ErrorResponse {

	private final int code;
	private final String message;
	private final String path;
	private final Instant timestamp;
	private final Map<String, String> errors;

	public ErrorResponse(int code, String message, String path) {
		this(code, message, path, null);
	}

	public ErrorResponse(int code, String message, String path, Map<String, String> errors) {
		this.code = code;
		this.message = message;
		this.path = path;
		this.timestamp = Instant.now();
		this.errors = errors;
	}

	public int getCode() {
		return code;
	}

	public String getMessage() {
		return message;
	}

	public String getPath() {
		return path;
	}

	public Instant getTimestamp() {
		return timestamp;
	}

	public Map<String, String> getErrors() {
		return errors;
	}
}
