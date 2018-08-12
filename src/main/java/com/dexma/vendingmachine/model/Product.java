package com.dexma.vendingmachine.model;

import java.math.BigDecimal;

/**
 * 
 * Model class that represents a Product in the Vending Machine
 * 
 * @author Marco 
 *
 */
public class Product {
	
	private String description;
	private BigDecimal price;
	
	public Product(String description) {
		this.description = description;
	}
	
	public Product(String description, BigDecimal price) {
		this.description = description;
		this.price = price.setScale(2, BigDecimal.ROUND_HALF_UP);
	}
	
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

}
