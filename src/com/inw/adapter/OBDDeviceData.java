 package com.inw.adapter;

import java.util.Arrays;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

public class OBDDeviceData extends AdapterBase {
	
	private String protocolVersion;
	private String hardWareVersion;
	private String softWareVersionMCU;
	private String softWareVersionModem;
	
	@Override
	public String toString() {
		String str = " \n protocolVersion = " + protocolVersion 
		+ " \n hardWareVersion = " + hardWareVersion
		+ " \n softWareVersionMCU = " + softWareVersionMCU 
		+ " \n softWareVersionModem = " + softWareVersionModem;
	
		return str;
	}

	public String getProtocolVersion() {
		return protocolVersion;
	}

	public void setProtocolVersion(String hexStr) {
		byte[] verBytes;
		try {
			logger.info("Protocol Version HexStr {}",hexStr);
			verBytes = Hex.decodeHex(hexStr);
			String binString = String.format("%8s", Integer.toBinaryString(verBytes[0] & 0xFF)).replace(' ', '0')
					+ String.format("%8s", Integer.toBinaryString(verBytes[1] & 0xFF)).replace(' ', '0');
			
			Integer majorVerBin =  Integer.parseInt(binString.substring(0, 4), 2);
			Integer minorVerBin =  Integer.parseInt(binString.substring(4, 10), 2);
			Integer revVerBin =  Integer.parseInt(binString.substring(11), 2);
			this.protocolVersion = majorVerBin.toString() + "." + minorVerBin.toString() + "." +revVerBin.toString();
			logger.info("Protocol Version  {}",this.protocolVersion);
		} catch (DecoderException e) {
			logger.error("Erro occured : ", e );
		}	
	}		

	public String getHardWareVersion() {
		return hardWareVersion;
	}

	public void setHardWareVersion(String hexStr) {
		byte[] verBytes;
		try {
			logger.info("Hardware Version HexStr {}",hexStr);
			verBytes = Hex.decodeHex(hexStr);
			String binString = String.format("%8s", Integer.toBinaryString(verBytes[0] & 0xFF)).replace(' ', '0')
					+ String.format("%8s", Integer.toBinaryString(verBytes[1] & 0xFF)).replace(' ', '0');
			
			Integer majorVerBin =  Integer.parseInt(binString.substring(0, 4), 2);
			Integer minorVerBin =  Integer.parseInt(binString.substring(4, 10), 2);
			Integer revVerBin =  Integer.parseInt(binString.substring(11), 2);
			this.hardWareVersion = majorVerBin.toString() + "." + minorVerBin.toString() + "." +revVerBin.toString();
			logger.info("Hardware Version  {}",this.hardWareVersion);
		} catch (DecoderException e) {
			logger.error("Erro occured : ", e );
		}	
	}

	public String getSoftWareVersionMCU() {
		return softWareVersionMCU;
	}

	public void setSoftWareVersions(String hexStr) {
		byte[] verBytes;
		try {
			logger.info("setSoftWareVersionMCU HexStr {}",hexStr);
			verBytes = Hex.decodeHex(hexStr);
			int mculen = verBytes[0]&0x1F;
			this.softWareVersionMCU = new String(Arrays.copyOfRange(verBytes, 1, mculen+1) );
			int modLen = verBytes[mculen+1] & 0x1F;
			this.softWareVersionModem = new String(Arrays.copyOfRange(verBytes, mculen+2, mculen+ 2 + modLen));

			logger.info("setSoftWareVersionMCU  {}",this.softWareVersionMCU);
			logger.info("softWareVersionModem  {}",this.softWareVersionModem);
			
		} catch (DecoderException e) {
			logger.error("Erro occured : ", e );
		}			
	}

	public String getSoftWareVersionModem() {
		return softWareVersionModem;
	}

}
