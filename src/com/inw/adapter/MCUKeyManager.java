package com.inw.adapter;

import java.util.Hashtable;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import com.thingworx.sdk.simplething.ThingWorxClientForOBD;

public class MCUKeyManager extends AdapterBase {
	
	// String cipherText = data.substring(48, data.length() - 4);
	// String cipherText =
	// "C8F5181DB2F907EDA2124D85EEBA3EF893FEDF5B3D2F71D1D9E0678DD28144330FACFB252322707CEAA494B7DC67AE59F289DAA5FEBF9D27D499158C4B4E10A164F3CA9F2D7D266CE4DB0F4CB06B6884";
	// String cipherKeyText =
	// "E1B7A18B0913F78EC0898ED6282DD8D315003F6BFDA4696E15003F6BFDA4696E";
	//String cipherKeyText = "5DBAC03E22ED7BCF3A1D99CA06EDF7BBB21937AC49B271C81413A41E2F1AED51";	
	// Test code to convert IMEI number received over TCP bytes into its digit form.
	
	//Ravis IMEI = "383631363332303430323335313530"
	// Peters IMEI = "383631363332303430323334373332"
	//	String strIMEI = "383631363332303430323334373332";
	//	
	//	try {
	//		logger.info("IMEI = ");
	//		byte[] imeiBytes = Hex.decodeHex(strIMEI);
	//		for(int j = 0; j < imeiBytes.length; j++) {
	//			logger.info((char)imeiBytes[j]);
	//		}
	//	} catch (DecoderException e1) {
	//		// TODO Auto-generated catch block
	//		logger.error("Exception occured: ",e1);
	//	}	
	
    // static variable single_instance of type Singleton 
    private static MCUKeyManager single_instance = null; 
  
    Hashtable<String, String> mcuKeys = new Hashtable<String, String>();
  
    // private constructor restricted to this class itself 
    private MCUKeyManager() 
    { 
    	//mcuKeys.put("861632040235150", "8626C921B2FCC6DC61FCD306EBFB2D44A1D2769314A5E01603323697CBD148C7"); //Ravi's
    	//mcuKeys.put("861632040234732", "5DBAC03E22ED7BCF3A1D99CA06EDF7BBB21937AC49B271C81413A41E2F1AED51"); //Peter's
    } 
  
    // static method to create instance of Singleton class 
    public static MCUKeyManager getInstance() 
    { 
        if (single_instance == null) 
            single_instance = new MCUKeyManager(); 
  
        return single_instance; 
    }
    
    public String getMCUKeyForIMEINumber(String imeiNumber) {
    	
    	String imeiKey = imeiNumber;
    	if(imeiNumber.length() == 30) { //If the imei number (17 char long) comes in Hex encode format, do conversion.
			byte[] imeiBytes =  null;
			try { 
				imeiBytes = Hex.decodeHex(imeiNumber);
			} catch (DecoderException e) {
				logger.error(e.toString());
			}    		
    		imeiKey = new String(imeiBytes);
    	}
    	//logger.info("Finding MCU Key for IMEI number : {}", imeiKey);
    	String mcuKey = mcuKeys.get(imeiKey);
    	if(mcuKey == null || mcuKey.isEmpty()) {
        	logger.info("MCU Key for IMEI {} doesnt exist in local cache, getting from TWX server...", imeiKey);
    		try {
				mcuKey = ThingWorxClientForOBD.getInstance().getMCUKeyFromServer(imeiKey);
			} catch (Exception e) {
				logger.error("Error occured :", e);
			}
    		mcuKeys.put(imeiKey,mcuKey);
    		logger.info("Received MCU Key from server {}", mcuKey);
    	}else {
    		logger.info("For IMEI {} ,Using Cached MCUKey {}",imeiKey, mcuKey);
    	}
    	
    	return mcuKey;
    }
}
