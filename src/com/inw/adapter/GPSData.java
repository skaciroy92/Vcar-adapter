package com.inw.adapter;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import com.thingworx.types.primitives.structs.Location;

public class GPSData extends AdapterBase {

	private Location latLong;
	private Integer headingAngle;
	public GPSData(String hexString) {
		super();
		latLong = extractGPSLatLongFromHexStr(hexString);
		headingAngle = extractGPSHeadingDirectionFromHexStr(hexString);
	}
	
	public Location getLatLong() {
		return latLong;
	}

	public Integer getHeadingAngle() {
		return headingAngle;
	}
	
	private Location extractGPSLatLongFromHexStr(String hexStr) {
		Location result =new Location();
		try {
			logger.info("extractGPSLatLongFromHexStr hexStr {}",hexStr);
			{
				String longHexStr = hexStr.substring(12, 20);
				//logger.info("LONGITUDE HEX STR:{} ", longHexStr);
				byte[] LongitudeBytes = Hex.decodeHex(longHexStr.toCharArray());
//				String longiBin1 = String.format("%8s", Integer.toBinaryString(LongitudeBytes[0] & 0xFF)).replace(' ', '0')
//								+ String.format("%8s", Integer.toBinaryString(LongitudeBytes[1] & 0xFF)).replace(' ', '0')
//								+ String.format("%8s", Integer.toBinaryString(LongitudeBytes[2] & 0xFF)).replace(' ', '0')
//								+ String.format("%8s", Integer.toBinaryString(LongitudeBytes[3] & 0xFF)).replace(' ', '0');
				//logger.info("LONG BYTES BEFORE MASKING:{} ", longiBin1);
				LongitudeBytes[0] = (byte)( LongitudeBytes[0] & 1);
				String longiBin2 = String.format("%8s", Integer.toBinaryString(LongitudeBytes[0] & 0xFF)).replace(' ', '0')
						+ String.format("%8s", Integer.toBinaryString(LongitudeBytes[1] & 0xFF)).replace(' ', '0')
						+ String.format("%8s", Integer.toBinaryString(LongitudeBytes[2] & 0xFF)).replace(' ', '0')
						+ String.format("%8s", Integer.toBinaryString(LongitudeBytes[3] & 0xFF)).replace(' ', '0');		 	
				//logger.info("LONG BYTES AFTER MASKING:{} ", longiBin2);
				Integer longiInt = Integer.parseInt(longiBin2, 2);
				//logger.info("Int representation of {} is {}", longiBin2, longiInt);
	
				result.setLongitude(longiInt * 0.00001);
				logger.info("extractGPSLatLongFromHexStr vehGPS_Longitude = {}",result.getLongitude());
			}
			{
				String latHexStr = hexStr.substring(20, 26);
				//logger.info("LONGITUDE HEX STR:{} ", latHexStr);				
				byte[] LatBytes = Hex.decodeHex(latHexStr.toCharArray());
				String latiBin =String.format("%8s", Integer.toBinaryString(LatBytes[0] & 0xFF)).replace(' ', '0')
						+ String.format("%8s", Integer.toBinaryString(LatBytes[1] & 0xFF)).replace(' ', '0')
						+ String.format("%8s", Integer.toBinaryString(LatBytes[2] & 0xFF)).replace(' ', '0');
				//logger.info("LATITUDE BYTES :{} ", latiBin);
				Integer latiInt = Integer.parseInt(latiBin, 2);
				//logger.info("Int representation of {} is {}", latiBin, latiInt);

				result.setLatitude(latiInt * 0.00001);
				logger.info("extractGPSLatLongFromHexStr vehGPS_Latitude = {}",result.getLatitude());
			}

		} catch (DecoderException e) {
			logger.error("Exception occured: ",e);
		}
		return result;
	}	

	private Integer extractGPSHeadingDirectionFromHexStr(String hexStr) {
		Integer angleInt = 0;
		try {
			logger.info("extractHeadingAngleFromHexStr {}",hexStr);
			{
				String angleHexStr = hexStr.substring(28, 32);
				logger.info("HEADING ANGLE HEX STR:{} ", angleHexStr);
				byte[] angleBytes = Hex.decodeHex(angleHexStr.toCharArray());
				angleBytes[0] = (byte)( angleBytes[0] & 3); //Take LSB 2 Bits from first byte
				String angleBin = String.format("%8s", Integer.toBinaryString(angleBytes[0] & 0xFF)).replace(' ', '0')
						+ String.format("%8s", Integer.toBinaryString(angleBytes[1] & 0xFF)).replace(' ', '0');			
				logger.info("HEADING ANGLE BYTES AFTER MASKING:{} ", angleBin);
				angleInt = Integer.parseInt(angleBin, 2);
				//logger.info("Int representation of {} is {}", longiBin2, longiInt);
				logger.info("extractGPSHeadingDirectionFromHexStr headingAngle = {}",angleInt);
			}
			
		} catch (DecoderException e) {
			logger.error("Exception occured: ",e);
		}
		return angleInt;
	}	
	
	public String toString() {
		
		return " Location [Lat,Long]=" + getLatLong().getLatitude() + "," + getLatLong().getLongitude()
				+ "\n Heading Angle = " + getHeadingAngle();
		
	}
}
