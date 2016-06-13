package com.warehouse.discount.api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.warehouse.discount.api.model.Purchase;
import com.warehouse.discount.api.service.PurchaseService;
import com.warehouse.discount.api.service.UserService;

@RestController
public class PurchaseController {

	@Autowired
	private UserService userService;
	@Autowired
	private PurchaseService purchaseService;

	@Cacheable("purchases")
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/api/recent_purchases/{username:.+}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity getRecentPurchasesByUser(@PathVariable String username) {
		
		if (!(userService.userExists(username))) {
			
			return new ResponseEntity<String>("User with username of '"+username+"' was not found", 
											  HttpStatus.NOT_FOUND);
		}
		
		return new ResponseEntity<List<Purchase>>(purchaseService.getRecentPurchasesByUser(username), 
												  HttpStatus.OK);
	}
	
	@ExceptionHandler(Exception.class)
	@ResponseStatus(value=HttpStatus.INTERNAL_SERVER_ERROR)
	public Exception databaseError(Exception exception) {
		exception.printStackTrace();
		return exception;
	}

}