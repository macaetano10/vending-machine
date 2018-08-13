package com.dexma.vendingmachine.app.api;

import java.math.BigDecimal;

import com.dexma.vendingmachine.model.CoinType;
import com.dexma.vendingmachine.model.Product;

/**
 * Vending Machine App Interface
 * 
 * @author Marco
 *
 */
public interface VendingMachineApi {

	/**
	 * Method used to Insert a coin into the Vending Machine.
	 * 
	 * @param insertedCoin
	 */
	public void insertCoin(CoinType insertedCoin);

	/**
	 * Method used to Cancel the Current Operation in the Vending Machine and get the Refund.
	 * 
	 */
	public void cancelOperation();

	/**
	 * Method used to Select a specific Product after enough coins were inserted. 
	 * If the inserted coins are not enough for selected product, a null reference will be returned.
	 * If the selected product does not exist in the loaded Product List, an IllegalArgumentException is thrown.
	 * 
	 * @param productName
	 * @return selectedProduct
	 */
	public Product selectProduct(String productName);

	/**
	 * Method used to Reset the Vending Machine, usually executed by the Machine Supplier. 
	 * This Method will reset/reload the Product List and reset the Loaded Cash
	 * 
	 */
	public void resetMachine();
	
	/**
	 * Method that returns the total value inside the transaction wallet.
	 * In other words, it returns the sum of the coins already inserted for the current transaction.
	 * 
	 * @return totalValueInWallet
	 */
	public BigDecimal getTotalValueInTransactionWallet();

}