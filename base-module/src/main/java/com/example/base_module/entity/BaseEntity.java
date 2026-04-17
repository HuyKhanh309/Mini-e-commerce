package com.example.base_module.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass 
public class BaseEntity{
	
	@Id
	@GeneratedValue(strategy= GenerationType.UUID)
	private UUID id;
	
	@CreationTimestamp
	@Column(name="createdAt")
	private LocalDateTime createdAt;

	@UpdateTimestamp
	@Column(name="updatedAt")
	private LocalDateTime updatedAt;
	
	@Column(name="updateBy")
	private String updateBy;
	
	public BaseEntity() {
		this.updateBy = "System";
	}
    
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public static boolean isFieldExisted(Class<?> clazz,String fieldName) {
	    while (clazz != null) {
	        try {
	            clazz.getDeclaredField(fieldName);
	            return true;
	        } catch (NoSuchFieldException ex) {
	            clazz = clazz.getSuperclass();
	        }
	    }
	    return false;
	}
}
