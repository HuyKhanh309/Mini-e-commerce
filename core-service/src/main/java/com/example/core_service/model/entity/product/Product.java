package com.example.core_service.model.entity.product;

import java.math.BigDecimal;

import com.example.base_module.entity.BaseEntity;
import com.example.core_service.exception.InsufficientStockException;
import com.example.core_service.model.dto.product.ProductDTO;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "products")
public class Product extends BaseEntity {

	@Column(name = "name", nullable = false)
	private String name;

	@Column(name = "description")
	private String description;

	@Column(name = "price", nullable = false, precision = 12, scale = 2)
	private BigDecimal price;

	@Column(name = "stock_quantity", nullable = false)
	private Integer stockQuantity;

	public Product() {
		super();
	}

	public ProductDTO toDTO() {
		ProductDTO dto = new ProductDTO();
		dto.setId(getId());
		dto.setCreatedAt(getCreatedAt());
		dto.setUpdatedAt(getUpdatedAt());
		dto.setUpdateBy(getUpdateBy());
		dto.setName(this.name);
		dto.setDescription(this.description);
		dto.setPrice(this.price);
		dto.setStockQuantity(this.stockQuantity);
		return dto;
	}

	public void decreaseStock(int quantity) {
		if (stockQuantity - quantity < 0) {
			throw new InsufficientStockException(
					"Insufficient stock for product:" + getName()
							+ ", availableStock:" + stockQuantity
							+ ", requiredQty:" + quantity);
		}

		stockQuantity -= quantity;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Integer getStockQuantity() {
		return stockQuantity;
	}

	public void setStockQuantity(Integer stockQuantity) {
		this.stockQuantity = stockQuantity;
	}
}
