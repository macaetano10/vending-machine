package com.dexma.vendingmachine.app.impl;

import com.dexma.vendingmachine.app.api.VendingMachineApi;

/**
 * This Factory Class is responsible to avoid a manual instantiation 
 * and hide the direct access to VendingMachineImpl (which has a protected Constructor). 
 * This is not a Singleton Factory class, therefore many instances 
 * of VendingMachineImpl can be created. 
 * 
 * @author Marco
 */
public class VendingMachineFactory {
	
	
	/** Static Method that creates and returns a new instance of the implementation of VendingMachineApi
	 * 
	 * @return instance of VendingMachineApiImpl
	 */
	public static VendingMachineApi getInstance(){
		return new VendingMachineImpl();
	}

}
