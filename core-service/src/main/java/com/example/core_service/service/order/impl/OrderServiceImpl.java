package com.example.core_service.service.order.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import com.example.base_module.exception.BadRequestException;
import com.example.base_module.exception.ResourceNotFoundException;
import com.example.base_module.entity.BaseEntity;
import com.example.core_service.config.Login;
import com.example.core_service.model.dto.order.OrderItemRequestDTO;
import com.example.core_service.model.dto.order.OrderRequestDTO;
import com.example.core_service.model.entity.order.Order;
import com.example.core_service.model.entity.order.OrderItem;
import com.example.core_service.model.entity.product.Product;
import com.example.core_service.model.enums.OrderStatus;
import com.example.core_service.model.repository.order.OrderRepository;
import com.example.core_service.model.repository.product.ProductRepository;
import com.example.core_service.service.order.OrderService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

	private final CacheManager cacheManager;
	private final OrderRepository orderRepository;
	private final ProductRepository productRepository;
	private final TransactionTemplate transactionTemplate;

	@Override
	@Transactional(readOnly = true)
	public Page<Order> getMyOrders(UUID userId, int page, int size, String sortField, String sortOrder) {
		if (page < 0) {
			page = 0;
		}

		if (size <= 0 || size > 100) {
			size = 20;
		}

		if (!BaseEntity.isFieldExisted(Order.class, sortField)) {
			sortField = "id";
		}

		if (!sortOrder.equalsIgnoreCase("desc") && !sortOrder.equalsIgnoreCase("asc")) {
			sortOrder = "desc";
		}

		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortOrder), sortField));
		return orderRepository.findByUserId(userId, pageable);
	}

	@Override
	@Transactional(readOnly = true)
	public Order getMyOrderById(UUID orderId, UUID userId) {
		return orderRepository.findByIdAndUserId(orderId, userId)
				.orElseThrow(() -> new ResourceNotFoundException("Order", orderId));
	}

	@Override
	@Transactional
	public Order changeStatus(UUID orderId, OrderStatus targetStatus, Login admin) {
		Order order = orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Order", orderId));

		OrderStatus currentStatus = order.getStatus();
		if (currentStatus == targetStatus) {
			throw new BadRequestException("Order status is unchanged");
		}

		if (!isValidTransition(currentStatus, targetStatus)) {
			throw new BadRequestException("Invalid order status transition");
		}

		order.setStatus(targetStatus);
		order.setUpdateBy(admin.getUsername());
		return orderRepository.save(order);
	}

	@Override
	public Order placeOrder(OrderRequestDTO request, Login user) {
		Map<UUID, Integer> quantitiesByProductId = aggregateQuantities(request.getOrderItems());

		Order order = transactionTemplate.execute(status -> {
			List<Product> products = productRepository.findAllByIdInForUpdate(quantitiesByProductId.keySet().stream().toList());
			if (products.size() != quantitiesByProductId.size()) {
				throw new ResourceNotFoundException("Product");
			}

			Order newOrder = new Order();
			newOrder.setUserId(user.getId());
			newOrder.setStatus(OrderStatus.PENDING);
			newOrder.setOrderedAt(LocalDateTime.now());
			newOrder.setUpdateBy(user.getUsername());
			BigDecimal totalAmount = BigDecimal.ZERO;
			for (Product product : products) {
				Integer quantity = quantitiesByProductId.get(product.getId());
				product.decreaseStock(quantity);

				OrderItem item = new OrderItem();
				item.setProduct(product);
				item.setQuantity(quantity);
				item.setUnitPrice(product.getPrice());
				item.setUpdateBy(user.getUsername());
				newOrder.addItem(item);

				totalAmount = totalAmount.add(product.getPrice().multiply(BigDecimal.valueOf(quantity)));
			}

			newOrder.setTotalAmount(totalAmount);

			try {
				productRepository.saveAll(products);
				newOrder = orderRepository.save(newOrder);
				return newOrder;
			}
			catch (Exception e) {
				throw new BadRequestException("Order creation failed");
			}
		});

		List<UUID> productIds = quantitiesByProductId.keySet().stream().toList();
		Cache cache = cacheManager.getCache("productById");
		if (cache != null) {
			productIds.forEach(cache::evict);
		}

		return order;
	}

	private Map<UUID, Integer> aggregateQuantities(List<OrderItemRequestDTO> items) {
		Map<UUID, Integer> quantities = new LinkedHashMap<>();
		for (OrderItemRequestDTO item : items) {
			quantities.merge(item.getId(), item.getQuantity(), Integer::sum);
		}
		return quantities;
	}

	private boolean isValidTransition(OrderStatus currentStatus, OrderStatus targetStatus) {
		return switch (currentStatus) {
			case PENDING -> targetStatus == OrderStatus.CONFIRMED || targetStatus == OrderStatus.CANCELLED;
			case CONFIRMED -> targetStatus == OrderStatus.PROCESSING || targetStatus == OrderStatus.CANCELLED;
			case PROCESSING -> targetStatus == OrderStatus.SHIPPING;
			case SHIPPING -> targetStatus == OrderStatus.DELIVERED;
			case DELIVERED, CANCELLED -> false;
		};
	}
}
