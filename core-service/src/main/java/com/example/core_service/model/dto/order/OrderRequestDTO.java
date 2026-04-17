package com.example.core_service.model.dto.order;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

public class OrderRequestDTO {

	@Valid
	@NotEmpty(message = "Order items must not be empty")
	private List<OrderItemRequestDTO> orderItems;

	public List<OrderItemRequestDTO> getOrderItems() {
		return orderItems;
	}

	public void setOrderItems(List<OrderItemRequestDTO> orderItems) {
		this.orderItems = orderItems;
	}
}
