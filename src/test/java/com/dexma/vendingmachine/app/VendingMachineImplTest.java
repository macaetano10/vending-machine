package com.dexma.vendingmachine.app;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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

		assertEquals(vendingMachineApp.getLoadedValue(), BigDecimal.ZERO);
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
		assertEquals(vendingMachineApp.getLoadedValue(), BigDecimal.ZERO);
	}
	
}
