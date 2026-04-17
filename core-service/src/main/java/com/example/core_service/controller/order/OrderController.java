package com.example.core_service.controller.order;

import com.example.core_service.model.dto.order.OrderDTO;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import com.example.core_service.PageResponse;
import com.example.core_service.annotation.Role;
import com.example.core_service.config.Login;
import com.example.core_service.controller.ApiResponse;
import com.example.core_service.model.dto.order.OrderRequestDTO;
import com.example.core_service.model.dto.order.OrderStatusUpdateDTO;
import com.example.core_service.model.entity.order.Order;
import com.example.core_service.service.order.OrderService;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/v1/orders")
@AllArgsConstructor
public class OrderController {

	private final OrderService orderService;

	@PutMapping("/{orderId}/status")
	@Role("ADMIN")
	public ResponseEntity<ApiResponse> changeStatus(
			@PathVariable UUID orderId,
			@Valid @RequestBody OrderStatusUpdateDTO request) {
		ApiResponse response = new ApiResponse(null);

		Login login = (Login) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Order order = orderService.changeStatus(orderId, request.getStatus(), login);

		response.setData("order", order.toDTO());
		return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getCode()));
	}

	@GetMapping("/{orderId}")
	@Role("USER")
	public ResponseEntity<ApiResponse> getMyOrderById(@PathVariable UUID orderId) {
		ApiResponse response = new ApiResponse(null);

		Login login = (Login) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Order order = orderService.getMyOrderById(orderId, login.getId());

		response.setData("order", order.toFullDTO());
		return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getCode()));
	}

	@GetMapping("/me")
	@Role("USER")
	public ResponseEntity<ApiResponse> getMyOrders(
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "20") int size,
			@RequestParam(defaultValue = "createdAt") String sortField,
			@RequestParam(defaultValue = "desc") String sortOrder) {
		ApiResponse response = new ApiResponse(null);

		Login login = (Login) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		Page<Order> orders = orderService.getMyOrders(login.getId(), page - 1, size, sortField, sortOrder);
		PageResponse<OrderDTO> orderList = new PageResponse<>(orders.map(Order::toDTO));

		response.setData("orders", orderList.getContent());
		response.setData("page", orderList.getPage());
		response.setData("size", orderList.getSize());
		response.setData("totalElements", orderList.getTotalElements());
		response.setData("totalPages", orderList.getTotalPages());
		return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getCode()));
	}

	@PostMapping
	@Role("USER")
	public ResponseEntity<ApiResponse> create(
			@Valid @RequestBody OrderRequestDTO request) {
		ApiResponse response = new ApiResponse(null);

		Login login = (Login) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		Order order = orderService.placeOrder(request, login);
		response.code(201).message("Created");
		response.setData("order", order.toFullDTO());
		return new ResponseEntity<>(response, HttpStatusCode.valueOf(response.getCode()));
	}
}
