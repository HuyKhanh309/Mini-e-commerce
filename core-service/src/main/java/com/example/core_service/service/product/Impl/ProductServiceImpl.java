package com.example.core_service.service.product.Impl;

import java.util.UUID;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.base_module.cache.CachedPage;
import com.example.base_module.exception.AppException;
import com.example.base_module.exception.ResourceNotFoundException;
import com.example.base_module.repo.BaseRepository;
import com.example.base_module.service.impl.BaseServiceImpl;
import com.example.core_service.model.entity.product.Product;
import com.example.core_service.model.repository.product.ProductRepository;
import com.example.core_service.service.product.ProductService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProductServiceImpl extends BaseServiceImpl<Product, UUID> implements ProductService {

	private final ProductRepository productRepository;

	@Override
	protected BaseRepository<Product, UUID> getMapper() {
		return productRepository;
	}

	@Override
	@Transactional(readOnly = true)
	public CachedPage<Product> getList(int page, int size, String field, String order, Class<?> type) {
		return super.getList(page, size, field, order, type);
	}

	@Override
	@Transactional(readOnly = true)
	@Cacheable(cacheNames = "productById", key = "#id")
	public Product getById(UUID id) {
		return super.getById(id);
	}

	@Override
	@Transactional
	public Product create(Product entity) {
		try {
			return productRepository.save(entity);
		} catch (Exception ex) {
			throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "Error creating product", ex);
		}
	}

	@Override
	@Transactional
	@CacheEvict(cacheNames =  "productById" , key = "#id")
	public Product update(UUID id, Product entity) {
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product", id));
		try {
			entity.setId(product.getId());
			entity.setCreatedAt(product.getCreatedAt());
			entity.setUpdatedAt(product.getUpdatedAt());
			return productRepository.save(entity);
		} catch (Exception ex) {
			throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "Error updating product", ex);
		}
	}

	@Override
	@Transactional
	@CacheEvict(cacheNames =  "productById" , key = "#id")
	public boolean delete(UUID id) {
		Product product = productRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Product", id));
		try {
			productRepository.delete(product);
			return true;
		} catch (Exception ex) {
			throw new AppException(HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting product", ex);
		}
	}
}
