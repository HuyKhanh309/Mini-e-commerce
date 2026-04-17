package com.example.core_service.model.repository.product;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.base_module.repo.BaseRepository;
import com.example.core_service.model.entity.product.Product;

import jakarta.persistence.LockModeType;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.QueryHints;

@Repository
public interface ProductRepository extends BaseRepository<Product, UUID> {

	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT p FROM Product p WHERE p.id IN :ids")
	@QueryHints(@QueryHint(name = "jakarta.persistence.lock.timeout", value = "3000"))
	List<Product> findAllByIdInForUpdate(@Param("ids") List<UUID> ids);
}
