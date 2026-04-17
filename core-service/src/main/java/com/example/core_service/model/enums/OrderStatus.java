package com.example.core_service.model.enums;

import java.util.Arrays;

public enum OrderStatus {
	PENDING(0),
	CONFIRMED(1000),
	PROCESSING(2000),
	SHIPPING(3000),
	DELIVERED(4000),
	CANCELLED(999);

	private final int code;

	OrderStatus(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public static OrderStatus fromCode(Integer code) {
		if (code == null) {
			return null;
		}

		return Arrays.stream(values())
				.filter(status -> status.code == code)
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Unknown order status code: " + code));
	}
}
