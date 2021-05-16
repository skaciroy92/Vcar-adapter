 package com.inw.adapter;

import java.nio.ByteBuffer;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.joda.time.DateTime;

public class SimWifiInfo extends AdapterBase {
	
	private DateTime infoReadTime;
	private String simPhoneNumber;
	private String simICCIDNumber;
	private String simIMSINumber;
	private String wifiMACid;
	private String bluetoothMACid;
	private int phoneNumberLength;
	private int iccidLength;
	private int imsiLength;
	private int wifiMACidLength;
	private int bluetoothMACidLength;
	
	@Override
	public String toString() {
		String str = " infoReadTime = " + infoReadTime 
		+ " \n simPhoneNumber = " + simPhoneNumber 
		+ " \n simICCIDNumber = " + simICCIDNumber
		+ " \n simIMSINumber = " + simIMSINumber 
		+ " \n wifiMACid = " + wifiMACid
		+ " \n bluetoothMACid = " + bluetoothMACid;
	
		return str;
	}

	public String getSimPhoneNumber() {
		return simPhoneNumber;
	}

	public void setSimPhoneNumber(String hexStr) {
		byte[] verBytes;
		try {
			logger.info("simPhoneNumber HexStr {}",hexStr);
			verBytes = Hex.decodeHex(hexStr.toCharArray());
			String binString = String.format("%8s", Integer.toBinaryString(verBytes[0] & 0xFF)).replace(' ', '0')
					+ String.format("%8s", Integer.toBinaryString(verBytes[1] & 0xFF)).replace(' ', '0');
			
			Integer majorVerBin =  Integer.parseInt(binString.substring(0, 4), 2);
			Integer minorVerBin =  Integer.parseInt(binString.substring(4, 10), 2);
			Integer revVerBin =  Integer.parseInt(binString.substring(11), 2);
			this.simPhoneNumber = majorVerBin.toString() + "." + minorVerBin.toString() + "." +revVerBin.toString();
			logger.info("simPhoneNumber  {}",this.simPhoneNumber);
		} catch (DecoderException e) {
			logger.error("Erro occured : ", e );
		}	
	}		

	public String getSimIMSINumber() {
		return simIMSINumber;
	}

	public void setSimIMSINumber(String hexStr) {
		try {
			logger.info("simIMSINumber HexStr {}",hexStr);			
			byte[] strBytes = Hex.decodeHex(hexStr.toCharArray());
			this.simIMSINumber = new String(strBytes);
			logger.info("simIMSINumber  {}",this.simIMSINumber);
		} catch (DecoderException e) {
			logger.error("Erro occured : ", e );
		}	
	}

	public String getSimICCIDNumber() {
		return simICCIDNumber;
	}

	public void setSimICCIDNumber(String hexStr) {
		try {
			logger.info("simICCIDNumber HexStr {}",hexStr);
			byte[] strBytes = Hex.decodeHex(hexStr.toCharArray());
			this.simICCIDNumber = new String(strBytes);
			logger.info("simICCIDNumber  {}",this.simICCIDNumber);
			
		} catch (DecoderException e) {
			logger.error("Erro occured : ", e );
		}			
	}

	public String getWifiMACid() {
		return wifiMACid;
	}
	public void setWifiMACid(String hexStr) {
		try {
			logger.info("WifiMACid HexStr {}",hexStr);
			byte[] strBytes = Hex.decodeHex(hexStr.toCharArray());
			this.wifiMACid = new String(strBytes);
			logger.info("wifiMACid  {}",this.wifiMACid);
			
		} catch (DecoderException e) {
			logger.error("Erro occured : ", e );
		}			
	}
	
	public String getBluetoothMACid() {
		return bluetoothMACid;
	}
	public void setBluetoothMACid(String hexStr) {
		try {
			logger.info("bluetoothMACid HexStr {}",hexStr);
			byte[] strBytes = Hex.decodeHex(hexStr.toCharArray());
			this.bluetoothMACid = new String(strBytes);
			logger.info("bluetoothMACid  {}",this.bluetoothMACid);
			
		} catch (DecoderException e) {
			logger.error("Erro occured : ", e );
		}			
	}
	
	public void setInfoReadTime_str(String infoReadTime_str) {
		logger.info(" infoReadTime_str {}",infoReadTime_str);
		this.infoReadTime = this.getDateFromHexString(infoReadTime_str);
		logger.info(" infoReadTime {}",this.infoReadTime);
	}
	
	public DateTime getInfoReadTime() {
		return this.infoReadTime;
	}
		
	public void setPhoneNumberLength(String lengthHexStr) {
		try {
			phoneNumberLength = (int)ByteBuffer.wrap(Hex.decodeHex(lengthHexStr.toCharArray())).get();
			logger.info("phoneNumberLength : ", phoneNumberLength);
		} catch (DecoderException e) {
			logger.error("Exception occured : ",e);
		}
	}
	public int getPhoneNumberLength() {
		return phoneNumberLength;
	}
	
	public void setIccidLength(String lengthHexStr) {
		try {
			iccidLength = (int)ByteBuffer.wrap(Hex.decodeHex(lengthHexStr.toCharArray())).get();
			logger.info("iccidLength : ", iccidLength);
		} catch (DecoderException e) {
			logger.error("Exception occured : ",e);
		}
	}
	public int getIccidLength() {
		return iccidLength;
	}	
	public void setImsiLength(String lengthHexStr) {
		try {
			imsiLength = (int)ByteBuffer.wrap(Hex.decodeHex(lengthHexStr.toCharArray())).get();
			logger.info("ImsiLength : ", imsiLength);
		} catch (DecoderException e) {
			logger.error("Exception occured : ",e);
		}
	}
	public int getImsiLength() {
		return imsiLength;
	}
	public void setWifiMACidLength(String lengthHexStr) {
		try {
			wifiMACidLength = (int)ByteBuffer.wrap(Hex.decodeHex(lengthHexStr.toCharArray())).get();
			logger.info("wifiMACidLength : ", wifiMACidLength);
		} catch (DecoderException e) {
			logger.error("Exception occured : ",e);
		}
	}
	public int getWifiMACidLength() {
		return wifiMACidLength;
	}
	public void setBluetoothMACidLength(String lengthHexStr) {
		try {
			bluetoothMACidLength = (int)ByteBuffer.wrap(Hex.decodeHex(lengthHexStr.toCharArray())).get();
			logger.info("XXXXXX : ", bluetoothMACidLength);
		} catch (DecoderException e) {
			logger.error("Exception occured : ",e);
		}
	}
	public int getBluetoothMACidLength() {
		return bluetoothMACidLength;
	}	
}
