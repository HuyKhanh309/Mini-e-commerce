package com.example.core_service.model.dto.order;

import java.math.BigDecimal;
import java.util.UUID;

import com.example.base_module.dto.BaseDTO;
public class OrderItemDTO extends BaseDTO {

	private UUID productId;
	private Integer quantity;
	private BigDecimal unitPrice;

	public UUID getProductId() {
		return productId;
	}

	public void setProductId(UUID productId) {
		this.productId = productId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}
}
