package com.example.base_module.service.impl;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.example.base_module.cache.CachedPage;
import com.example.base_module.entity.BaseEntity;
import com.example.base_module.repo.BaseRepository;
import com.example.base_module.service.BaseService;

public abstract class BaseServiceImpl<T extends BaseEntity, ID> implements BaseService<T, ID>{

	protected abstract BaseRepository<T , ID> getMapper();

	@Override
	public CachedPage<T> getList(int page, int size, String field, String order, Class<?> type) {
		
		if (page < 0) {
			page = 0;
		}

		if (size <= 0 || size > 100) {
			size = 20;
		}
		
		if(!T.isFieldExisted(type, field)) {
			field = "id";
		}

		if (!order.equalsIgnoreCase("desc") && !order.equalsIgnoreCase("asc")) {
			order = "asc";
		}

		Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(order), field));
		return new CachedPage<>(getMapper().findAll(pageable));
	}

	@Override
	public T getById(ID id) {
		return getMapper().findById(id).orElse(null);
	}

	
}
