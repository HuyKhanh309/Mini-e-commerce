package com.example.core_service.model.repository.order;

import java.util.UUID;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;

import com.example.base_module.repo.BaseRepository;
import com.example.core_service.model.entity.order.Order;

@Repository
public interface OrderRepository extends BaseRepository<Order, UUID> {

	Page<Order> findByUserId(UUID userId, Pageable pageable);

	@EntityGraph(attributePaths = { "items", "items.product" })
	Optional<Order> findByIdAndUserId(UUID id, UUID userId);
}
