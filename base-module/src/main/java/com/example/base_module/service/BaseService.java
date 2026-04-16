package com.example.base_module.service;

import com.example.base_module.cache.CachedPage;

public interface BaseService<T,ID> {
	
	CachedPage<T> getList(int page, int size, String field, String order, Class<?> type);

	T getById(ID id);	
}
