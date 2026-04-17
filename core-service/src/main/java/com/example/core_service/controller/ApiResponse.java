package com.example.core_service.controller;

import java.util.HashMap;

public class ApiResponse extends HashMap<String, Object> {

	private static final long serialVersionUID = 8026025038549239996L;

	private ApiResponse() {
		put("code", 200);
		put("message", "OK");
	}

	public ApiResponse(String requestId) {
		this();
		put("requestId", requestId);
	}

	public ApiResponse code(int code) {
		put("code", code);
		return this;
	}

	public int getCode() {
		return (int) get("code");
	}

	public ApiResponse message(String message) {
		put("message", message);
		return this;
	}

	public ApiResponse setData(String key, Object data) {
		put(key, data);
		return this;
	}
}
