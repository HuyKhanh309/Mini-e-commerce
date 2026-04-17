package com.example.core_service.exception;

import com.example.base_module.exception.BadRequestException;

public class InsufficientStockException extends BadRequestException {

	public InsufficientStockException(String message) {
		super(message);
	}
}
