package com.dexma.vendingmachine.adapter;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.dexma.vendingmachine.model.CoinType;
import com.dexma.vendingmachine.model.Product;

/**
 * Test class for VMHardwareAdapter
 * 
 * @author Marco
 *
 */
public class VMHardwareAdapterTest {
	
	@Test
	public void testReturnProduct(){
		VMFirmwareAdapter vmHardwareAdapter = new VMFirmwareAdapter();
		boolean result=vmHardwareAdapter.returnProduct(new Product("Coke",new BigDecimal("1.50")));
		
		assertTrue(result);
	}

	@Test
	public void testShowDisplayMessage(){
		VMFirmwareAdapter vmHardwareAdapter = new VMFirmwareAdapter();
		boolean result=vmHardwareAdapter.showDisplayMessage("Test display message");
		
		assertTrue(result);
	}

	@Test
	public void testRefundCash(){
		VMFirmwareAdapter vmHardwareAdapter = new VMFirmwareAdapter();
		Map<CoinType,Long> coinMap = new HashMap<CoinType,Long>();

		coinMap.put(CoinType.CENTS_5,0L);
		coinMap.put(CoinType.CENTS_10,0L);
		coinMap.put(CoinType.CENTS_20,0L);
		coinMap.put(CoinType.CENTS_50,3L);
		coinMap.put(CoinType.EUROS_1,1L);
		coinMap.put(CoinType.EUROS_2,0L);
		
		boolean result=vmHardwareAdapter.refundCash(coinMap);
		
		assertTrue(result);
	}

}
