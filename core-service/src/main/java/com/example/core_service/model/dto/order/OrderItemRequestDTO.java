package com.example.core_service.model.dto.order;

import java.util.UUID;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class OrderItemRequestDTO {

	@NotNull(message = "Product id must not be null")
	private UUID id;

	@NotNull(message = "Quantity must not be null")
	@Positive(message = "Quantity must be greater than 0")
	private Integer quantity;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
}
