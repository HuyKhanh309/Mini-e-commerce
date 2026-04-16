package com.example.base_module.datasource;

/**
 * Loại datasource cho routing: PRIMARY (master, write) hoặc SLAVE (replica, read).
 */
public enum DataSourceType {
	PRIMARY,
	SLAVE
}
