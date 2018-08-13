package com.dexma.vendingmachine.adapter;

import java.util.Map;
import java.util.logging.Logger;

import com.dexma.vendingmachine.model.CoinType;
import com.dexma.vendingmachine.model.Product;

/**
 * This is a class that simulates the communication with the Firmware of the Vending Machine.
 * Methods of this class are called everytime the current process would have
 * an interaction with the Firmware/Hardware of the Vending Machine 
 * 
 * @author Marco
 *
 */
public class VMFirmwareAdapter {

	private final static Logger logger = Logger.getLogger(VMFirmwareAdapter.class.getName());

	/**
	 * Simulates a call to the Vending Machine Firmware to release a chosen Product to the user
	 * 
	 * @param product
	 * @return Response status
	 */
	public boolean returnProduct(Product product){
		logger.info("[Hardware Action] Returning Product: "+product.getDescription());
		//Hardware Action: Return Product
		
		return true;
	}
	
	/**
	 * Simulates a call to the Vending Machine Firmware to display a message in the Physical display
	 * 
	 * @param displayMessage
	 * @return Response status
	 */
	public boolean showDisplayMessage(String displayMessage){
		logger.info("[Hardware Action] Showing display message: "+displayMessage);		
		//Hardware Action: Show display message in the Vending Machine
		
		return true;
	}

	/**
	 * Simulates a call to the Vending Machine Firmware to release a specific amount of each specific coins to the user (refund)
	 * 
	 * @param map of Coin Type to be refunded and the respective amount for each of them
	 * @return Response status
	 */
	public boolean refundCash(Map<CoinType, Long> map) {
		logger.info("[Hardware Action] Releasing following coins to the User: "+map);
		//Hardware Action: Return Change
		
		return true;
	}
}
