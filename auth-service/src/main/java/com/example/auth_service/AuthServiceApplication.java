package com.example.auth_service;

import java.time.ZoneId;
import java.util.TimeZone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
@ComponentScan(basePackages = {"com.example.auth_service", "com.example.base_module"})
@EnableDiscoveryClient
@EnableCaching
public class AuthServiceApplication{
	
	public static void main(String[] args) {
		SpringApplication.run(AuthServiceApplication.class, args);
	}

	@PostConstruct
	public void init() {
		TimeZone.setDefault(TimeZone.getTimeZone(ZoneId.of("Asia/Ho_Chi_Minh")));
	}
}
