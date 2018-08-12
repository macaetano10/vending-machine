package com.dexma.vendingmachine.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;
import java.util.logging.Logger;

import com.dexma.vendingmachine.model.Product;

/**
 * Controller class to manage logic operations with Products
 * 
 * @author Marco
 *
 */
public class ProductController {
	
	/**
	 * File name that contains the list of available products and prices for the Vending Machine.
	 * This file should be under src/main/resources
	 */
	private static final String PRODUCT_LIST_FILE_NAME = "products.list";

	private final static Logger logger = Logger.getLogger(ProductController.class.getName());
	
	private ArrayList<Product> productsAvailable = new ArrayList<Product>();

	/**
	 * Method that load Products and Prices from the Product List File into Memory
	 */
	public void initializeProducts(){
		initializeProducts(PRODUCT_LIST_FILE_NAME);
	}
	
	/**
	 * Method that load Products and Prices from the Product List File into Memory
	 * @param String productListFileName
	 */
	public void initializeProducts(String productListFileName){
		//Load file from build path
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(productListFileName);
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			
		String line = null;
		int lineCount=0;
		try {
			while((line=reader.readLine())!=null){
				if (!line.isEmpty()){
					lineCount++;
						
					String[] values=line.split(";");
					
					//Each line should be in the format: "Description;Price"
					if (values.length==2){
						try{
							BigDecimal currentProductPrice = new BigDecimal(values[1]);
							getProductsAvailable().add(new Product(values[0],currentProductPrice));
						}catch(NumberFormatException e){
							e.printStackTrace();
							logger.warning("Product list error - Product Ignored in Line "+lineCount+" - Invalid Price");
						}
					}else{
						logger.warning("Product list error - Product Ignored in Line "+lineCount+" - Invalid Line Format (e.g Coke;1.5)");
					}
				}
					
			}
		} catch (IOException e) {
			e.printStackTrace();
			logger.warning("Product list error - Can not read the file specified");

		}
	}

	/**
	 * Method that looks for a Product from the Loaded Product List given a Product Name and returns it. 
	 * If no Product is found with the given name a null reference is returned.
	 * @param String productName
	 */
	public Product lookForProduct(String productName){ 
		
		//Cool Java 8 feature :) saves time!
		Optional<Product> optional = getProductsAvailable().stream()
				.filter(p-> p.getDescription().equalsIgnoreCase((productName))).findFirst();
		
		if (optional.isPresent()){
			return optional.get();
		}else{
			logger.warning("Product "+productName+" is not available in the Vending Machine"); 
		}
		
		return null;
	}

	/**
	 * Method that reset and reload the Product List from the Product List File into Memory.
	 * @param String productName
	 */
	public void reset() {
		
		getProductsAvailable().clear();
		initializeProducts();
		
	}

	/**
	 * Method that returns the loaded Product List available
	 * @return ArrayList<Product>List of Products
	 */
	public ArrayList<Product> getProductsAvailable() {
		return productsAvailable;
	}

	
}
