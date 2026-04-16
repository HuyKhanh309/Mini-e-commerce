package com.example.base_module.dto;

import java.beans.PropertyDescriptor;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

import org.springframework.beans.BeanUtils;
import org.springframework.util.ClassUtils;

import com.example.base_module.entity.BaseEntity;


public class BaseDTO{
	
	private UUID id;
	
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
    
    private String updateBy;
	
	public BaseDTO() {
		
	}
	
	protected <T extends BaseEntity> T toEntity(Supplier<T> supplier) {
    	return setAllPropertyValues(supplier, getAllPropertyValues());
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
	
	private <T extends BaseEntity> T setAllPropertyValues(Supplier<T> supplier, Map<String, Object> values) {
    	var obj = supplier.get();
        Class<?> clazz = ClassUtils.getUserClass(obj);
    	for (PropertyDescriptor pd : BeanUtils.getPropertyDescriptors(clazz)) {
            if (pd.getWriteMethod() == null || "class".equals(pd.getName())) {
                continue;
            }
            
            Class<?> type = pd.getPropertyType();
            if(!type.isPrimitive() && !String.class.equals(type)) {
            	continue;
            }
            
            try {
                Object inValue = values.get(pd.getName());
                pd.getWriteMethod().invoke(obj, inValue);
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException("Failed to set property: " + pd.getName(), e);
            }
        }
    	return obj;
    }
    
    private Map<String, Object> getAllPropertyValues(){
        Map<String, Object> out = new LinkedHashMap<>();
        Class<?> clazz = ClassUtils.getUserClass(this);
        for (PropertyDescriptor pd : BeanUtils.getPropertyDescriptors(clazz)) {
            var read = pd.getReadMethod();
            
            if (read == null || "class".equals(pd.getName())) {
                continue;
            }
            
            Class<?> type = pd.getPropertyType();
            if(!type.isPrimitive() && !String.class.equals(type)) {
            	continue;
            }
            
            try {
                if (!read.canAccess(this)) {
                    read.setAccessible(true);
                }
                Object value = read.invoke(this);
                out.put(pd.getName(), value);
            } catch (ReflectiveOperationException e) {
                throw new RuntimeException("Cannot read property: " + pd.getName(), e);
            }
        }
        return out;
    }
}
