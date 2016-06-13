package com.warehouse.discount.api.controller;

import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.List;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.warehouse.discount.api.model.Purchase;
import com.warehouse.discount.api.service.PurchaseService;
import com.warehouse.discount.api.service.UserService;

@RunWith(MockitoJUnitRunner.class)
public class PurchaseControllerTest {
	
	private static final String EMPTY_USERNAME = null;
	private static final String EMPTY_REQUEST_RESPONSE = "User with username of 'null' was not found";
	
	private static final String INEXISTENT_USERNAME = "idontexist";
	private static final String INEXISTENT_REQUEST_RESPONSE = "User with username of 'idontexist' was not found";
	
	private static final String EXISTENT_USERNAME = "elvis";

	@Mock
	private UserService userService;
	@Mock
	private PurchaseService purchaseService;
	
	@InjectMocks
	private PurchaseController purchaseController;
	
	@After
	public void afterEach() {
		Mockito.verifyNoMoreInteractions(userService, purchaseService);
	}
	
	@Test
	@SuppressWarnings("rawtypes")
	public void testGetRecentPurchasesByUser_emptyUsername() {
		Mockito.when(userService.userExists(EMPTY_USERNAME)).thenReturn(false);
		
		ResponseEntity recentPurchasesByUser = purchaseController.getRecentPurchasesByUser(EMPTY_USERNAME);
		
		assertEquals(EMPTY_REQUEST_RESPONSE, recentPurchasesByUser.getBody());
		assertEquals(HttpStatus.NOT_FOUND, recentPurchasesByUser.getStatusCode());
		
		Mockito.verify(userService).userExists(EMPTY_USERNAME);
	}
	
	@Test
	@SuppressWarnings("rawtypes")
	public void testGetRecentPurchasesByUser_inexistentUsername() {
		Mockito.when(userService.userExists(INEXISTENT_USERNAME)).thenReturn(false);
		
		ResponseEntity recentPurchasesByUser = purchaseController.getRecentPurchasesByUser(INEXISTENT_USERNAME);
		
		assertEquals(INEXISTENT_REQUEST_RESPONSE, recentPurchasesByUser.getBody());
		assertEquals(HttpStatus.NOT_FOUND, recentPurchasesByUser.getStatusCode());
		
		Mockito.verify(userService).userExists(INEXISTENT_USERNAME);
	}
	
	@Test
	@SuppressWarnings("rawtypes")
	public void testGetRecentPurchasesByUser_existentUsername() {
		List<Purchase> purchases = Collections.singletonList(new Purchase());
		
		Mockito.when(userService.userExists(EXISTENT_USERNAME)).thenReturn(true);
		Mockito.when(purchaseService.getRecentPurchasesByUser(EXISTENT_USERNAME)).thenReturn(purchases);
		
		ResponseEntity recentPurchasesByUser = purchaseController.getRecentPurchasesByUser(EXISTENT_USERNAME);
		
		assertEquals(purchases, recentPurchasesByUser.getBody());
		assertEquals(HttpStatus.OK, recentPurchasesByUser.getStatusCode());
		
		Mockito.verify(userService).userExists(EXISTENT_USERNAME);
		Mockito.verify(purchaseService).getRecentPurchasesByUser(EXISTENT_USERNAME);
	}
	
}