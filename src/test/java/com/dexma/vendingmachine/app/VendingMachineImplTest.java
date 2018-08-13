package com.dexma.vendingmachine.app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;

import org.junit.Test;

import com.dexma.vendingmachine.app.api.VendingMachineApi;
import com.dexma.vendingmachine.app.impl.VendingMachineFactory;
import com.dexma.vendingmachine.model.CoinType;
import com.dexma.vendingmachine.model.Product;

/**
 * Test class for VendingMachineImpl
 * 
 * @author Marco
 *
 */
public class VendingMachineImplTest {
	
	@Test
	public void testCancelOperation(){
		VendingMachineApi vendingMachineApp = VendingMachineFactory.getInstance();
		vendingMachineApp.insertCoin(CoinType.CENTS_5);
		vendingMachineApp.cancelOperation();

		//I could not use assertEquals method because it does not work if the scales are different
		assertTrue(vendingMachineApp.getTotalValueInTransactionWallet().compareTo(BigDecimal.ZERO)==0);
	}
	
	@Test
	public void testSelectProductNotNull(){
		VendingMachineApi vendingMachineApp = VendingMachineFactory.getInstance();
		vendingMachineApp.insertCoin(CoinType.EUROS_1);
		vendingMachineApp.insertCoin(CoinType.CENTS_50);
		Product product = vendingMachineApp.selectProduct("Coke");
		assertNotNull(product);
	}

	@Test
	public void testSelectProductValue(){
		VendingMachineApi vendingMachineApp = VendingMachineFactory.getInstance();
		vendingMachineApp.insertCoin(CoinType.EUROS_1);
		vendingMachineApp.insertCoin(CoinType.CENTS_50);
		Product product = vendingMachineApp.selectProduct("Coke");
		assertEquals(product.getDescription(),"Coke");
	}
	
	@Test
	public void testSelectProductWithChange(){
		VendingMachineApi vendingMachineApp = VendingMachineFactory.getInstance();
		vendingMachineApp.insertCoin(CoinType.EUROS_1);
		vendingMachineApp.insertCoin(CoinType.CENTS_50);
		vendingMachineApp.insertCoin(CoinType.CENTS_50);
		Product product = vendingMachineApp.selectProduct("Coke");
		assertEquals(product.getDescription(),"Coke");
	}

	@Test
	public void testSelectProductWithoutEnoughMoney(){
		VendingMachineApi vendingMachineApp = VendingMachineFactory.getInstance();
		vendingMachineApp.insertCoin(CoinType.CENTS_5);
		Product product = vendingMachineApp.selectProduct("Coke");
		assertNull(product);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testSelecInvalidProduct(){
		VendingMachineApi vendingMachineApp = VendingMachineFactory.getInstance();
		vendingMachineApp.insertCoin(CoinType.CENTS_5);
		vendingMachineApp.selectProduct("InvalidProduct");
	}
	
	@Test
	public void testResetMachine(){
		VendingMachineApi vendingMachineApp = VendingMachineFactory.getInstance();
		vendingMachineApp.insertCoin(CoinType.CENTS_5);
		vendingMachineApp.resetMachine();

		//I could not use assertEquals method because it does not work if the scales are different
		assertTrue(vendingMachineApp.getTotalValueInTransactionWallet().compareTo(BigDecimal.ZERO)==0);	
	}
	
}
