package com.dexma.vendingmachine.model;

import java.math.BigDecimal;

/**
 * 
 * Enum for the different types of Coins accepted in the Vending Machine
 * 
 * @author Marco 
 *
 */
public enum CoinType {
	
	CENTS_5(new BigDecimal(0.05),"5 Cents"),
	CENTS_10(new BigDecimal(0.10),"10 Cents"),
	CENTS_20(new BigDecimal(0.20),"20 Cents"),
	CENTS_50(new BigDecimal(0.50),"50 Cents"),
	EUROS_1(new BigDecimal(1),"1 Euro"),
	EUROS_2(new BigDecimal(2),"2 Euros");
	
	private BigDecimal value;
	private String description;
	
	private CoinType(BigDecimal value, String description){
		this.value=value.setScale(2, BigDecimal.ROUND_HALF_UP); //scale of 2 decimals
		this.description=description;
	}

	public BigDecimal getValue() {
		return value;
	}

	public String getDescription() {
		return description;
	}

}
