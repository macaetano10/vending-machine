package com.dexma.vendingmachine.model;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.Map;

import org.junit.Test;

public class WalletTest {
	

	@Test
	public void testAddAmountOfCoinst(){
		Wallet wallet = new Wallet();
		wallet.addAmountOfCoins(CoinType.EUROS_1, 2L);
		
		assertTrue(wallet.getCoinMap().get(CoinType.EUROS_1) == 2L);
	}
	
	@Test
	public void testCleanWallett(){
		Wallet wallet = new Wallet();
		wallet.cleanWallet();
		
		assertNotNull(wallet.getCoinMap());
	}

	@Test
	public void testCountTotalValue(){
		Wallet wallet = new Wallet();
		wallet.addAmountOfCoins(CoinType.EUROS_1, 2L);
		
		assertTrue(wallet.countTotalValue().compareTo(new BigDecimal("2.00"))==0);
	}
	
	
	@Test
	public void testIsThereEnoughChange(){
		Wallet wallet = new Wallet();
		wallet.addAmountOfCoins(CoinType.EUROS_1, 1L);
		wallet.addAmountOfCoins(CoinType.CENTS_10, 1L);
		wallet.addAmountOfCoins(CoinType.CENTS_20, 1L);
		
		boolean isThereEnoughChange = wallet.isThereEnoughChange(new BigDecimal("1.30"));
		assertTrue(isThereEnoughChange);
		
	}
	
	@Test
	public void testWithdrawChange(){
		Wallet wallet = new Wallet();
		wallet.addAmountOfCoins(CoinType.EUROS_1, 1L);
		wallet.addAmountOfCoins(CoinType.CENTS_10, 1L);
		wallet.addAmountOfCoins(CoinType.CENTS_20, 1L);
		
		
		Map<CoinType, Long> coinMap = wallet.withdrawChange(new BigDecimal("1.30"));
		assertTrue(!coinMap.isEmpty());
		assertTrue(coinMap.get(CoinType.CENTS_10) ==1L);
		assertTrue(coinMap.get(CoinType.CENTS_20) ==1L);
		assertTrue(coinMap.get(CoinType.EUROS_1) == 1L);
		assertTrue(wallet.countTotalValue().compareTo(BigDecimal.ZERO)==0);
	}
	
	@Test
	public void testTransferFundsToWallet(){
		Wallet wallet = new Wallet();
		wallet.addAmountOfCoins(CoinType.EUROS_1, 1L);
		wallet.addAmountOfCoins(CoinType.CENTS_10, 1L);
		wallet.addAmountOfCoins(CoinType.CENTS_20, 1L);
		
		Wallet wallet2 = new Wallet();
		
		wallet.transferFundsToWallet(wallet2);
		
		assertTrue(wallet.countTotalValue().compareTo(BigDecimal.ZERO)==0);
		assertTrue(wallet2.countTotalValue().compareTo(new BigDecimal("1.30"))==0);
		
		assertTrue(wallet2.getCoinMap().get(CoinType.CENTS_10) ==1L);
		assertTrue(wallet2.getCoinMap().get(CoinType.CENTS_20) ==1L);
		assertTrue(wallet2.getCoinMap().get(CoinType.EUROS_1) == 1L);
	}
	
}
