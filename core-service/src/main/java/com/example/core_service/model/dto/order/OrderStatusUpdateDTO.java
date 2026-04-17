package com.example.core_service.model.dto.order;

import jakarta.validation.constraints.NotNull;

import com.example.core_service.model.enums.OrderStatus;

public class OrderStatusUpdateDTO {

	@NotNull
	private OrderStatus status;

	public OrderStatus getStatus() {
		return status;
	}

	public void setStatus(OrderStatus status) {
		this.status = status;
	}
}
