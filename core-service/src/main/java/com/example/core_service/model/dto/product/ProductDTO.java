package com.example.core_service.model.dto.product;

import java.math.BigDecimal;

import com.example.base_module.dto.BaseDTO;
import com.example.core_service.model.entity.product.Product;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public class ProductDTO extends BaseDTO {

	@NotBlank(message = "Product name must not be empty")
	private String name;

	private String description;

	@NotNull(message = "Price must not be null")
	@DecimalMin(value = "0.0", inclusive = true, message = "Price must be greater than or equal to 0")
	private BigDecimal price;

	@NotNull(message = "Stock quantity must not be null")
	@PositiveOrZero(message = "Stock quantity must be greater than or equal to 0")
	private Integer stockQuantity;

	public ProductDTO() {
		super();
	}

	public ProductDTO(Product product) {
		super();
		product.setId(getId());
		product.setCreatedAt(getCreatedAt());
		product.setUpdatedAt(getUpdatedAt());
		product.setUpdateBy(getUpdateBy());
		product.setName(this.name);
		product.setDescription(this.description);
		product.setPrice(this.price);
		product.setStockQuantity(this.stockQuantity);
	}

	public Product toEntity() {
		Product product = new Product();
		product.setId(getId());
		product.setUpdateBy(getUpdateBy());
		product.setName(this.name);
		product.setDescription(this.description);
		product.setPrice(this.price);
		product.setStockQuantity(this.stockQuantity);
		return product;
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
