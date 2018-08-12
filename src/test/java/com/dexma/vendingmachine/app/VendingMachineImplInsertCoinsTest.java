package com.dexma.vendingmachine.app;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import com.dexma.vendingmachine.app.api.VendingMachineApi;
import com.dexma.vendingmachine.app.impl.VendingMachineFactory;
import com.dexma.vendingmachine.model.CoinType;

/**
 *  Test Class for the method VendingMachineImpl#InsertCoins
 *  
 *  This class is responsible to perform one execution of the testTotalInsertCoins method
 *  for each of the different values of the Array defined below.
 *  
 *  This parameterized class is a way to avoid the duplicated code when we need
 *  to create one method (testTotalInsertCoins) for each of the coinTypes to be tested
 *  
 * @author Marco
 */
@RunWith(Parameterized.class)
public class VendingMachineImplInsertCoinsTest {
	
	 @Parameters
	 public static Collection<Object[]> data() {
		 return Arrays.asList(new Object[][] {
			 //Format is: {{input1,expected1},{input2,expected2,...}}
			 { CoinType.CENTS_5, CoinType.CENTS_5}, { CoinType.CENTS_10, CoinType.CENTS_10 }, { CoinType.CENTS_20, CoinType.CENTS_20 }, 
			 { CoinType.CENTS_50, CoinType.CENTS_50}, { CoinType.EUROS_1, CoinType.EUROS_1 }, { CoinType.EUROS_2, CoinType.EUROS_2 }
		 });
	 }

	 private CoinType input;
	 private CoinType expected;
	    
	 //This constructor must be provided for the parameterized tests to work.
	 public VendingMachineImplInsertCoinsTest(CoinType input, CoinType expected) {
		 this.input = input;
		 this.expected = expected;	
	 }
	    
	 @Test
	 public void testTotalInsertCoins() {
		 VendingMachineApi vendingMachineApp = VendingMachineFactory.getInstance();
		 vendingMachineApp.insertCoin(input);
		 assertEquals(vendingMachineApp.getLoadedValue(), expected.getValue());
		 
	 }
}