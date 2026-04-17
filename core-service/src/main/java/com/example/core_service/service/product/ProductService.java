package com.example.core_service.service.product;

import java.util.UUID;

import com.example.base_module.service.BaseService;
import com.example.core_service.model.entity.product.Product;

public interface ProductService extends BaseService<Product, UUID> {

	Product create(Product entity);

	Product update(UUID id, Product entity);

	boolean delete(UUID id);
}
