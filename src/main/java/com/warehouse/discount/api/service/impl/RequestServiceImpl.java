package com.warehouse.discount.api.service.impl;

import java.io.IOException;

import org.apache.http.client.fluent.Request;
import org.springframework.stereotype.Service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.warehouse.discount.api.service.RequestService;

@Service
public class RequestServiceImpl implements RequestService {

	private static final String BASE_API_URL = "http://74.50.59.155:6000/api/";

	public JsonObject requestJsonObject(String url)  {
		return requestJsonElement(url).getAsJsonObject();
	}
	
	private JsonElement requestJsonElement(String url) {
		try {
			String getResponse = Request.Get(BASE_API_URL + url).execute().returnContent().asString();
			return new JsonParser().parse(getResponse);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Error contacting external API.", e);
		}
	}

}