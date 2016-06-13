package com.warehouse.discount.api.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.gson.JsonObject;
import com.warehouse.discount.api.service.impl.UserServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {
	
	private static final String EXTERNAL_API_USER = "users/";
	
	private static final String EMPTY_USERNAME = null;
	private static final String INEXISTENT_USERNAME = "idontexist";
	private static final String EXISTENT_USERNAME = "elvis";
	
	@Mock
	private RequestService requestService;
	
	@InjectMocks
	private UserServiceImpl userService;
	
	@After
	public void after() {
		Mockito.verifyNoMoreInteractions(requestService);
	}

	@Test
	public void testUserExists_emptyUsername() {
		Mockito.when(requestService.requestJsonObject(EXTERNAL_API_USER + EMPTY_USERNAME)).thenReturn(new JsonObject());
		
		assertFalse(userService.userExists(EMPTY_USERNAME));
		
		Mockito.verify(requestService).requestJsonObject(EXTERNAL_API_USER + EMPTY_USERNAME);
	}
	
	@Test
	public void testUserExists_inexistentUsername() {
		Mockito.when(requestService.requestJsonObject(EXTERNAL_API_USER + INEXISTENT_USERNAME)).thenReturn(new JsonObject());
		
		assertFalse(userService.userExists(INEXISTENT_USERNAME));
		
		Mockito.verify(requestService).requestJsonObject(EXTERNAL_API_USER + INEXISTENT_USERNAME);
	}
	
	@Test
	public void testUserExists_existentUsername() {
		JsonObject userJsonObject = new JsonObject();
		userJsonObject.add("user", new JsonObject());
		Mockito.when(requestService.requestJsonObject(EXTERNAL_API_USER + EXISTENT_USERNAME)).thenReturn(userJsonObject);
		
		assertTrue(userService.userExists(EXISTENT_USERNAME));
		
		Mockito.verify(requestService).requestJsonObject(EXTERNAL_API_USER + EXISTENT_USERNAME);
	}
	
}