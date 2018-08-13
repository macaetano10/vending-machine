package com.dexma.vendingmachine.app.impl;

import java.math.BigDecimal;
import java.util.Map;
import java.util.logging.Logger;

import com.dexma.vendingmachine.adapter.VMFirmwareAdapter;
import com.dexma.vendingmachine.app.api.VendingMachineApi;
import com.dexma.vendingmachine.controller.ProductController;
import com.dexma.vendingmachine.controller.WalletController;
import com.dexma.vendingmachine.model.CoinType;
import com.dexma.vendingmachine.model.Product;

/**
 * Implementation class for VendingMachineApi. 
 * This class interacts with VMFirmwareAdapter,
 * WalletController and ProductController.
 * 
 * @author Marco
 *
 */
public class VendingMachineImpl implements VendingMachineApi {
	
	private final static Logger logger = Logger.getLogger(VendingMachineImpl.class.getName());
	
	private ProductController productController;
	private WalletController walletController;
	private VMFirmwareAdapter vmHardwareAdapter;
	
	protected VendingMachineImpl(){
		super();
	}
   
	{
		initializeMachine();
	}
	
	/**
	 * Steps to initialize and configure the VendingMachineImpl instance
	 */
	private void initializeMachine(){
		vmHardwareAdapter = new VMFirmwareAdapter();
		
		productController = new ProductController();
		productController.initializeProducts();
		
		walletController = new WalletController();
		walletController.initializeMachineWallet();
	}
	
	/* (non-Javadoc)
	 * @see com.dexma.vendingmachine.app.impl.VendingMachineApi#insertCoin(com.dexma.vendingmachine.model.CoinType)
	 */
	@Override
	public void insertCoin(CoinType insertedCoin){
		walletController.insertCoin(insertedCoin);
		
		String userMessage=insertedCoin.getDescription()+" succesfully inserted. Total inserted: "+walletController.countTotalTransactionValue()+" Euros";
		logger.info(userMessage);
		vmHardwareAdapter.showDisplayMessage(userMessage);
	}
	
	/* (non-Javadoc)
	 * @see com.dexma.vendingmachine.app.impl.VendingMachineApi#cancelOperation()
	 */
	@Override
	public void cancelOperation(){
		String userMessage ="Operation Canceled - Refund: "+walletController.countTotalTransactionValue()+" Euros";
		logger.info(userMessage);
		
		vmHardwareAdapter.showDisplayMessage(userMessage);		
		
		//Refund inserted coins
		vmHardwareAdapter.refundCash(walletController.getTransactionWallet().getCoinMap());
		
		//Empty current transaction wallet (inserted coins)
		walletController.cleanTransactionWallet();
	}
	
	/* (non-Javadoc)
	 * @see com.dexma.vendingmachine.app.impl.VendingMachineApi#selectProduct(java.lang.String)
	 */
	@Override
	public Product selectProduct(String productName){
		Product selectedProduct = productController.lookForProduct(productName);
		
		if (selectedProduct!=null){
			
			//Return total value of all inserted coins
			BigDecimal totalTransactionValue = walletController.countTotalTransactionValue();
			
			//Check if there is enough coins inserted for the selected product
			if (totalTransactionValue.compareTo(selectedProduct.getPrice()) >= 0){
				
				boolean problemWithChange=false;
				boolean returnChangeToUser=false;
				
				//Before finishing the Purchase, check if there is enough change in the machine wallet change if needed
				if (totalTransactionValue.compareTo(selectedProduct.getPrice()) > 0){
					returnChangeToUser=true;
					
					//Determines the remaining change value
					BigDecimal remainingChangeToCheck = totalTransactionValue.subtract(selectedProduct.getPrice());
					
					//Checking if there is enough change in the machine wallet, if not, the purchase can not be completed
					boolean isThereEnoughChange = walletController.isThereEnoughChange(remainingChangeToCheck);
					
					if (!isThereEnoughChange){
						problemWithChange=true;
					}
				}

				if (problemWithChange){
					logger.warning("Error - User can not finish the purchase. Isuficient Coins in the machine wallet. The User will be refunded.");
					
					//Purchase can not be completed due to insufficient change in the machine. User will be refunded.
					cancelOperation();
					
				}else{
					//Allowed to Complete the Purchase
					
					//Returning the product
					String returnProductMessage="Returning Product "+selectedProduct.getDescription();
					logger.info(returnProductMessage);
					vmHardwareAdapter.showDisplayMessage(returnProductMessage);
					vmHardwareAdapter.returnProduct(selectedProduct);
					
					
					//Returning the change if it exists
					if (returnChangeToUser){
						BigDecimal remainingChange = totalTransactionValue.subtract(selectedProduct.getPrice());
						String userMessage="Returning the Remaining Change "+remainingChange;
						logger.info(userMessage);
						vmHardwareAdapter.showDisplayMessage(userMessage);
						
						//CHANGE STRATEGY - Calculating a combination of coin types and their respective amount to be returned to the user as change
						Map<CoinType,Long> coinsToBeRefunded = walletController.refundChangeFromMachineWallet(remainingChange);
						
						if (coinsToBeRefunded!=null){
							vmHardwareAdapter.refundCash(coinsToBeRefunded);
						}
					}
					
					//Transfer transaction funds to machine wallet
					walletController.completePurchase();
				}
				
			}else{
				String userMessage="Not enough money. Total loaded value: "+walletController.countTotalTransactionValue()+" Euros. Price of the Product: "+selectedProduct.getPrice();
				
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
		walletController.reset();
		
		String message="The system has been reseted";
		logger.warning(message);
		vmHardwareAdapter.showDisplayMessage(message);
	}
	
	/* (non-Javadoc)
	 * @see com.dexma.vendingmachine.app.impl.VendingMachineApi#getTotalValueInTransactionWallet()
	 */
	public BigDecimal getTotalValueInTransactionWallet(){
		return walletController.getTransactionWallet().countTotalValue();
	}
	
}
