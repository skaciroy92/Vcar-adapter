 package com.inw.adapter;

import java.nio.ByteBuffer;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.joda.time.DateTime;

public class OBDDongleStateData extends AdapterBase {

	private double state_SleepVoltage,state_WakeUpVoltage;
	
	private int state_WakeUpType, state_PowerONAfterRebootType, state_UpgradeStatus;

	private DateTime state_SleepTime_dt,
					state_WakeUpTime_dt,
					state_PowerONAfterRebootTime_dt,
					state_PowerDownLastTime_dt,
					state_CantLocateForLong_dt,
					state_UpgradeCompleteTime_dt;
	
	private GPSData state_SleepGPS,
					state_WakeUpGPS,
					state_CantLocateLastGPS;	
	
	@Override
	public String toString() {
		String str = " \n State_SleepVoltage= " + state_SleepVoltage 
		+ " \n State_SleepTime_dt= " + state_SleepTime_dt
		+ " \n State_SleepGPS= " + state_SleepGPS 
		+ " \n State_WakeUpType= " + state_WakeUpType 
		+ " \n State_WakeUpTime= " + state_WakeUpTime_dt
		+ " \n State_WakeUpVoltage= " + state_WakeUpVoltage 
		+ " \n State_WakeUpGPS= " + state_WakeUpGPS
		+ " \n State_PowerONAfterRebootType= " + state_PowerONAfterRebootType
		+ " \n State_PowerONAfterRebootTime_dt= " + state_PowerONAfterRebootTime_dt
		+ " \n State_PowerDownLastTime_dt= " + state_PowerDownLastTime_dt
		+ " \n State_CantLocateForLong_dt=" + state_CantLocateForLong_dt
		+ " \n State_CantLocateLastGPS=" + state_CantLocateLastGPS 
		+ " \n State_UpgradeCompleteTime_dt=" + state_UpgradeCompleteTime_dt
		+ " \n state_UpgradeStatus=" + state_UpgradeStatus;	
	
		return str;
	}
	
	public void setState_PowerONAfterRebootTime_str(String dateTimeStr) {
		logger.info(" State_PowerONAfterRebootTime_str {}",dateTimeStr);
		this.state_PowerONAfterRebootTime_dt = this.getDateFromHexString(dateTimeStr);
		logger.info(" State_PowerONAfterRebootTime_dt {}",this.state_PowerONAfterRebootTime_dt);
	}
	
	public DateTime getState_PowerONAfterRebootTime() {
		return state_PowerONAfterRebootTime_dt;
	}
	public void setState_PowerDownLastTime_str(String dateTimeStr) {
		logger.info(" state_PowerDownLastTime_str {}",dateTimeStr);
		this.state_PowerDownLastTime_dt = this.getDateFromHexString(dateTimeStr);
		logger.info(" State_PowerDownLastTime_dt {}",this.state_PowerDownLastTime_dt);
	}
	
	public DateTime getState_State_PowerDownLastTime() {
		return state_PowerDownLastTime_dt;
	}
	
	public void setState_PowerONAfterRebootType(String lengthHexStr) {
		try {
			state_PowerONAfterRebootType = (int)ByteBuffer.wrap(Hex.decodeHex(lengthHexStr)).get();
			logger.info("state_PowerONAfterRebootType : ", state_PowerONAfterRebootType);
		} catch (DecoderException e) {
			logger.error("Exception occured : ",e);
		}
	}	
	
	public int getState_PowerONAfterRebootType() {
		return state_PowerONAfterRebootType;
	}	
	
	
	
	public void setState_WakeUpType(String lengthHexStr) {
		try {
			state_WakeUpType = (int)ByteBuffer.wrap(Hex.decodeHex(lengthHexStr)).get();
			logger.info("State_WakeUpType : ", state_WakeUpType);
		} catch (DecoderException e) {
			logger.error("Exception occured : ",e);
		}
	}
	
	public int getState_WakeUpType() {
		return state_WakeUpType;
	}
	
	public void setState_WakeUpGPS(String GPSposition) {
		this.state_WakeUpGPS = new GPSData(GPSposition);
	}
	public GPSData getState_WakeUpGPS() {
		return this.state_WakeUpGPS;
	}
	
	public void setState_WakeUpTime_str(String dateTimeStr) {
		logger.info(" State_PowerONAfterRebootTime_str {}",dateTimeStr);
		this.state_WakeUpTime_dt = this.getDateFromHexString(dateTimeStr);
		logger.info(" State_WakeUpTime_dt {}",this.state_WakeUpTime_dt);
	}
	
	public DateTime getState_WakeUpTime() {
		return state_WakeUpTime_dt;
	}
	public void setState_WakeUpVoltage(String hexStr) {
		try {
			state_WakeUpVoltage = (double)ByteBuffer.wrap(Hex.decodeHex(hexStr)).getShort() * 0.1;
			logger.info("state_WakeUpVoltage : ", state_WakeUpVoltage);
		} catch (DecoderException e) {
			logger.error("Exception occured : ",e);
		}
	}
	
	public double getState_WakeUpVoltage() {
		return state_WakeUpVoltage;
	}	
	
	
	
	public void setState_SleepVoltage(String hexStr) {
		try {
			state_SleepVoltage = (double)ByteBuffer.wrap(Hex.decodeHex(hexStr)).getShort() * 0.1;
			logger.info("state_SleepVoltage : ", state_SleepVoltage);
		} catch (DecoderException e) {
			logger.error("Exception occured : ",e);
		}
	}
	
	public double getState_SleepVoltage() {
		return state_SleepVoltage;
	}
	
	public void setState_SleepGPS(String GPSposition) {
		this.state_SleepGPS = new GPSData(GPSposition);
	}
	public GPSData getState_SleepGPS() {
		return this.state_SleepGPS;
	}
	
	public void setstate_SleepTime(String dateTimeStr) {
		logger.info(" state_SleepTime_str {}",dateTimeStr);
		this.state_SleepTime_dt = this.getDateFromHexString(dateTimeStr);
		logger.info(" state_SleepTime_dt {}",this.state_SleepTime_dt);
	}
	
	public DateTime getState_SleepTime() {
		return state_SleepTime_dt;
	}
	
	public void setState_CantLocateLastGPS(String GPSposition) {
		this.state_CantLocateLastGPS = new GPSData(GPSposition);
	}
	public GPSData getState_CantLocateLastGPS() {
		return this.state_CantLocateLastGPS;
	}
	
	public void setState_CantLocateForLongTime(String dateTimeStr) {
		logger.info(" state_CantLocateForLongTime_str {}",dateTimeStr);
		this.state_CantLocateForLong_dt = this.getDateFromHexString(dateTimeStr);
		logger.info(" state_CantLocateForLongTime_dt {}",this.state_CantLocateForLong_dt);
	}
	
	public DateTime getState_CantLocateForLongTime() {
		return state_CantLocateForLong_dt;
	}
	
	public void setState_UpgradeStatus(String lengthHexStr) {
		try {
			state_UpgradeStatus = (int)ByteBuffer.wrap(Hex.decodeHex(lengthHexStr)).get();
			logger.info("State_UpgradeStatus : ", state_UpgradeStatus);
		} catch (DecoderException e) {
			logger.error("Exception occured : ",e);
		}
	}
	
	public int getState_UpgradeStatus() {
		return state_UpgradeStatus;
	}
	
	public void setState_UpgradeCompleteTime(String dateTimeStr) {
		logger.info(" state_UpgradeCompleteTime_str {}",dateTimeStr);
		this.state_UpgradeCompleteTime_dt = this.getDateFromHexString(dateTimeStr);
		logger.info(" state_UpgradeCompleteTime_dt {}",this.state_UpgradeCompleteTime_dt);
	}
	
	public DateTime getState_UpgradeCompleteTime() {
		return state_UpgradeCompleteTime_dt;
	}	
}
