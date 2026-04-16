package com.example.base_module.entity;

import java.beans.PropertyDescriptor;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ClassUtils;

import com.example.base_module.dto.BaseDTO;

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
    
    protected <T extends BaseDTO> T toDTO(Supplier<T> supplier) {
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
	
    private <T extends BaseDTO> T setAllPropertyValues(Supplier<T> supplier, Map<String, Object> values) {
    	var obj = supplier.get();
        Class<?> clazz = ClassUtils.getUserClass(obj);
    	for (PropertyDescriptor pd : BeanUtils.getPropertyDescriptors(clazz)) {
            if (pd.getWriteMethod() == null || "class".equals(pd.getName())) {
                continue;
            }
            
            Class<?> type = pd.getPropertyType();
            if(!type.isPrimitive() && !excludedClasses(type)) {
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
            if(!type.isPrimitive() && !excludedClasses(type)) {
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
    
    private boolean excludedClasses(Class<?> type) {
    	return String.class.equals(type) || Integer.class.equals(type)
    			|| Long.class.equals(type) || LocalDateTime.class.equals(type)
    			|| UUID.class.equals(type);
    }
}
