package com.dexma.vendingmachine.controller;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.dexma.vendingmachine.model.Product;

/**
 * Test class for ProductControllerTest
 * 
 * @author Marco
 *
 */
public class ProductControllerTest {
	
	private static final String PRODUCTS_LIST_TEST_FILE_NAME = "products.list.test"; //List of Products with a valid formats
	private static final String PRODUCTS_LIST_TEST2_FILE_NAME = "products.list2.test"; //List of Products with invalid formats


	@Test
	public void testInitializeProducts(){
		ProductController productController = new ProductController();
		productController.initializeProducts(PRODUCTS_LIST_TEST_FILE_NAME);
		
		assertTrue(!productController.getProductsAvailable().isEmpty());
	}
	
	@Test
	public void testInitializeProductsInvalid(){
		ProductController productController = new ProductController();
		productController.initializeProducts(PRODUCTS_LIST_TEST2_FILE_NAME);
		
		assertTrue(productController.getProductsAvailable().isEmpty());
	}
	

	@Test
	public void testLookForProduct(){
		ProductController productController = new ProductController();
		productController.initializeProducts();
		
		Product product = productController.lookForProduct("Coke");
		assertNotNull(product);
	}
	


	@Test
	public void testLookForInvalidProduct(){
		ProductController productController = new ProductController();
		productController.initializeProducts();
		
		Product product = productController.lookForProduct("InvalidProduct");
		assertNull(product);
	}
	

	@Test
	public void testReset(){
		ProductController productController = new ProductController();
		productController.reset();

		assertTrue(!productController.getProductsAvailable().isEmpty());
	}

}
