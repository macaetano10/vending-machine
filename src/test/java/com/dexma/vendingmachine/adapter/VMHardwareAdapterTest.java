package com.dexma.vendingmachine.adapter;

import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Test;

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
		boolean result=vmHardwareAdapter.refundCash(new BigDecimal("1.50"));
		
		assertTrue(result);
	}

}
