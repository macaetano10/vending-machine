package com.dexma.vendingmachine.controller;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Map;

import org.junit.Test;

import com.dexma.vendingmachine.model.CoinType;

/**
 * Test class for WalletControllerTest
 * 
 * @author Marco
 *
 */
public class WalletControllerTest {

	private static final String COIN_LIST_TEST_FILE_NAME = "coins.list.test"; //List of Coins with valid format
	private static final String PRODUCTS_LIST_TEST2_FILE_NAME = "coins.list2.test"; //List of Coins with invalid formats

	@Test
	public void testInitializeWallet(){
		WalletController walletController = new WalletController();
		walletController.initializeMachineWallet(COIN_LIST_TEST_FILE_NAME);
		
		assertNotNull(walletController.getTransactionWallet().getCoinMap());
	}
	

	@Test(expected = IllegalArgumentException.class)
	public void testInitializeWalletInvalidFile(){
		WalletController walletController = new WalletController();
		walletController.initializeMachineWallet("InvalidFileName");
		
	}
	

	@Test(expected = RuntimeException.class)
	public void testInitializeWalletInvalidFormat(){
		WalletController walletController = new WalletController();
		walletController.initializeMachineWallet(PRODUCTS_LIST_TEST2_FILE_NAME);
		
	}
	
	@Test
	public void testReset(){
		WalletController walletController = new WalletController();
		walletController.reset(); 

		assertNotNull(walletController.getTransactionWallet());
	}
	
	@Test
	public void testInsertVoin(){
		WalletController walletController = new WalletController();
		walletController.insertCoin(CoinType.CENTS_10); 

		assertTrue(walletController.getTransactionWallet().countTotalValue().compareTo(CoinType.CENTS_10.getValue())==0);
		
	}
	
	@Test
	public void testCleanTransactionWallet(){
		WalletController walletController = new WalletController();
		walletController.insertCoin(CoinType.CENTS_10); 
		walletController.cleanTransactionWallet();

		assertTrue(walletController.getTransactionWallet().countTotalValue().compareTo(BigDecimal.ZERO)==0);
		
	}
	
	@Test
	public void testCountTotalTransactionValue(){
		WalletController walletController = new WalletController();
		walletController.insertCoin(CoinType.CENTS_10); 

		assertTrue(walletController.countTotalTransactionValue().compareTo(CoinType.CENTS_10.getValue())==0);
		
	}
	
	@Test
	public void testIsThereEnoughChange(){
		WalletController walletController = new WalletController();
		walletController.initializeMachineWallet(COIN_LIST_TEST_FILE_NAME);
		
		boolean isThereEnoughChange = walletController.isThereEnoughChange(new BigDecimal("1.30"));
		assertTrue(isThereEnoughChange);
		
	}
	
	@Test
	public void testRefundChangeFromMachineWalletNotEmpty(){
		WalletController walletController = new WalletController();
		walletController.initializeMachineWallet(COIN_LIST_TEST_FILE_NAME);
		
		Map<CoinType, Long> coinMap = walletController.refundChangeFromMachineWallet(new BigDecimal("1.30"));
		assertTrue(!coinMap.isEmpty());
		
	}
	
	@Test
	public void testRefundChangeFromMachineWalletValue(){
		WalletController walletController = new WalletController();
		walletController.initializeMachineWallet(COIN_LIST_TEST_FILE_NAME);
				
		Map<CoinType, Long> coinMap = walletController.refundChangeFromMachineWallet(new BigDecimal("1.30"));
		assertTrue(coinMap.get(CoinType.CENTS_10) ==1L);
		assertTrue(coinMap.get(CoinType.CENTS_20) ==1L);
		assertTrue(coinMap.get(CoinType.EUROS_1) == 1L);
		
		assertTrue(coinMap.get(CoinType.CENTS_5) == 0L);
		assertTrue(coinMap.get(CoinType.CENTS_50) == 0L);
		assertTrue(coinMap.get(CoinType.EUROS_2) == 0L);
		
	}

	@Test
	public void testCompletePurchase(){
		WalletController walletController = new WalletController();
		walletController.insertCoin(CoinType.CENTS_5); 
		walletController.insertCoin(CoinType.CENTS_5); 
		
		walletController.completePurchase();
		
		assertTrue(walletController.getMachineWallet().getCoinMap().get(CoinType.CENTS_5) == 2L);
		
	}

}
