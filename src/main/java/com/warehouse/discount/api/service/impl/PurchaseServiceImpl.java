
package com.warehouse.discount.api.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.warehouse.discount.api.model.Purchase;
import com.warehouse.discount.api.service.PurchaseService;
import com.warehouse.discount.api.service.RequestService;

@Service
public class PurchaseServiceImpl implements PurchaseService {

	private static final String EXTERNAL_API_PURCHASES_BY_USER_URL = "purchases/by_user/";
	private static final String EXTERNAL_API_PRODUCT_BY_ID_URL = "products/";
	private static final String EXTERNAL_API_PURCHASES_BY_PRODUCT_ID_URL = "purchases/by_product/";
	private static final String LIMIT = "?limit=5";
	
	@Autowired
	private RequestService requestService;
	
	@Override
	public List<Purchase> getRecentPurchasesByUser(String username) {
		Map<Long, Purchase> mappedPurchases = new HashMap<>();
		
		JsonObject purchasesByUserJsonObj = requestService.requestJsonObject(EXTERNAL_API_PURCHASES_BY_USER_URL + username + LIMIT);
		JsonArray purchasesByUserJsonArray = purchasesByUserJsonObj.get("purchases").getAsJsonArray();
		
		processPurchasesByUsername(mappedPurchases, purchasesByUserJsonArray);
		
		return extractSortedPurchases(mappedPurchases);
	}

	private void processPurchasesByUsername(Map<Long, Purchase> mappedPurchases, JsonArray purchasesByUserJsonArray) {
		for (JsonElement purchaseByUserElmn : purchasesByUserJsonArray) {
			
			JsonObject purchaseJsonObj = purchaseByUserElmn.getAsJsonObject();
			Long productId = purchaseJsonObj.get("productId").getAsLong();

			if (!mappedPurchases.containsKey(productId)) {
				mappedPurchases.put(productId, processIndividualPurchase(productId));
			}
		}
	}

	private Purchase processIndividualPurchase(Long productId) {
		JsonObject productJsonObj = requestService.requestJsonObject(EXTERNAL_API_PRODUCT_BY_ID_URL + productId).get("product").getAsJsonObject();

		Purchase purchase = new Purchase();
		purchase.setId(productId);
		purchase.setFace(productJsonObj.get("face").getAsString());
		purchase.setPrice(productJsonObj.get("price").getAsLong());
		purchase.setSize(productJsonObj.get("size").getAsLong());
		purchase.setRecent(fetchRecentBuyers(productId));
		
		return purchase;
	}

	private List<String> fetchRecentBuyers(Long productId) {
		List<String> buyers = new ArrayList<>();
		
		JsonObject purchasesByProductJsonObj = requestService.requestJsonObject(EXTERNAL_API_PURCHASES_BY_PRODUCT_ID_URL + productId);
		JsonArray purchasesByProductJsonArray = purchasesByProductJsonObj.get("purchases").getAsJsonArray();
		
		for (JsonElement purchaseByProductElmn : purchasesByProductJsonArray) {
			JsonObject purchaseByProductJsonObj = purchaseByProductElmn.getAsJsonObject();
			buyers.add(purchaseByProductJsonObj.get("username").getAsString());
		}
		
		return buyers;
	}
	
	private ArrayList<Purchase> extractSortedPurchases(Map<Long, Purchase> mappedPurchases) {
		ArrayList<Purchase> purchases = new ArrayList<>(mappedPurchases.values());
		Collections.sort(purchases);
		Collections.reverse(purchases);
		return purchases;
	}

}