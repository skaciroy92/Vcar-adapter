package com.inw.adapter;

import java.nio.ByteBuffer;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.joda.time.DateTime;

public class VehicleAlertData extends AdapterBase implements OBDDataSection {

	private String Alert_BatteryVoltage_str,	
					Alert_DevicePulledoutState_str,
					Alert_SuspectedCollision_str,
					Alert_VibrationAfterIgnitionOFF_str,
					Alert_BatteryVoltageTime_str,
					Alert_DevicePulledOutTime_str,
					Alert_SuspectedCollisionTime_str,
					Alert_VibrationAfterIngnitionOFFTime_str,
					Alert_BatteryVoltageGPS_str,
					Alert_DevicePulledOutGPS_str,
					Alert_SuspectedCollionGPS_str,
					Alert_VibrationAfterIgnitionOnGPS_str;		
	
	private double Alert_BatteryVoltage,	
					Alert_SuspectedCollision;
	
	private int Alert_VibrationAfterIgnitionOFF,
				Alert_DevicePulledoutState;

	private DateTime Alert_BatteryVoltageTime,
			Alert_DevicePulledOutTime,
			Alert_SuspectedCollisionTime,
			Alert_VibrationAfterIngnitionOFFTime;

	private GPSData Alert_BatteryVoltageGPS,
			Alert_DevicePulledOutGPS,
			Alert_SuspectedCollionGPS,
			Alert_VibrationAfterIgnitionOnGPS;	

	public VehicleAlertData() {
		// TODO Auto-generated constructor stub
	}

	public String toString() {
		String str = " \n Alert_BatteryVoltage=" + Alert_BatteryVoltage 
		+ " \n Alert_BatteryVoltageTime=" + Alert_BatteryVoltageTime
		+ " \n Alert_BatteryVoltageGPS=" + Alert_BatteryVoltageGPS 
		+ " \n Alert_DevicePulledoutState=" + Alert_DevicePulledoutState 
		+ " \n Alert_DevicePulledOutTime=" + Alert_DevicePulledOutTime
		+ " \n Alert_DevicePulledOutGPS=" + Alert_DevicePulledOutGPS
		+ " \n Alert_SuspectedCollision=" + Alert_SuspectedCollision
		+ " \n Alert_SuspectedCollisionTime=" + Alert_SuspectedCollisionTime 
		+ " \n Alert_SuspectedCollionGPS=" + Alert_SuspectedCollionGPS
		+ " \n Alert_VibrationAfterIgnitionOFF=" + Alert_VibrationAfterIgnitionOFF
		+ " \n Alert_VibrationAfterIngnitionOFFTime=" + Alert_VibrationAfterIngnitionOFFTime 
		+ " \n Alert_VibrationAfterIgnitionOnGPS=" + Alert_VibrationAfterIgnitionOnGPS;		
		return str;		
	}



	public double getAlert_BatteryVoltage() {
		return Alert_BatteryVoltage;
	}

	public double getAlert_DevicePulledoutState() {
		return Alert_DevicePulledoutState;
	}

	public double getAlert_SuspectedCollision() {
		return Alert_SuspectedCollision;
	}

	public double getAlert_VibrationAfterIgnitionOFF() {
		return Alert_VibrationAfterIgnitionOFF;
	}

	public DateTime getAlert_BatteryVoltageTime() {
		return Alert_BatteryVoltageTime;
	}

	public DateTime getAlert_DevicePulledOutTime() {
		return Alert_DevicePulledOutTime;
	}

	public DateTime getAlert_SuspectedCollisionTime() {
		return Alert_SuspectedCollisionTime;
	}

	public DateTime getAlert_VibrationAfterIngnitionOFFTime() {
		return Alert_VibrationAfterIngnitionOFFTime;
	}

	public GPSData getAlert_BatteryVoltageGPS() {
		return Alert_BatteryVoltageGPS;
	}

	public GPSData getAlert_DevicePulledOutGPS() {
		return Alert_DevicePulledOutGPS;
	}

	public GPSData getAlert_SuspectedCollionGPS() {
		return Alert_SuspectedCollionGPS;
	}

	public GPSData getAlert_VibrationAfterIgnitionOnGPS() {
		return Alert_VibrationAfterIgnitionOnGPS;
	}

	public void setAlert_BatteryVoltage_str(String alert_BatteryVoltage_str) {
		Alert_BatteryVoltage_str = alert_BatteryVoltage_str;
		try {
			logger.info("Alert_BatteryVoltage_str {}",Alert_BatteryVoltage_str);
			this.Alert_BatteryVoltage = ByteBuffer.wrap(Hex.decodeHex(Alert_BatteryVoltage_str.toCharArray())).getShort() *0.1;
			logger.info("Alert_BatteryVoltage {}",Alert_BatteryVoltage);
			
		} catch (DecoderException e) {
			logger.error("Error occured : {}", e);
		}
	}

	public void setAlert_DevicePulledoutState_str(String alert_DevicePulledoutState_str) {
		Alert_DevicePulledoutState_str = alert_DevicePulledoutState_str;
		try {
			logger.info("Alert_DevicePulledoutState_str {}",Alert_DevicePulledoutState_str);
			this.Alert_DevicePulledoutState = ByteBuffer.wrap(Hex.decodeHex(Alert_DevicePulledoutState_str.toCharArray())).get();
			logger.info("Alert_DevicePulledoutState {}",Alert_DevicePulledoutState);
			
		} catch (DecoderException e) {
			logger.error("Error occured : {}", e);
		}		
	}

	public void setAlert_SuspectedCollision_str(String alert_SuspectedCollision_str) {
		Alert_SuspectedCollision_str = alert_SuspectedCollision_str;
		try {
			logger.info("Alert_SuspectedCollision_str {}",Alert_SuspectedCollision_str);
			this.Alert_SuspectedCollision = ByteBuffer.wrap(Hex.decodeHex(Alert_SuspectedCollision_str.toCharArray())).get() * 0.1;
			logger.info("Alert_SuspectedCollision {}",Alert_SuspectedCollision);
			
		} catch (DecoderException e) {
			logger.error("Error occured : {}", e);
		}			
	}	
	
	public void setAlert_VibrationAfterIgnitionOFF_str(String alert_VibrationAfterIgnitionOFF_str) {
		Alert_VibrationAfterIgnitionOFF_str = alert_VibrationAfterIgnitionOFF_str;
		try {
			logger.info("Alert_VibrationAfterIgnitionOFF_str {}",Alert_VibrationAfterIgnitionOFF_str);
			this.Alert_VibrationAfterIgnitionOFF = Short.toUnsignedInt(ByteBuffer.wrap(Hex.decodeHex(Alert_VibrationAfterIgnitionOFF_str.toCharArray())).getShort());
			logger.info("Alert_VibrationAfterIgnitionOFF {}",Alert_VibrationAfterIgnitionOFF);
			
		} catch (DecoderException e) {
			logger.error("Error occured : {}", e);
		}		
	}

	public void setAlert_VibrationAfterIngnitionOFFTime_str(String alert_VibrationAfterIngnitionOFFTime_str) {
		Alert_VibrationAfterIngnitionOFFTime_str = alert_VibrationAfterIngnitionOFFTime_str;
		this.Alert_VibrationAfterIngnitionOFFTime = this.getDateFromHexString(Alert_VibrationAfterIngnitionOFFTime_str);
	}

	public void setAlert_SuspectedCollisionTime_str(String alert_SuspectedCollisionTime_str) {
		Alert_SuspectedCollisionTime_str = alert_SuspectedCollisionTime_str;
		this.Alert_SuspectedCollisionTime = this.getDateFromHexString(Alert_SuspectedCollisionTime_str);		
	}

	public void setAlert_DevicePulledOutTime_str(String alert_DevicePulledOutTime_str) {
		Alert_DevicePulledOutTime_str = alert_DevicePulledOutTime_str;
		this.Alert_DevicePulledOutTime = this.getDateFromHexString(Alert_DevicePulledOutTime_str);		
		
	}

	public void setAlert_BatteryVoltageTime_str(String alert_BatteryVoltageTime_str) {
		Alert_BatteryVoltageTime_str = alert_BatteryVoltageTime_str;
		this.Alert_BatteryVoltageTime = this.getDateFromHexString(Alert_BatteryVoltageTime_str);		
		
	}

	public void setAlert_BatteryVoltageGPS_str(String alert_BatteryVoltageGPS_str) {
		Alert_BatteryVoltageGPS_str = alert_BatteryVoltageGPS_str;
		this.Alert_BatteryVoltageGPS = new GPSData(this.Alert_BatteryVoltageGPS_str);						
	}
	
	public void setAlert_SuspectedCollionGPS_str(String alert_SuspectedCollionGPS_str) {
		Alert_SuspectedCollionGPS_str = alert_SuspectedCollionGPS_str;
		this.Alert_SuspectedCollionGPS = new GPSData(this.Alert_SuspectedCollionGPS_str);						

	}

	public void setAlert_DevicePulledOutGPS_str(String alert_DevicePulledOutGPS_str) {
		Alert_DevicePulledOutGPS_str = alert_DevicePulledOutGPS_str;
		this.Alert_DevicePulledOutGPS = new GPSData(this.Alert_DevicePulledOutGPS_str);						
		
	}

	public void setAlert_VibrationAfterIgnitionOnGPS_str(String alert_VibrationAfterIgnitionOnGPS_str) {
		Alert_VibrationAfterIgnitionOnGPS_str = alert_VibrationAfterIgnitionOnGPS_str;
		this.Alert_VibrationAfterIgnitionOnGPS = new GPSData(this.Alert_VibrationAfterIgnitionOnGPS_str);						

	}
}
