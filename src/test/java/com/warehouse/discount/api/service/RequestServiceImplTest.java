package com.warehouse.discount.api.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.warehouse.discount.api.service.impl.RequestServiceImpl;

public class RequestServiceImplTest {

	private static final String EXISTENT_USER_URL = "users/Tom34";
	private static final String INEXISTENT_USER_URL = "users/xxxxxxxxx";
	
	private static final String ERROR_URL = "xxx";
	
	private RequestServiceImpl requestService = new RequestServiceImpl();
	
	@Test
	public void test001() {
		assertNotNull(requestService.requestJsonObject(EXISTENT_USER_URL));
	}

	@Test
	public void test002() {
		assertNotNull(requestService.requestJsonObject(INEXISTENT_USER_URL));
	}
	
	@Test
	public void test003() {
		try {
			requestService.requestJsonObject(ERROR_URL);
		} catch (RuntimeException exception) {
			assertEquals("Error contacting external API.", exception.getMessage());
		}
	}
	
}