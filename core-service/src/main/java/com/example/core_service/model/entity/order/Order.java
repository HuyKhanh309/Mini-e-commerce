package com.example.core_service.model.entity.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.example.base_module.entity.BaseEntity;
import com.example.core_service.model.dto.order.OrderDTO;
import com.example.core_service.model.enums.OrderStatus;

import jakarta.persistence.*;

@Entity
@Table(name = "orders")
public class Order extends BaseEntity {

	@Column(name = "user_id", nullable = false)
	private UUID userId;

	@Column(name = "status", nullable = false)
	private int status;

	@Column(name = "total_amount", nullable = false, precision = 12, scale = 2)
	private BigDecimal totalAmount;

	@Column(name = "ordered_at", nullable = false)
	private LocalDateTime orderedAt;

	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY )
	private List<OrderItem> items = new ArrayList<>();

	public void addItem(OrderItem item) {
		items.add(item);
		item.setOrder(this);
	}

	public OrderDTO toDTO() {
		OrderDTO dto = new OrderDTO();
		dto.setId(getId());
		dto.setCreatedAt(getCreatedAt());
		dto.setUpdatedAt(getUpdatedAt());
		dto.setUpdateBy(getUpdateBy());
		dto.setUserId(this.userId);
		dto.setStatus(getStatus());
		dto.setTotalAmount(this.totalAmount);
		dto.setOrderedAt(this.orderedAt);
		return dto;
	}

	public OrderDTO toFullDTO() {
		OrderDTO dto = this.toDTO();
		dto.setOrderItems(items == null ? List.of() : items.stream().map(OrderItem::toDTO).toList());
		return dto;
	}

	public UUID getUserId() {
		return userId;
	}

	public void setUserId(UUID userId) {
		this.userId = userId;
	}

	public OrderStatus getStatus() {
		return OrderStatus.fromCode(status);
	}

	public void setStatus(OrderStatus status) {
		this.status = status.getCode();
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public LocalDateTime getOrderedAt() {
		return orderedAt;
	}

	public void setOrderedAt(LocalDateTime orderedAt) {
		this.orderedAt = orderedAt;
	}

	public List<OrderItem> getItems() {
		return items;
	}

	public void setItems(List<OrderItem> items) {
		this.items = items;
	}
}
