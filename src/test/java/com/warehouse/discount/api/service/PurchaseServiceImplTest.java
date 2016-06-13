package com.warehouse.discount.api.service;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.warehouse.discount.api.model.Purchase;
import com.warehouse.discount.api.service.impl.PurchaseServiceImpl;

@RunWith(MockitoJUnitRunner.class)
public class PurchaseServiceImplTest {

	private static final String USERNAME = "elvis";
	private static final String ANOTHER_USERNAME = "isnotdead";
	
	private static final String PURCHASES_BY_USER_URL = "purchases/by_user/";
	private static final String PRODUCT_BY_ID_URL = "products/";
	private static final String PURCHASES_BY_PRODUCT_ID_URL = "purchases/by_product/";
	private static final String LIMIT = "?limit=5";
	
	private static final Long PRODUCT_ID = 1L;
	private static final String PRODUCT_FACE = ":)";
	private static final Long PRODUCT_PRICE = 2L;
	private static final Long PRODUCT_SIZE = 3L;
	private static final Long ANOTHER_PRODUCT_ID = 4L;

	@Mock
	private RequestService requestService;

	@InjectMocks
	private PurchaseServiceImpl purchaseService;

	@After
	public void after() {
		Mockito.verifyNoMoreInteractions(requestService);
	}

	@Test
	public void testGetRecentPurchasesByUser_withoutPurchases() throws Exception {
		JsonObject purchasesByUser = new JsonParser().parse("{'purchases' : []}").getAsJsonObject();
		Mockito.when(requestService.requestJsonObject(PURCHASES_BY_USER_URL + USERNAME + LIMIT)).thenReturn(purchasesByUser);
		
		List<Purchase> recentPurchasesByUser = purchaseService.getRecentPurchasesByUser(USERNAME);
		
		assertEquals(0, recentPurchasesByUser.size());
		
		Mockito.verify(requestService).requestJsonObject(PURCHASES_BY_USER_URL + USERNAME + LIMIT);
	}

	@Test
	public void testGetRecentPurchasesByUser_withoutRecentBuyers() {
		JsonObject purchasesByUser = new JsonParser().parse("{'purchases' : [{'productId' : '"+PRODUCT_ID+"'}]}").getAsJsonObject();
		Mockito.when(requestService.requestJsonObject(PURCHASES_BY_USER_URL + USERNAME + LIMIT)).thenReturn(purchasesByUser);
		
		JsonObject productByProductId = new JsonParser().parse("{'product' : {'face' : '"+PRODUCT_FACE+"', size : '"+PRODUCT_SIZE+"', price : '"+PRODUCT_PRICE+"'}}").getAsJsonObject();
		Mockito.when(requestService.requestJsonObject(PRODUCT_BY_ID_URL + PRODUCT_ID)).thenReturn(productByProductId);

		JsonObject purchasesByProduct = new JsonParser().parse("{'purchases' : []}").getAsJsonObject();
		Mockito.when(requestService.requestJsonObject(PURCHASES_BY_PRODUCT_ID_URL + PRODUCT_ID)).thenReturn(purchasesByProduct);
		
		
		List<Purchase> recentPurchasesByUser = purchaseService.getRecentPurchasesByUser(USERNAME);
		
		assertEquals(1, recentPurchasesByUser.size());
		Purchase purchase = recentPurchasesByUser.get(0);
		
		assertEquals(PRODUCT_FACE, purchase.getFace());
		assertEquals(PRODUCT_SIZE, purchase.getSize());
		assertEquals(PRODUCT_PRICE, purchase.getPrice());
		
		Mockito.verify(requestService).requestJsonObject(PURCHASES_BY_USER_URL + USERNAME + LIMIT);
		Mockito.verify(requestService).requestJsonObject(PRODUCT_BY_ID_URL + PRODUCT_ID);
		Mockito.verify(requestService).requestJsonObject(PURCHASES_BY_PRODUCT_ID_URL + PRODUCT_ID);
	}
	
	@Test
	public void testGetRecentPurchasesByUser_duplicateProduct() {
		JsonObject purchasesByUser = new JsonParser().parse("{'purchases' : [{'productId' : '"+PRODUCT_ID+"'},{'productId' : '"+PRODUCT_ID+"'}]}").getAsJsonObject();
		Mockito.when(requestService.requestJsonObject(PURCHASES_BY_USER_URL + USERNAME + LIMIT)).thenReturn(purchasesByUser);
		
		JsonObject productByProductId = new JsonParser().parse("{'product' : {'face' : '"+PRODUCT_FACE+"', size : '"+PRODUCT_SIZE+"', price : '"+PRODUCT_PRICE+"'}}").getAsJsonObject();
		Mockito.when(requestService.requestJsonObject(PRODUCT_BY_ID_URL + PRODUCT_ID)).thenReturn(productByProductId);

		JsonObject purchasesByProduct = new JsonParser().parse("{'purchases' : []}").getAsJsonObject();
		Mockito.when(requestService.requestJsonObject(PURCHASES_BY_PRODUCT_ID_URL + PRODUCT_ID)).thenReturn(purchasesByProduct);
		
		
		List<Purchase> recentPurchasesByUser = purchaseService.getRecentPurchasesByUser(USERNAME);
		
		assertEquals(1, recentPurchasesByUser.size());
		Purchase purchase = recentPurchasesByUser.get(0);
		
		assertEquals(PRODUCT_FACE, purchase.getFace());
		assertEquals(PRODUCT_SIZE, purchase.getSize());
		assertEquals(PRODUCT_PRICE, purchase.getPrice());
		assertEquals(0, purchase.getRecent().size());
		
		Mockito.verify(requestService).requestJsonObject(PURCHASES_BY_USER_URL + USERNAME + LIMIT);
		Mockito.verify(requestService).requestJsonObject(PRODUCT_BY_ID_URL + PRODUCT_ID);
		Mockito.verify(requestService).requestJsonObject(PURCHASES_BY_PRODUCT_ID_URL + PRODUCT_ID);
	}

	@Test
	public void testGetRecentPurchasesByUser_complete() {
		JsonObject purchasesByUser = new JsonParser().parse("{'purchases' : [{'productId' : '"+PRODUCT_ID+"'}]}").getAsJsonObject();
		Mockito.when(requestService.requestJsonObject(PURCHASES_BY_USER_URL + USERNAME + LIMIT)).thenReturn(purchasesByUser);
		
		JsonObject productByProductId = new JsonParser().parse("{'product' : {'face' : '"+PRODUCT_FACE+"', size : '"+PRODUCT_SIZE+"', price : '"+PRODUCT_PRICE+"'}}").getAsJsonObject();
		Mockito.when(requestService.requestJsonObject(PRODUCT_BY_ID_URL + PRODUCT_ID)).thenReturn(productByProductId);

		JsonObject purchasesByProduct = new JsonParser().parse("{'purchases' : [{'username' : '"+ANOTHER_USERNAME+"'}]}").getAsJsonObject();
		Mockito.when(requestService.requestJsonObject(PURCHASES_BY_PRODUCT_ID_URL + PRODUCT_ID)).thenReturn(purchasesByProduct);
		
		
		List<Purchase> recentPurchasesByUser = purchaseService.getRecentPurchasesByUser(USERNAME);
		
		assertEquals(1, recentPurchasesByUser.size());
		Purchase purchase = recentPurchasesByUser.get(0);
		
		assertEquals(PRODUCT_FACE, purchase.getFace());
		assertEquals(PRODUCT_SIZE, purchase.getSize());
		assertEquals(PRODUCT_PRICE, purchase.getPrice());
		assertEquals(1, purchase.getRecent().size());
		assertEquals(ANOTHER_USERNAME, purchase.getRecent().get(0));
		
		Mockito.verify(requestService).requestJsonObject(PURCHASES_BY_USER_URL + USERNAME + LIMIT);
		Mockito.verify(requestService).requestJsonObject(PRODUCT_BY_ID_URL + PRODUCT_ID);
		Mockito.verify(requestService).requestJsonObject(PURCHASES_BY_PRODUCT_ID_URL + PRODUCT_ID);
	}
	
	@Test
	public void testGetRecentPurchasesByUser_sorting() {
		
		// ------------ PURCHASES BY USER -------------
		JsonObject purchasesByUser = new JsonParser().parse("{'purchases' : [{'productId' : '"+ANOTHER_PRODUCT_ID+"'},{'productId' : '"+PRODUCT_ID+"'}]}").getAsJsonObject();
		Mockito.when(requestService.requestJsonObject(PURCHASES_BY_USER_URL + USERNAME + LIMIT)).thenReturn(purchasesByUser);
		
		// ------------ PRODUCTS BY PRODUCT ID -------------
		JsonObject productByProductId = new JsonParser().parse("{'product' : {'face' : '"+PRODUCT_FACE+"', size : '"+PRODUCT_SIZE+"', price : '"+PRODUCT_PRICE+"'}}").getAsJsonObject();
		JsonObject productByAnotherProductId = new JsonParser().parse("{'product' : {'face' : '"+PRODUCT_FACE+"', size : '"+PRODUCT_SIZE+"', price : '"+PRODUCT_PRICE+"'}}").getAsJsonObject();

		Mockito.when(requestService.requestJsonObject(PRODUCT_BY_ID_URL + PRODUCT_ID)).thenReturn(productByProductId);
		Mockito.when(requestService.requestJsonObject(PRODUCT_BY_ID_URL + ANOTHER_PRODUCT_ID)).thenReturn(productByAnotherProductId);

		// ------------ PURCHASES BY PRODUCT ID -------------
		JsonObject purchasesByProduct = new JsonParser().parse("{'purchases' : [{'username' : '"+ANOTHER_USERNAME+"'}]}").getAsJsonObject();
		JsonObject purchasesByAnotherProduct = new JsonParser().parse("{'purchases' : []}").getAsJsonObject();

		Mockito.when(requestService.requestJsonObject(PURCHASES_BY_PRODUCT_ID_URL + PRODUCT_ID)).thenReturn(purchasesByProduct);
		Mockito.when(requestService.requestJsonObject(PURCHASES_BY_PRODUCT_ID_URL + ANOTHER_PRODUCT_ID)).thenReturn(purchasesByAnotherProduct);
		
		
		List<Purchase> recentPurchasesByUser = purchaseService.getRecentPurchasesByUser(USERNAME);
		
		assertEquals(2, recentPurchasesByUser.size());
		Purchase purchase = recentPurchasesByUser.get(0);
		Purchase anotherPurchase = recentPurchasesByUser.get(1);
		
		assertEquals(PRODUCT_FACE, purchase.getFace());
		assertEquals(PRODUCT_SIZE, purchase.getSize());
		assertEquals(PRODUCT_PRICE, purchase.getPrice());
		assertEquals(1, purchase.getRecent().size());
		assertEquals(ANOTHER_USERNAME, purchase.getRecent().get(0));

		assertEquals(0, anotherPurchase.getRecent().size());
		
		Mockito.verify(requestService).requestJsonObject(PURCHASES_BY_USER_URL + USERNAME + LIMIT);
		
		Mockito.verify(requestService).requestJsonObject(PRODUCT_BY_ID_URL + PRODUCT_ID);
		Mockito.verify(requestService).requestJsonObject(PURCHASES_BY_PRODUCT_ID_URL + PRODUCT_ID);
		
		Mockito.verify(requestService).requestJsonObject(PRODUCT_BY_ID_URL + ANOTHER_PRODUCT_ID);
		Mockito.verify(requestService).requestJsonObject(PURCHASES_BY_PRODUCT_ID_URL + ANOTHER_PRODUCT_ID);
	}
	
}