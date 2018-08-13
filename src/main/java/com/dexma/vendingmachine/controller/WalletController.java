package com.dexma.vendingmachine.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Map;
import java.util.logging.Logger;

import com.dexma.vendingmachine.model.CoinType;
import com.dexma.vendingmachine.model.Wallet;

public class WalletController {
	
	private final static Logger logger = Logger.getLogger(ProductController.class.getName());
	
	/**
	 * File name that contains the list of coins in the wallet
	 * This file should be under src/main/resources
	 */
	private static final String COIN_LIST_FILE_NAME = "coins.list";

	/**
	 * Wallet (cash inventary) from the Vending Machine
	 */
	private Wallet machineWallet = new Wallet();
	
	/**
	 * Wallet (cash inventary) that represents the inserted coins (available for purchase)
	 */
	private Wallet transactionWallet = new Wallet();
	
	/**
	 * Loads the number of coins existed in the wallet from the Coins List File into Memory
	 */
	public void initializeMachineWallet(){
		initializeMachineWallet(COIN_LIST_FILE_NAME);
	}
	
	/**
	 * Loads the number of coins existed in the wallet from the Coins List File into Memory
	 * If the content had a bad format, a RuntimeException is thrown.
	 * If the file does not exist, an IllegalArgumentException is thrown. 
	 * @param coinsListFileName
	 */
	public void initializeMachineWallet(String coinsListFileName){
		//Load file from build path
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(coinsListFileName);
		if (inputStream==null){
			String message="Coins list error - File "+coinsListFileName+" does not exist.";
			logger.severe(message);
			throw new IllegalArgumentException(message);
		}
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

		//Read and load the Coin List File
		String line = null;
		int lineCount=0;
		try {
			boolean firstLine=true;
			coinFileLoop: while((line=reader.readLine())!=null){
				
				if (firstLine){ //Skip the header of the file
					firstLine=false;
					continue coinFileLoop;
				}
				if (!line.isEmpty()){
					lineCount++;

					//Each line should be in the format: "CoinType;InitialAmount"
					String[] values=line.split(";");
					if (values.length==2){
						
						CoinType coinType = null;
						try{
							coinType = CoinType.valueOf(values[0]);
						}catch(IllegalArgumentException e){
							e.printStackTrace();
							throwExceptionAndLog("Coin list error - Line "+lineCount+" - Invalid Coin Type");
						}
							
						try{
							Long amountOfCoins = Long.valueOf(values[1]);
							machineWallet.addAmountOfCoins(coinType,amountOfCoins);
						}catch(NumberFormatException e){
							e.printStackTrace();
							throwExceptionAndLog("Coin list error - Line "+lineCount+" - Invalid Long Value");
						}
					}else{
						throwExceptionAndLog("Coin list error - Line "+lineCount+" - Invalid Line Format (e.g CoinType;100)");
					}
				}
						
			}
		} catch (IOException e) {
			e.printStackTrace();
			throwExceptionAndLog("Coin List Error - Error when reading file "+coinsListFileName);
		}
	}
	
	/**
	 * Insert a coin in the transaction wallet
	 * @param insertedCoin
	 */
	public void insertCoin(CoinType insertedCoin) {
		transactionWallet.addAmountOfCoins(insertedCoin, 1L);
	}
	
	/**
	 * Helper method to log and throw exception
	 * @param message
	 */
	private void throwExceptionAndLog(String message) {
		logger.severe(message);
		throw new RuntimeException(message);
	}

	/**
	 * 
	 * @return currentr transaction wallet
	 */
	public Wallet getTransactionWallet() {
		return transactionWallet;
	}
	

	/**
	 * 
	 * @return machine wallet
	 */
	public Wallet getMachineWallet() {
		return machineWallet;
	}

	/**
	 * Cleans the current transaction wallet
	 */
	public void cleanTransactionWallet() {
		transactionWallet.cleanWallet();
	}

	/**
	 * Returns the total value of the current transaction wallet 
	 * 
	 * @return total value of the current transaction wallet 
	 */
	public BigDecimal countTotalTransactionValue() {
		return transactionWallet.countTotalValue();
	}

	/**
	 * Reset and reinitializes the machine wallet. Reset the transaction wallet.
	 */
	public void reset() {
		machineWallet.cleanWallet();
		initializeMachineWallet();
		transactionWallet.cleanWallet();
	}

	/**
	 * Refund/Withdraw change from machine wallet.
	 * IMPORTANT: The amount of change is removed from the machine wallet.
	 * 
	 * @param remainingChange
	 * @return coinMap
	 */
	public Map<CoinType, Long> refundChangeFromMachineWallet(BigDecimal remainingChange){
		return machineWallet.withdrawChange(remainingChange);
	}

	 /**
     * Returns a boolean value that shows, given a specific value (change),
     * if there are enough coins in the wallet to be returned as change.
     * 
     * @param amountToVerify
     * 
     * @return isThereEnoughChange
     */
	public boolean isThereEnoughChange(BigDecimal remainingChangeToCheck) {
		return machineWallet.isThereEnoughChange(remainingChangeToCheck);
	}

	/**
	 * Complete the Purchase, transferring the funds from the transaction wallet to the machine wallet.
	 */
	public void completePurchase() {
		transactionWallet.transferFundsToWallet(machineWallet);
	}


}
