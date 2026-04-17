package com.example.core_service.model.entity.order;

import java.math.BigDecimal;

import com.example.base_module.entity.BaseEntity;
import com.example.core_service.model.dto.order.OrderItemDTO;
import com.example.core_service.model.entity.product.Product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "order_items")
public class OrderItem extends BaseEntity {

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "order_id", nullable = false)
	private Order order;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "product_id", nullable = false)
	private Product product;

	@Column(name = "quantity", nullable = false)
	private Integer quantity;

	@Column(name = "unit_price", nullable = false, precision = 12, scale = 2)
	private BigDecimal unitPrice;

	public Order getOrder() {
		return order;
	}

	public void setOrder(Order order) {
		this.order = order;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
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

	public OrderItemDTO toDTO() {
		OrderItemDTO dto = new OrderItemDTO();
		dto.setId(getId());
		dto.setCreatedAt(getCreatedAt());
		dto.setUpdatedAt(getUpdatedAt());
		dto.setUpdateBy(getUpdateBy());
		dto.setProductId(this.product == null ? null : this.product.getId());
		dto.setQuantity(this.quantity);
		dto.setUnitPrice(this.unitPrice);
		return dto;
	}
}
