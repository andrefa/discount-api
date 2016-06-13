package com.warehouse.discount.api.service;

import java.util.List;

import com.warehouse.discount.api.model.Purchase;

public interface PurchaseService {

	List<Purchase> getRecentPurchasesByUser(String username);

}