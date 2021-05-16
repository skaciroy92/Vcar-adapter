package com.inw.adapter;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AdapterBase {
	
	protected static final Logger logger = LoggerFactory.getLogger(AdapterBase.class);
	DateTimeFormatter dtf = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss");
	
	private static SecretKeySpec secretKey;
	//private static byte[] key;
	// Peter's MCU Key = 5DBAC03E22ED7BCF3A1D99CA06EDF7BBB21937AC49B271C81413A41E2F1AED51
	// Ravi's MCU KEy  = 8626C921B2FCC6DC61FCD306EBFB2D44A1D2769314A5E01603323697CBD148C7
	//private static String mcuKey = "8626C921B2FCC6DC61FCD306EBFB2D44A1D2769314A5E01603323697CBD148C7";	
	
	protected DateTime getDateFromHexString(String hexString) {
		//logger.info(" getDateFromHexString {}",hexString);
		long milliSeconds = Long.parseLong(hexString, 16);
		//logger.info(" {} in millsecond is {}",hexString, milliSeconds);
		DateTime result = new DateTime();
		result.withMillis(milliSeconds);
		//logger.info(" Date Time of {} is {}",hexString, result);
		return result;
	}
	
	protected static void setKey(byte[] myKeyBytes) {
		secretKey = new SecretKeySpec(myKeyBytes, "AES");
	}

	protected static byte[] decrypt(byte[] cipherByteArray, byte[] secret) {
		try {
			setKey(secret);
			Cipher cipher = Cipher.getInstance("AES/ECB/NOPADDING");
			cipher.init(Cipher.DECRYPT_MODE, secretKey);
			return cipher.doFinal(cipherByteArray);
		} catch (Exception e) {
			logger.error("Error while decrypting: " +  e.toString() );
		}
		return null;
	}

	protected byte[] decryptDataFrame(byte[] data, String imeiNumber) {

		String cipherKeyText = MCUKeyManager.getInstance().getMCUKeyForIMEINumber(imeiNumber);
		//logger.info("Decrypting data using Key {}.",cipherKeyText);
			
		byte[] cipherKeyBytearray = null;
		try {

			cipherKeyBytearray = Hex.decodeHex(cipherKeyText.toCharArray());
		} catch (DecoderException e) {
			// TODO Auto-generated catch block
			logger.error("Exception occured in decryption: ",e);
		}

		byte[] plainBytes = decrypt(data, cipherKeyBytearray);
		logger.info(" Decrypted bytes  using MCUKEY {} is {}", cipherKeyText, Hex.encodeHexString(plainBytes));
		return plainBytes;

	}	
	
	protected static byte[] encrypt(byte[] plainByteArray, byte[] secret) {
		logger.info("Encrypting byte[] size {}", plainByteArray.length);
		try {
			setKey(secret);
			Cipher cipher = Cipher.getInstance("AES/ECB/NOPADDING");
			cipher.init(Cipher.ENCRYPT_MODE, secretKey);
			//logger.info("Cipher text : {}",cipher.doFinal(plainByteArray));
			return cipher.doFinal(plainByteArray);
		} catch (Exception e) {
			logger.info("Error while encrypting: " + e.toString());
		}
		return null;
	}	
}
