package com.example.core_service.service.order;

import java.util.UUID;

import org.springframework.data.domain.Page;

import com.example.core_service.config.Login;
import com.example.core_service.model.dto.order.OrderRequestDTO;
import com.example.core_service.model.enums.OrderStatus;
import com.example.core_service.model.entity.order.Order;

public interface OrderService {

	Order placeOrder(OrderRequestDTO request, Login user);

	Page<Order> getMyOrders(UUID userId, int page, int size, String sortField, String sortOrder);

	Order getMyOrderById(UUID orderId, UUID userId);

	Order changeStatus(UUID orderId, OrderStatus targetStatus, Login admin);
}
