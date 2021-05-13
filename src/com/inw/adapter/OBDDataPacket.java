package com.inw.adapter;
import java.nio.ByteBuffer;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

public class OBDDataPacket extends AdapterBase {
	
	private String totalLength;
	private String protocolVersion;
	private String encryptionMethod;
	private String deviceId;
	private String messageType;
	private Integer effectiveDatalength;
	private String actualData;
	private String checksum;
	private String rawData;
	private String frameNumber;
	private String header;
	private String dataTypeMajor;
	private String dataTypeMinor;
	
	private String vehVIN_CommProtocol;
	private String vehVIN_VINLength;
	
	private int vehVIN_Length_int;
	private String vehVIN_VINCode;
	private String vehVIN_VINCodeHex_str;
	
	private VehicleFlowData flowData;
	private VehTripSummaryData tripSummaryData;
	private VehicleAlertData vehAlertData;
	
	private  OBDDeviceData obdDeviceData;
	private SimWifiInfo simWifiInfoData;
	private OBDDongleStateData obdDongleStateData;
	
	private DTCErrorCode dtcErrorCodeData;
	
	private boolean skipSendingToServer =  false;
	
	public VehicleFlowData getFlowData() {
		return flowData;
	}

	public void initFlowData() {
		this.flowData = new VehicleFlowData();
	}
	
	public void initObdDongleStateData() {
		this.obdDongleStateData = new OBDDongleStateData();
	}
	
	public OBDDongleStateData getObdDongleStateData() {
		return this.obdDongleStateData;
	}
	
	public void initDTCErrorData() {
		this.dtcErrorCodeData = new DTCErrorCode();
	}
	
	public DTCErrorCode getDTCErrorData() {
		return this.dtcErrorCodeData;
	}	
	
	public void initOBDDeviceData() {
		this.obdDeviceData = new OBDDeviceData();
	}	
	
	public void initSimWifiInfoData() {
		this.simWifiInfoData = new SimWifiInfo();
	}
	
	public SimWifiInfo getSimWifiInfo() {
		return simWifiInfoData;
	}
	
	
	public void initTripSummaryData() {
		this.tripSummaryData = new VehTripSummaryData();
	}
	
	public void initVehicleAlertData() {
		this.vehAlertData = new VehicleAlertData();
	}
	
	public OBDDeviceData getOBDDeviceData() {
		return this.obdDeviceData;
	}	
	
	public VehTripSummaryData getTripSummary() {
		return this.tripSummaryData;
	}
	
	public String getRawData() {
		return rawData;
	}
	
	public void setSkipSendingToServer(boolean shouldSkip) {
		this.skipSendingToServer = shouldSkip;
	}

	public void setRawData(String rawData) {
		this.rawData = rawData;
	}

	public OBDDataPacket(String totalLength, String protocolVersion, String encryptionMethod, String deviceId,
			String messageType, Integer effectiveDatalength, String actualData, String checksum) {
		super();
		this.totalLength = totalLength;
		this.protocolVersion = protocolVersion;
		this.encryptionMethod = encryptionMethod;
		this.deviceId = deviceId;
		this.messageType = messageType;
		this.effectiveDatalength = effectiveDatalength;
		this.actualData = actualData;
		this.checksum = checksum;
	}
	
	public OBDDataPacket() {
		super();
	}

	@Override
	public String toString() {
		String str = "\n deviceId = " + deviceId 
				+ "\n messageType = " + messageType 
				+ "\n dataTypeMajor = " + dataTypeMajor + ", dataTypeMinor = " + dataTypeMinor;
				//+ " \n totalLength=" + totalLength + ", protocolVersion=" + protocolVersion + ", encryptionMethod="
				//+ " \n " + encryptionMethod + ", deviceId=" + deviceId + 
				//+ " \n " + ", effectiveDatalength=" + effectiveDatalength + ", checksum="
				//+ " \n " + checksum + ", frameNumber=" + frameNumber + ", header=" + header
				
		
		if(dataTypeMajor != null && dataTypeMinor != null ) {
			if(dataTypeMajor.equals("01")) { // Driving Behavior Data
				
				if(dataTypeMinor.equals("00") || // Sudden Acceleration
					dataTypeMinor.equals("01") || // Sudden deceleration
					dataTypeMinor.equals("02") || // Sharp turn
					dataTypeMinor.equals("03") || // Exceed idle
					dataTypeMinor.equals("04") ) { // Driving tired 
					
				}				
			}else if(dataTypeMajor.equals("02")) { // GPS Data
				
				if(dataTypeMinor.equals("00")) { // Flow Data
					str =  str + " \n GPS Location [Lat,Long]=" + getFlowData().getVehGPS_Data().getLatLong().getLatitude() 
								+ "," + getFlowData().getVehGPS_Data().getLatLong().getLongitude();
				}				
			}else if(dataTypeMajor.equals("03")) { // Vehicle Data
				if(dataTypeMinor.equals("01")) { // Flow Data

					str =  str + flowData.toString();
				
				}else if(dataTypeMinor.equals("00")) { //VIN Data
					
					str =  str + "\n VehVIN_CommProtocol=" + vehVIN_CommProtocol 
							+ "\n VehVIN_VINLength=" + vehVIN_VINLength 
							+ "\n VehVIN_VINCode = "+ vehVIN_VINCode + " >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> ";
				}
			}else if(dataTypeMajor.equals("00")) { // Trip Data
				
				if(dataTypeMinor.equals("00")) { // Summary data 
					str =  str + tripSummaryData.toString();
					
				}else if(dataTypeMinor.equals("01")) { // Ignition on
					str =  str + " \n ignitionON [Lat,Long]=" + getTripSummary().getIgn_ON_GPSData().getLatLong().getLatitude() 
								+ "," +  getTripSummary().getIgn_ON_GPSData().getLatLong().getLongitude()
								+ " \n ignition_ONTime = " +getTripSummary().getIgnition_ON_Time_dt() ;
				}else if(dataTypeMinor.equals("02")) { // Ignition off
					str =  str + " \n ignitionOFF [Lat,Long]=" + getTripSummary().getIgn_OFF_GPSData().getLatLong().getLatitude() 
							+ "," +  getTripSummary().getIgn_OFF_GPSData().getLatLong().getLongitude()
							+ " \n ignition_OFFTime = " +getTripSummary().getIgnition_OFF_Time_dt() ;					
				}else if(dataTypeMinor.equals("03")) { // Passive trip start
					
				}else if(dataTypeMinor.equals("04")) { // Passive trip end
					
				}					
			}else if(dataTypeMajor.equals("04")) { // OBD Dongle State Info
				
				if(dataTypeMinor.equalsIgnoreCase("0C")) { // DTC Error code data 
					str =  str + getSimWifiInfo().toString();
					
				}else if(dataTypeMinor.equals("02") ||
						dataTypeMinor.equals("03") ||
						dataTypeMinor.equals("04") ||
						dataTypeMinor.equals("05") ||
						dataTypeMinor.equals("06") ) { // Low Battery Data
					str =  str + getObdDongleStateData().toString() ;
				}					
			}else if(dataTypeMajor.equals("05")) { // Vehicle DTC error and other Alerts
				
				if(dataTypeMinor.equals("00")) { // DTC Error code data 
					str =  str + dtcErrorCodeData.toString();
				}else if(dataTypeMinor.equals("01") ||
						dataTypeMinor.equals("02") ||
						dataTypeMinor.equals("03") ||
						dataTypeMinor.equals("04")) { // Low Battery Data
					
					str =  str + getVehicleAlertData().toString() ;
				}					
			}			
			
		}
		return str;
	}


	public String getTotalLength() {
		return totalLength;
	}


	public void setTotalLength(String totalLength) {
		this.totalLength = totalLength;
	}


	public String getProtocolVersion() {
		return protocolVersion;
	}


	public void setProtocolVersion(String protocolVersion) {
		this.protocolVersion = protocolVersion;
	}


	public String getEncryptionMethod() {
		return encryptionMethod;
	}


	public void setEncryptionMethod(String encryptionMethod) {
		this.encryptionMethod = encryptionMethod;
	}


	public String getDeviceId() {
		return deviceId;
	}


	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}


	public String getMessageType() {
		return messageType;
	}


	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}


	public Integer getEffectiveDatalength() {
		return effectiveDatalength;
	}


	public void setEffectiveDatalength(Integer effectiveDatalength) {
		this.effectiveDatalength = effectiveDatalength;
	}


	public String getActualData() {
		return actualData;
	}


	public void setActualData(String actualData) {
		this.actualData = actualData;
	}


	public String getChecksum() {
		return checksum;
	}


	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}


	public String getFrameNumber() {
		return frameNumber;
	}


	public void setFrameNumber(String frameNumber) {
		this.frameNumber = frameNumber;
	}


	public String getHeader() {
		return header;
	}


	public void setHeader(String header) {
		this.header = header;
	}


	public String getDataTypeMinor() {
		return dataTypeMinor;
	}


	public void setDataTypeMinor(String dataTypeMinor) {
		this.dataTypeMinor = dataTypeMinor;
	}

	public String getDataTypeMajor() {
		return dataTypeMajor;
	}

	public void setDataTypeMajor(String dataTypeMajor) {
		this.dataTypeMajor = dataTypeMajor;
	}

	public String getVehVIN_CommProtocol() {
		return vehVIN_CommProtocol;
	}

	public void setVehVIN_CommProtocol(String vehVIN_CommProtocol) {
		this.vehVIN_CommProtocol = vehVIN_CommProtocol;
	}

	public String getVehVIN_VINLength() {
		return vehVIN_VINLength;
	}

	public void setVehVIN_VINLength(String vehVIN_VINLength) {
		this.vehVIN_VINLength = vehVIN_VINLength;
		try {
			vehVIN_Length_int = (int)ByteBuffer.wrap(Hex.decodeHex(this.vehVIN_VINLength)).get();
			logger.info("vehVIN_VINLength_int : ", vehVIN_Length_int);
		} catch (DecoderException e) {
			logger.error("Exception occured : ",e);
		}
	}

	public String getVehVIN_VINCode() {
		return vehVIN_VINCode;
	}

	public void setVehVIN_VINCodeHexStr(String vehVIN_VINCodeHexStr) {
		this.vehVIN_VINCodeHex_str = vehVIN_VINCodeHexStr;
		logger.info("Parsing VIn Code from Hex {}",vehVIN_VINCodeHexStr);
		this.vehVIN_VINCode = null;
		// Test code to convert IMEI number received over TCP bytes into its digit form.
		try {
			byte[] vinBytes = Hex.decodeHex(vehVIN_VINCodeHexStr);
			this.vehVIN_VINCode = new String(vinBytes);
		} catch (DecoderException e1) {
			logger.error(e1.getStackTrace().toString());
		}
	}

	public int getVehVIN_Length_int() {
		return vehVIN_Length_int;
	}

	public VehicleAlertData getVehicleAlertData() {
		return vehAlertData;
	}

	public boolean isSkipSendingToServer() {
		return skipSendingToServer;
	}
		
}
