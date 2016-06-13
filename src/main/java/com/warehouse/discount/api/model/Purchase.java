package com.warehouse.discount.api.model;

import java.util.List;

public class Purchase implements Comparable<Purchase> {

	private Long id;
	private String face;
	private Long price;
	private Long size;
	private List<String> recent;

	public Purchase() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFace() {
		return face;
	}

	public void setFace(String face) {
		this.face = face;
	}

	public Long getPrice() {
		return price;
	}

	public void setPrice(Long price) {
		this.price = price;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public List<String> getRecent() {
		return recent;
	}

	public void setRecent(List<String> recent) {
		this.recent = recent;
	}

	@Override
	public int compareTo(Purchase o) {
		return Integer.valueOf(this.getRecent().size()).compareTo(o.getRecent().size());
	}

}