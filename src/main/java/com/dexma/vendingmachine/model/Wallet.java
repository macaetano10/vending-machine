package com.dexma.vendingmachine.model;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * Class that represents a Wallet (Coins Inventory) and provide functions for it
 * 
 * @author Marco 
 *
 */
public class Wallet {

	/**
	 * Relation of Coin Types and their specific existing amount
	 */
	private Map<CoinType,Long> coinMap;
	
	{
		initializeWallet();
	}
	
	/**
	 * Initializes the wallet with an empty coinMap
	 */
	private void initializeWallet(){
		coinMap = createDefaultCoinMap();
	}

	/**
	 * Creates an empty coin map, with all the coin types available
	 * 
	 * @return coinMap
	 */
	private Map<CoinType,Long> createDefaultCoinMap() {
		Map<CoinType,Long> defaultCoinMap = new HashMap<CoinType,Long>();
		for (CoinType coinType: CoinType.values()){
			defaultCoinMap.put(coinType, 0L);
		}
		return defaultCoinMap;
	}
	
	/**
	 * Add a specific amount of coins to the wallet, considering a given coin type
	 * 
	 * @param coinType
	 * @param amountOfCoins
	 */
	public void addAmountOfCoins(CoinType coinType, Long amountOfCoins) {
		coinMap.put(coinType, coinMap.get(coinType) + amountOfCoins);
	}
	
	/**
	 * Reset/Clean the wallet
	 */
	public void cleanWallet(){
		coinMap=createDefaultCoinMap();
	}

	/**
	 * Count the total value existed in the wallet
	 * 
	 * @return totalValue
	 */
	public BigDecimal countTotalValue() {
		BigDecimal totalValue =BigDecimal.ZERO;
		
		for (CoinType coinType : coinMap.keySet()){
			BigDecimal amountForThisCoin = coinType.getValue().multiply(new BigDecimal(coinMap.get(coinType)));
			totalValue = totalValue.add(amountForThisCoin);
			
		}
		return totalValue;
	}
	
	 /**
     * Returns a boolean value that shows, given a specific value (change),
     * if there are enough coins in the wallet to be returned as change.
     * 
     * @param amountToVerify
     * 
     * @return isThereEnoughChange
     */
    public boolean isThereEnoughChange(BigDecimal amountToVerify){
         return withdrawChange(amountToVerify, false, createDefaultCoinMap()); //Check if we have enough coins before removing it from the wallet
    }
	
	 /**
     * Returns a coinMap, for the given amount of change, using the available coins in the current wallet. 
     * Then remove the coins from the current wallet.
     * 
     * @param amountOfChange
     * 
     * @return coinMap
     */
    public Map<CoinType, Long> withdrawChange(BigDecimal amountOfChange){
        Map<CoinType, Long> changeMap = createDefaultCoinMap();
        withdrawChange(amountOfChange, true, changeMap);
        return changeMap;

    }
    
    /**
     * Transfer all the coins stored in the current wallet into a Target Wallet
     * 
     * @param targetWallet
     */
    public void transferFundsToWallet(Wallet targetWallet){
    	for (CoinType transactionCoinType : CoinType.values()){
    		
    		//Copy coins from a specific coin type to target wallet
    		targetWallet.addAmountOfCoins(transactionCoinType, coinMap.get(transactionCoinType)); 
    		
    		//Remove copied coins from the current wallet
    		coinMap.put(transactionCoinType,0L); 
		}
    }


	 /**
     * Performs the logic of the change strategy: It populates the given change map with the available coins in the current wallet, 
     * related to the given amount of change.
     * Then remove the coins from the current wallet if removeCoinsFromWallet is true.
     * Then return whether these coins are enough to fulfill the given amount needed.
     * 
     * @param amountOfChange
     * @param removeCoinsFromWallet
     * @param changeMap
     * 
     * @return isThereEnoughCoins
     */
    private boolean withdrawChange(BigDecimal amountOfChange, boolean removeCoinsFromWallet, Map<CoinType, Long> changeMap) {
    	 List<CoinType> types = Arrays.asList(CoinType.values());
    	 
    	//Order the list to get the big coins first
         Collections.reverse(types); 
         BigDecimal amountLeft = amountOfChange;

         for (CoinType coinType: types){
        	 long coinCount=coinMap.get(coinType);
         	
        	 //Pick the biggest coin to be returned as change
        	 while (amountLeft.compareTo(coinType.getValue()) >= 0 && coinCount > 0) {
        		 
        		 //Subtract from the total amount left
        		 amountLeft = amountLeft.subtract(coinType.getValue());
        		 changeMap.put(coinType, changeMap.get(coinType) + 1);
        		 coinCount--;
                 
        		 if (removeCoinsFromWallet){ 
        			//Remove coin from machine wallet             
        			 addAmountOfCoins(coinType, -1L);         
                 }
        	 }
         }

         return amountLeft.compareTo(new BigDecimal(0)) == 0;
    }


	public Map<CoinType,Long> getCoinMap() {
		return coinMap;
	}

	public void setTransactionWallet(Map<CoinType,Long> transactionWallet) {
		this.coinMap = transactionWallet;
	}


}
