package com.example.auth_service;

import java.util.List;

import org.springframework.data.domain.Page;

public class PageResponse<T> {
	
	List<T> content;
	
	int page;
	
	int size;
	
	long totalElements;
	
	int totalPages;
    
	public PageResponse() {
		super();
	}
	
	public PageResponse(Page<T> page) {
		super();
		this.content = page.getContent();
		this.page = page.getNumber() + 1;
		this.size = page.getSize();
		this.totalElements = page.getTotalElements();
		this.totalPages = page.getTotalPages();
	}

	public List<T> getContent() {
		return content;
	}

	public void setContent(List<T> content) {
		this.content = content;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public long getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(long totalElements) {
		this.totalElements = totalElements;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}
}
