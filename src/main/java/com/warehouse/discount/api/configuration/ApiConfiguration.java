package com.warehouse.discount.api.configuration;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@EnableCaching
@ComponentScan(basePackages = "com.warehouse.discount.api")
public class ApiConfiguration {

	@Bean(name = "cacheManager")
	public CacheManager cacheManager() {
		return new ConcurrentMapCacheManager();
	}

}