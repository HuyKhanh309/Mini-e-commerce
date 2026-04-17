package com.example.core_service.controller.product;

import java.util.UUID;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.base_module.exception.ResourceNotFoundException;
import com.example.core_service.PageResponse;
import com.example.core_service.annotation.Role;
import com.example.core_service.config.Login;
import com.example.core_service.controller.ApiResponse;
import com.example.core_service.model.dto.product.ProductDTO;
import com.example.core_service.model.entity.product.Product;
import com.example.core_service.service.product.ProductService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/products")
@AllArgsConstructor
public class ProductController {

	private final ProductService productService;

	@GetMapping()
	@Role("USER")
	public ResponseEntity<ApiResponse> getList(
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "20") int size,
			@RequestParam(defaultValue = "id") String sortField,
			@RequestParam(defaultValue = "desc") String sortOrder) {
		ApiResponse resp = new ApiResponse(null);

		PageResponse<Product> productList = new PageResponse<>(
				productService.getList(page - 1, size, sortField, sortOrder, Product.class).toPage());
		resp.setData("products", productList.getContent().stream().map(Product::toDTO).toList());
		resp.setData("size", productList.getContent().size());

		return new ResponseEntity<>(resp, HttpStatusCode.valueOf(resp.getCode()));
	}

	@GetMapping("/{productId}")
	@Role("USER")
	public ResponseEntity<ApiResponse> getById(@PathVariable UUID productId) {
		ApiResponse resp = new ApiResponse(null);

		Product product = productService.getById(productId);
		if (product == null) {
			throw new ResourceNotFoundException("Product", productId);
		}

		resp.setData("Product", product.toDTO());
		return new ResponseEntity<>(resp, HttpStatusCode.valueOf(resp.getCode()));
	}

	@PostMapping()
	@Role("ADMIN")
	public ResponseEntity<ApiResponse> create(@Valid @RequestBody ProductDTO dto) {
		ApiResponse resp = new ApiResponse(null);
		Login user = (Login) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		dto.setUpdateBy(user.getUsername());

		Product product = productService.create(dto.toEntity());
		resp.setData("Product", product.toDTO());

		return new ResponseEntity<>(resp, HttpStatusCode.valueOf(resp.getCode()));
	}

	@PutMapping("/{productId}")
	@Role("ADMIN")
	public ResponseEntity<ApiResponse> update(@PathVariable UUID productId, @Valid @RequestBody ProductDTO dto) {
		ApiResponse resp = new ApiResponse(null);
		Login user = (Login) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		dto.setUpdateBy(user.getUsername());

		Product product = productService.update(productId, dto.toEntity());
		resp.setData("Product", product.toDTO());

		return new ResponseEntity<>(resp, HttpStatusCode.valueOf(resp.getCode()));
	}

	@DeleteMapping("/{productId}")
	@Role("ADMIN")
	public ResponseEntity<ApiResponse> delete(@PathVariable UUID productId) {
		ApiResponse resp = new ApiResponse(null);

		productService.delete(productId);
		resp.code(200).message("Delete product success");

		return new ResponseEntity<>(resp, HttpStatusCode.valueOf(resp.getCode()));
	}
}
