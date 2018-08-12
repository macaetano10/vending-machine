package com.dexma.vendingmachine.app.impl;

import java.math.BigDecimal;
import java.util.logging.Logger;

import com.dexma.vendingmachine.adapter.VMFirmwareAdapter;
import com.dexma.vendingmachine.app.api.VendingMachineApi;
import com.dexma.vendingmachine.controller.ProductController;
import com.dexma.vendingmachine.model.CoinType;
import com.dexma.vendingmachine.model.Product;

/**
 * Implementation class of VendingMachineApi. 
 * It contains the logic for the LoadedValue and interacts 
 * with VMFirmwareAdapter and ProductController.
 * 
 * @author Marco
 *
 */
public class VendingMachineImpl implements VendingMachineApi {
	
	private final static Logger logger = Logger.getLogger(VendingMachineImpl.class.getName());
	
	private ProductController productController;
	private VMFirmwareAdapter vmHardwareAdapter;
	
	protected VendingMachineImpl(){
		super();
	}
   
	private BigDecimal loadedValue=BigDecimal.ZERO;

	{
		initializeMachine();
	}
	
	/**
	 * Steps to initialize and configure the VendingMachineImpl instance
	 */
	private void initializeMachine(){
		productController = new ProductController();
		productController.initializeProducts();
		vmHardwareAdapter = new VMFirmwareAdapter();
	}
	
	/* (non-Javadoc)
	 * @see com.dexma.vendingmachine.app.impl.VendingMachineApi#insertCoin(com.dexma.vendingmachine.model.CoinType)
	 */
	@Override
	public void insertCoin(CoinType insertedCoin){
		this.loadedValue = this.loadedValue.add(insertedCoin.getValue());
		String userMessage=insertedCoin.getDescription()+" succesfully inserted. Total inserted: "+loadedValue+" Euros";
		
		logger.info(userMessage);
		vmHardwareAdapter.showDisplayMessage(userMessage);
	}
	
	/* (non-Javadoc)
	 * @see com.dexma.vendingmachine.app.impl.VendingMachineApi#cancelOperation()
	 */
	@Override
	public void cancelOperation(){
		String userMessage ="Operation Canceled by the User - Refund: "+loadedValue+" Euros";
		
		logger.info(userMessage);
		vmHardwareAdapter.showDisplayMessage(userMessage);
		vmHardwareAdapter.refundCash(loadedValue);
		
		//Reset loaded money
		loadedValue = BigDecimal.ZERO;
	}
	
	/* (non-Javadoc)
	 * @see com.dexma.vendingmachine.app.impl.VendingMachineApi#selectProduct(java.lang.String)
	 */
	@Override
	public Product selectProduct(String productName){
		Product selectedProduct = productController.lookForProduct(productName);
		
		if (selectedProduct!=null){
			
			//Check if there is enough money loaded in the machine for the selected product
			if (loadedValue.compareTo(selectedProduct.getPrice()) >= 0){
				String returnProductMessage="Returning Product "+selectedProduct.getDescription();
				logger.info(returnProductMessage);
				vmHardwareAdapter.showDisplayMessage(returnProductMessage);
				vmHardwareAdapter.returnProduct(selectedProduct);
				
				//Check if there is any remaining change to be returned
				if (loadedValue.compareTo(selectedProduct.getPrice()) > 0){
					BigDecimal remainingChange = loadedValue.subtract(selectedProduct.getPrice());
					String userMessage="Returning the Remaining Change "+remainingChange;
					
					logger.info(userMessage);
					vmHardwareAdapter.showDisplayMessage(userMessage);
					vmHardwareAdapter.refundCash(remainingChange);
				}

				loadedValue=BigDecimal.ZERO;
			}else{
				String userMessage="Not enough money. Total loaded value: "+loadedValue+" Euros. Price of the Product: "+selectedProduct.getPrice();
				
				logger.info(userMessage);
				vmHardwareAdapter.showDisplayMessage(userMessage);
				
				return null;
			}
			
			
		}else{
			//Product does not exist
			String message="Given product name does not exist";
			logger.warning(message);
			throw new IllegalArgumentException(message);
		}
		
		return selectedProduct;
	}
	
	/* (non-Javadoc)
	 * @see com.dexma.vendingmachine.app.impl.VendingMachineApi#resetMachine()
	 */
	@Override
	public void resetMachine(){
		productController.reset();
		
		loadedValue=BigDecimal.ZERO;
		
		String message="The system has been reseted";
		logger.warning(message);
		vmHardwareAdapter.showDisplayMessage(message);
	}

	/* (non-Javadoc)
	 * @see com.dexma.vendingmachine.app.impl.VendingMachineApi#getLoadedValue()
	 */
	@Override
	public BigDecimal getLoadedValue() {
		return loadedValue;
	}
	
}
