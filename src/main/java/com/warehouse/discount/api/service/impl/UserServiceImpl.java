package com.warehouse.discount.api.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.warehouse.discount.api.service.RequestService;
import com.warehouse.discount.api.service.UserService;

@Service
public class UserServiceImpl implements UserService {

	private static final String EXTERNAL_API_USER_URL = "users/";
	
	@Autowired
	private RequestService requestService;
	
	@Override
	public boolean userExists(String username) {
		return requestService.requestJsonObject(EXTERNAL_API_USER_URL + username).has("user");
	}
	
}