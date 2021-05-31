package com.inw.adapter;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.zip.Adler32;
import java.util.zip.Checksum;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

public class ReceivePackets extends AdapterBase {

	private static void sendConnectionAck(OBDDataPacket decryptedData, Socket s) throws DecoderException {
		logger.info("Sending connection ack..");
		// Checksum checksum = new Adler32();
		ConnectionAck ack = new ConnectionAck();
		ack.setHeader(decryptedData.getHeader());
		ack.setDataLength("002A");
		ack.setVersion(decryptedData.getProtocolVersion());
		ack.setEncryptionMethod(decryptedData.getEncryptionMethod());
		ack.setReserved("0000");
		ack.setDeviceId(decryptedData.getDeviceId());
		ack.setFrameType("02");
		ack.setFrameNumber(decryptedData.getFrameNumber());
		ack.setEffectiveDataLength("0005");
		ack.setSessionType("01");
		ack.setUtcTime(Long.toHexString(System.currentTimeMillis() / 1000));

		String frameHeader = ack.getDataLength() + ack.getVersion() + ack.getEncryptionMethod() + ack.getReserved()
				+ ack.getDeviceId();
		String frameData = ack.getFrameType() + ack.getFrameNumber() + ack.getEffectiveDataLength()
				+ ack.getSessionType() + ack.getUtcTime();

		byte[] ackBytes = Hex.decodeHex((frameHeader + frameData).toCharArray());

		Checksum checksum = new Adler32();
		checksum.update(ackBytes, 0, ackBytes.length);

		byte[] checksumBytes = ByteBuffer.allocate(4).putInt((int) checksum.getValue()).array();

		String checksumString = Hex.encodeHexString(checksumBytes);

		String ackPacket = frameData + checksumString + "ffff";
		
		String mcuKey = MCUKeyManager.getInstance().getMCUKeyForIMEINumber(decryptedData.getDeviceId());
		
		String encryptedPacket = Hex.encodeHexString(encrypt(Hex.decodeHex(ackPacket.toCharArray()), Hex.decodeHex(mcuKey.toCharArray())));

		String finalAckPacket = ack.getHeader() + frameHeader + encryptedPacket + "AAAA";
		//logger.info("Ack packet " + finalAckPacket);

		try {
			DataOutputStream dout = new DataOutputStream(s.getOutputStream());
			dout.write(Hex.decodeHex(finalAckPacket.toCharArray()));
			dout.flush();
		} catch (DecoderException e) {
			// TODO Auto-generated catch block
			logger.error("Exception occured: ",e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("Exception occured: ",e);
		}
	}

	private static void sendPublishAck(OBDDataPacket dataPacket, Socket s) throws DecoderException {

		logger.info("Sending Publish ack ");

		ConnectionAck ack = new ConnectionAck();
		ack.setHeader("5555");
		ack.setDataLength("002A");
		ack.setVersion(dataPacket.getProtocolVersion());
		ack.setEncryptionMethod(dataPacket.getEncryptionMethod());
		ack.setReserved("0000");
		ack.setDeviceId(dataPacket.getDeviceId());
		ack.setFrameType("04");
		ack.setFrameNumber(dataPacket.getFrameNumber());
		ack.setDataTypeMajor("00");
		ack.setDataTypeMinor("01");

		ack.setTail("AAAA");

		String frameHeader = ack.getDataLength() + ack.getVersion() + ack.getEncryptionMethod() + ack.getReserved()
				+ ack.getDeviceId();
		String frameData = ack.getFrameType() + ack.getFrameNumber() + "0002" + ack.getDataTypeMajor()
				+ ack.getDataTypeMinor();

		byte[] ackBytes = Hex.decodeHex((frameHeader + frameData).toCharArray());

		Checksum checksum = new Adler32();
		checksum.update(ackBytes, 0, ackBytes.length);

		byte[] checksumBytes = ByteBuffer.allocate(4).putInt((int) checksum.getValue()).array();

		String checksumString = Hex.encodeHexString(checksumBytes);

		String ackPacket = frameData + checksumString + "ffffffffff";
		
		String mcuKey = MCUKeyManager.getInstance().getMCUKeyForIMEINumber(dataPacket.getDeviceId());		
		String encryptedPacket = Hex.encodeHexString(encrypt(Hex.decodeHex(ackPacket.toCharArray()), Hex.decodeHex(mcuKey.toCharArray())));

		String finalAckPacket = ack.getHeader() + frameHeader + encryptedPacket + "AAAA";
		logger.info("Ack packet " + finalAckPacket);

		try {
			DataOutputStream dout = new DataOutputStream(s.getOutputStream());
			dout.write(Hex.decodeHex(finalAckPacket.toCharArray()));
			dout.flush();
		} catch (DecoderException e) {
			logger.error("Exception occured: ",e);
		} catch (IOException e) {
			logger.error("Exception occured: ",e);
		}

	}



	public OBDDataPacket processPacket(byte[] packetBytes, Socket s) throws IOException {
		byte[] actualPacket = new byte[packetBytes.length];
		
		System.arraycopy(packetBytes, 0, actualPacket, 0, 24);
		String actualPacketString = Hex.encodeHexString(actualPacket);
		//logger.info("actualPacketString 0-24 bytes= {}", actualPacketString);
		OBDDataPacket dataPacket = new OBDDataPacket();
				
		dataPacket.setHeader(actualPacketString.substring(0, 4));
		dataPacket.setTotalLength(actualPacketString.substring(4, 8));
		dataPacket.setProtocolVersion(actualPacketString.substring(8, 12));
		dataPacket.setEncryptionMethod(actualPacketString.substring(12, 14));
		dataPacket.setDeviceId(actualPacketString.substring(18, 48));		
		
		logger.info("Encryted packets from Device {} is {}",dataPacket.getDeviceId(),Hex.encodeHexString(packetBytes));
		// read the actual data for decryption
		byte[] decryptedPacket = decryptDataFrame(Arrays.copyOfRange(packetBytes, 24, (packetBytes.length) - 2), dataPacket.getDeviceId());
		//logger.info("Decrypted Data : " + Hex.encodeHexString(decryptedPacket));
		//logger.info(" Decrypted packet string {}", Hex.encodeHexString(decryptedPacket));
		
		System.arraycopy(decryptedPacket, 0, actualPacket, 24, decryptedPacket.length);

		// dataPacket.setRawData(packet);
		actualPacketString = Hex.encodeHexString(actualPacket);
		
		dataPacket.setMessageType(actualPacketString.substring(48, 50));
		dataPacket.setEffectiveDatalength(Integer.parseInt(actualPacketString.substring(54, 58), 16));
		dataPacket.setActualData(actualPacketString.substring(58, actualPacketString.length() - 4));
		dataPacket.setFrameNumber(actualPacketString.substring(50, 54));
		

		if (dataPacket.getMessageType().equals("01")) {
			logger.info("Connection Request MsgType [01] Received");
			// Send the connection ack packet
			try {
				sendConnectionAck(dataPacket, s);
				processOBDDeviceData(actualPacketString, dataPacket);
			} catch (DecoderException e) {
				logger.error(e.toString());
			}
		}else if (dataPacket.getMessageType().equals("03")) {
			logger.info("Publish frame msgtype [03] received. ");
			try {
				sendPublishAck(dataPacket, s);
				processPublishDataFrame(actualPacketString, dataPacket);
			} catch (DecoderException e) {
				logger.error(e.toString());
			}
		}else if (dataPacket.getMessageType().equals("04")) {
			logger.info("Publish ACK msgtype [04] received with ActualData{} ", dataPacket.getActualData());
			
		}else if (dataPacket.getMessageType().equalsIgnoreCase("0C")) {
			logger.info("Ping frame msgtype [0C] received ");
			try {
				sendPingResponse(dataPacket, s);
			} catch (DecoderException e) {
				logger.error(e.toString());
			}
		}else if (dataPacket.getMessageType().equals("0E")) {
			logger.info(" Disconnection frame msgtype [0E ] received...");
		}
		
		logger.info(" Extracted datapacket : {} ", dataPacket);
		return dataPacket;
	}

	private void processPublishDataFrame(String actualPacketString, OBDDataPacket dataPacket) {
				
		dataPacket.setDataTypeMajor(actualPacketString.substring(58, 60));
		dataPacket.setDataTypeMinor(actualPacketString.substring(60, 62));
		logger.info("processPublishDataFrame: MajorDataTyep: {}    MinorDatType: {}", dataPacket.getDataTypeMajor(),dataPacket.getDataTypeMinor());
		if(dataPacket.getDataTypeMajor().equals("03") ) {
			if( dataPacket.getDataTypeMinor().equals("01")) {
			
			this.processVehicleFlowData(actualPacketString, dataPacket);
			}else if (dataPacket.getDataTypeMinor().equals("00")) {
				
				this.processVehicleVINData(actualPacketString, dataPacket);
			}
			
		}else if(dataPacket.getDataTypeMajor().equals("02")) {
			if( dataPacket.getDataTypeMinor().equals("00")) {
				this.processGPSData(actualPacketString, dataPacket);
			}
		}else if(dataPacket.getDataTypeMajor().equals("00")) {
			if( dataPacket.getDataTypeMinor().equals("00")) {
				this.processTripSummaryData(actualPacketString, dataPacket);
			} else  if( dataPacket.getDataTypeMinor().equals("01")) {
				this.processIgnitionONStatusData(actualPacketString, dataPacket);
			} else  if( dataPacket.getDataTypeMinor().equals("02")) {
				this.processIgnitionOFFStatusData(actualPacketString, dataPacket);
			}			
		}else if(dataPacket.getDataTypeMajor().equals("05")) {
			
			if( dataPacket.getDataTypeMinor().equals("00")) {
				
				this.processDTCErrorData(actualPacketString, dataPacket);
			}else if( dataPacket.getDataTypeMinor().equals("01")) {
				
				this.processVehicleAlertBatteryVoltage(actualPacketString, dataPacket);
			}else if( dataPacket.getDataTypeMinor().equals("02")) {
				
				this.processVehicleAlertVibrationAfterIgnitionOff(actualPacketString, dataPacket);
			}else if( dataPacket.getDataTypeMinor().equals("05")) {
				
				this.processVehicleAlertSuspectedCollision(actualPacketString, dataPacket);
			}else if( dataPacket.getDataTypeMinor().equals("07")) {
				
				this.processVehicleAlertDevicePulledOut(actualPacketString, dataPacket);
			}
		}else if(dataPacket.getDataTypeMajor().equals("04")) { // Device Info
			
			if( dataPacket.getDataTypeMinor().equals("02")) { // OBD Dongle Sleep Event
				
				this.processDongleStateSleepMode(actualPacketString, dataPacket);
			}else if( dataPacket.getDataTypeMinor().equals("03")) { // OBD Dongle Wake up event
				
				this.processDongleStateWakeupMode(actualPacketString, dataPacket);
			}else if( dataPacket.getDataTypeMinor().equals("04")) { // OBD Dongle Cant Locate event
				
				this.processDongleStateCantLocateMode(actualPacketString, dataPacket);
			}else if( dataPacket.getDataTypeMinor().equals("05")) { // OBD Dongle Power ON After Reboot event
				
				this.processDongleStatePowerOnAfterRebootMode(actualPacketString, dataPacket);
			}else if( dataPacket.getDataTypeMinor().equals("06")) { // OBD Dongle Upgrade event
				
				this.processDongleStateUpgradeCompleteMode(actualPacketString, dataPacket);
			}else if( dataPacket.getDataTypeMinor().equalsIgnoreCase("0C")) {
				
				this.processSIMWiFiInfoData(actualPacketString, dataPacket);
			}
		}
	}
	
	private void processOBDDeviceData(String actualPacketString, OBDDataPacket dataPacket) {
		logger.info("Reading OOB Device Data !");
		dataPacket.initOBDDeviceData();
		dataPacket.getOBDDeviceData().setProtocolVersion(actualPacketString.substring(8, 12));
		dataPacket.getOBDDeviceData().setHardWareVersion(actualPacketString.substring(58, 62));
		dataPacket.getOBDDeviceData().setSoftWareVersions(actualPacketString.substring(62));
	}	
	
	private void processVehicleFlowData(String actualPacketString, OBDDataPacket dataPacket) {
		logger.info("Reading Vehicle Flow Data !");
		dataPacket.initFlowData();
		dataPacket.getFlowData().setUTC_Time(actualPacketString.substring(62, 70));
		dataPacket.getFlowData().setDataNumber(actualPacketString.substring(70, 72));
		dataPacket.getFlowData().setGPSposition_str(actualPacketString.substring(72, 120));
		dataPacket.getFlowData().setRPM(actualPacketString.substring(120, 124));
		dataPacket.getFlowData().setSpeed(actualPacketString.substring(124, 128 ));
		dataPacket.getFlowData().setEngineCoolantTemperature(actualPacketString.substring(128, 132));
		dataPacket.getFlowData().setThrottlePosition(actualPacketString.substring(132, 136));
		dataPacket.getFlowData().setEngineDuty(actualPacketString.substring(136, 140));
		dataPacket.getFlowData().setIntakeAirFlow(actualPacketString.substring(140, 144));
		dataPacket.getFlowData().setIntakeAirTemperature(actualPacketString.substring(144, 148));
		dataPacket.getFlowData().setIntakeAirTemperature(actualPacketString.substring(148, 152));
		dataPacket.getFlowData().setBatteryVoltage(actualPacketString.substring(152, 156));
		dataPacket.getFlowData().setFLIFuelLevelInput(actualPacketString.substring(156, 160));
		dataPacket.getFlowData().setDistanceSinceDTCClear(actualPacketString.substring(160, 164));
		dataPacket.getFlowData().setMalFuncIndiLamp(actualPacketString.substring(164, 168));
		dataPacket.getFlowData().setHistoricalTotalMileage(actualPacketString.substring(168, 176));
		dataPacket.getFlowData().setHistoricalTotalFuelConsumption(actualPacketString.substring(176, 184));
		dataPacket.getFlowData().setHistoricalTotalDrivingTime(actualPacketString.substring(184, 192));
		dataPacket.getFlowData().setReservedData(actualPacketString.substring(192, 202));
	}

	private void processVehicleVINData(String actualPacketString, OBDDataPacket dataPacket) {
		dataPacket.initFlowData();
		logger.info("Reading Vehicle VIN Data !");
		dataPacket.getFlowData().setUTC_Time(actualPacketString.substring(62, 70));
		dataPacket.setVehVIN_CommProtocol(actualPacketString.substring(70, 72));
		dataPacket.setVehVIN_VINLength (actualPacketString.substring(72, 74));
		dataPacket.setVehVIN_VINCodeHexStr(actualPacketString.substring(74, 74 +( dataPacket.getVehVIN_Length_int()*2) ));
		logger.info("VIN Code is {}",dataPacket.getVehVIN_VINCode());
	}

	private void processGPSData(String actualPacketString, OBDDataPacket dataPacket) {
		dataPacket.initFlowData();
		String GPSStrHex = actualPacketString.substring(64, 112);
		logger.info("Reading GPS Data ! from {}",GPSStrHex);
		dataPacket.getFlowData().setGPSposition_str(GPSStrHex);
		logger.info("GPS Position [Lat /Long] {} / {}",dataPacket.getFlowData().getVehGPS_Data().getLatLong().getLatitude()
														, dataPacket.getFlowData().getVehGPS_Data().getLatLong().getLongitude());		
	}	
	
	private void processTripSummaryData(String actualPacketString, OBDDataPacket dataPacket) {
		logger.info("Reading Vehicle TripSummary Data !!!!!!!!!!!!!!");
		dataPacket.initTripSummaryData();
		dataPacket.getTripSummary().setIgnition_ONTime_str(actualPacketString.substring(62, 70));
		dataPacket.getTripSummary().setIgnition_ONGPSLocation_str(actualPacketString.substring(70, 118));

		dataPacket.getTripSummary().setIgnition_OFFTime_str(actualPacketString.substring(118, 126));
		dataPacket.getTripSummary().setIgnition_OFFGPSLocation_str(actualPacketString.substring(126, 174));		
		dataPacket.getTripSummary().setDrivingDistance_str(actualPacketString.substring(174, 180));
		dataPacket.getTripSummary().setFuelConsumption_str(actualPacketString.substring(180, 186));
		dataPacket.getTripSummary().setMaximumSpeed_str(actualPacketString.substring(186, 190));
		dataPacket.getTripSummary().setIdleTime_str(actualPacketString.substring(190, 194));
		dataPacket.getTripSummary().setIdleFuelConsumption_str(actualPacketString.substring(194, 198));
		
		dataPacket.getTripSummary().setNumRapidAcceleration_str(actualPacketString.substring(198, 200));
		dataPacket.getTripSummary().setNumRapidDeceleration_str(actualPacketString.substring(200, 202));
		dataPacket.getTripSummary().setNumRapidSharpTurns_str(actualPacketString.substring(202, 204));

		dataPacket.getTripSummary().setHistoricalTotalMileage_str(actualPacketString.substring(204, 212));
		dataPacket.getTripSummary().setHistoricalTotalFuelConsumption_str(actualPacketString.substring(212, 220));
		dataPacket.getTripSummary().setHistoricalTotalDrivingTime_str(actualPacketString.substring(220, 228));
		dataPacket.setSkipSendingToServer(dataPacket.getTripSummary().getDrivingDistance_int() <= 0);
	}
	
	private void processIgnitionONStatusData(String actualPacketString, OBDDataPacket dataPacket) {
		logger.info("Reading Ignitioan ON Status Data !!!!!!!!!!!!!!");
		dataPacket.initTripSummaryData();
		dataPacket.getTripSummary().setIgnition_ONTime_str(actualPacketString.substring(64, 72));
		dataPacket.getTripSummary().setIgnition_ONGPSLocation_str(actualPacketString.substring(72, 120));			
	}
	
	
	private void processIgnitionOFFStatusData(String actualPacketString, OBDDataPacket dataPacket) {
		logger.info("Reading Ignitioan OFF Status Data !!!!!!!!!!!!!!");
		dataPacket.initTripSummaryData();
		dataPacket.getTripSummary().setIgnition_OFFTime_str(actualPacketString.substring(64, 72));
		dataPacket.getTripSummary().setIgnition_OFFGPSLocation_str(actualPacketString.substring(72, 120));		
		
	}
	
	private void processDTCErrorData(String actualPacketString, OBDDataPacket dataPacket) {
		logger.info("Reading DTC Error data !!!!!!!!!!!!!!: " + actualPacketString);
		dataPacket.initDTCErrorData();
		dataPacket.getDTCErrorData().setDTCErrorOccuranceTime_str(actualPacketString.substring(62,70));
		dataPacket.getDTCErrorData().setDTCErrorCode_str(actualPacketString.substring(70));
	}
	
	private void processDongleStateSleepMode(String actualPacketString, OBDDataPacket dataPacket) {
		logger.info("Reading OOBD Dongle Sleep State data !!!!!!!!!!!!!!");
		dataPacket.initObdDongleStateData();
		dataPacket.getObdDongleStateData().setstate_SleepTime(actualPacketString.substring(62,70));
		dataPacket.getObdDongleStateData().setState_SleepVoltage(actualPacketString.substring(70,74));
		dataPacket.getObdDongleStateData().setState_SleepGPS(actualPacketString.substring(74,122));
	}
	
	private void processDongleStateWakeupMode(String actualPacketString, OBDDataPacket dataPacket) {
		logger.info("Reading OOBD Dongle Wake up State data !!!!!!!!!!!!!!");
		dataPacket.initObdDongleStateData();
		dataPacket.getObdDongleStateData().setState_WakeUpTime_str(actualPacketString.substring(62,70));
		dataPacket.getObdDongleStateData().setState_WakeUpVoltage(actualPacketString.substring(70,74));
		dataPacket.getObdDongleStateData().setState_WakeUpType(actualPacketString.substring(74,76));
		dataPacket.getObdDongleStateData().setState_WakeUpGPS(actualPacketString.substring(76,124));
	}	
	
	private void processDongleStateCantLocateMode(String actualPacketString, OBDDataPacket dataPacket) {
		logger.info("Reading OOBD Dongle Can't locate for long State data !!!!!!!!!!!!!!");
		dataPacket.initObdDongleStateData();
		dataPacket.getObdDongleStateData().setState_CantLocateForLongTime(actualPacketString.substring(62,70));
		dataPacket.getObdDongleStateData().setState_CantLocateLastGPS(actualPacketString.substring(70,118));
	}
	
	private void processDongleStatePowerOnAfterRebootMode(String actualPacketString, OBDDataPacket dataPacket) {
		logger.info("Reading OOBD Dongle Power ON After Reboot State data !!!!!!!!!!!!!!");
		dataPacket.initObdDongleStateData();
		dataPacket.getObdDongleStateData().setState_PowerONAfterRebootTime_str(actualPacketString.substring(62,70));
		dataPacket.getObdDongleStateData().setState_PowerDownLastTime_str(actualPacketString.substring(70,78));
		dataPacket.getObdDongleStateData().setState_PowerONAfterRebootType(actualPacketString.substring(78,80));
	}	

	private void processDongleStateUpgradeCompleteMode(String actualPacketString, OBDDataPacket dataPacket) {
		logger.info("Reading OOBD Dongle Upgrade Complete State data !!!!!!!!!!!!!!");
		dataPacket.initObdDongleStateData();
		dataPacket.getObdDongleStateData().setState_UpgradeCompleteTime(actualPacketString.substring(62,70));
		dataPacket.getObdDongleStateData().setState_UpgradeStatus(actualPacketString.substring(70,72));
	}		
	
	private void processSIMWiFiInfoData(String actualPacketString, OBDDataPacket dataPacket) {
		logger.info("Reading OOB Device Info Data such as IMSI, Phone Number etc.");
		dataPacket.initSimWifiInfoData();
		dataPacket.getSimWifiInfo().setInfoReadTime_str(actualPacketString.substring(62,70));
		int startPos = 70, endPos = startPos + 2;
		if(endPos > startPos) dataPacket.getSimWifiInfo().setPhoneNumberLength(actualPacketString.substring(startPos, endPos));
		startPos = endPos; endPos = startPos + ( dataPacket.getSimWifiInfo().getPhoneNumberLength()*2);
		if(endPos > startPos) dataPacket.getSimWifiInfo().setSimPhoneNumber(actualPacketString.substring(startPos, endPos ));
		
		startPos = endPos; endPos = startPos + 2;
		if(endPos > startPos) dataPacket.getSimWifiInfo().setIccidLength(actualPacketString.substring(startPos, endPos));
		startPos = endPos; endPos = startPos + ( dataPacket.getSimWifiInfo().getIccidLength()*2);
		if(endPos > startPos) dataPacket.getSimWifiInfo().setSimICCIDNumber(actualPacketString.substring(startPos, endPos ));		

		startPos = endPos; endPos = startPos + 2;
		if(endPos > startPos) dataPacket.getSimWifiInfo().setImsiLength(actualPacketString.substring(startPos, endPos));
		startPos = endPos; endPos = startPos + ( dataPacket.getSimWifiInfo().getImsiLength()*2);
		if(endPos > startPos) dataPacket.getSimWifiInfo().setSimIMSINumber(actualPacketString.substring(startPos, endPos ));

		startPos = endPos; endPos = startPos + 2;
		if(endPos > startPos) dataPacket.getSimWifiInfo().setWifiMACidLength(actualPacketString.substring(startPos, endPos));
		startPos = endPos; endPos = startPos + ( dataPacket.getSimWifiInfo().getWifiMACidLength()*2);
		if(endPos > startPos) dataPacket.getSimWifiInfo().setWifiMACid(actualPacketString.substring(startPos, endPos ));		

		startPos = endPos; endPos = startPos + 2;
		if(endPos > startPos) dataPacket.getSimWifiInfo().setBluetoothMACidLength(actualPacketString.substring(startPos, endPos));
		startPos = endPos; endPos = startPos + ( dataPacket.getSimWifiInfo().getBluetoothMACidLength()*2);
		if(endPos > startPos) dataPacket.getSimWifiInfo().setBluetoothMACid(actualPacketString.substring(startPos, endPos ));			
		
	}
	
	private void processVehicleAlertBatteryVoltage(String actualPacketString, OBDDataPacket dataPacket) {
		logger.info("Reading Ignitioan ON Status Data !!!!!!!!!!!!!!");
		dataPacket.initVehicleAlertData();
		dataPacket.getVehicleAlertData().setAlert_BatteryVoltageTime_str(actualPacketString.substring(62,70));
		dataPacket.getVehicleAlertData().setAlert_BatteryVoltageGPS_str(actualPacketString.substring(70,118));
		dataPacket.getVehicleAlertData().setAlert_BatteryVoltage_str(actualPacketString.substring(118,122));	
	}
	
	private void processVehicleAlertVibrationAfterIgnitionOff(String actualPacketString, OBDDataPacket dataPacket) {
		logger.info("Reading Ignitioan ON Status Data !!!!!!!!!!!!!!");
		dataPacket.initVehicleAlertData();
		dataPacket.getVehicleAlertData().setAlert_VibrationAfterIngnitionOFFTime_str(actualPacketString.substring(62,70));
		dataPacket.getVehicleAlertData().setAlert_VibrationAfterIgnitionOnGPS_str(actualPacketString.substring(70,118));
		dataPacket.getVehicleAlertData().setAlert_VibrationAfterIgnitionOFF_str(actualPacketString.substring(118,122));	
	}	
	
	private void processVehicleAlertSuspectedCollision(String actualPacketString, OBDDataPacket dataPacket) {
		logger.info("Reading Ignitioan ON Status Data !!!!!!!!!!!!!!");
		dataPacket.initVehicleAlertData();
		dataPacket.getVehicleAlertData().setAlert_SuspectedCollisionTime_str(actualPacketString.substring(62,70));
		dataPacket.getVehicleAlertData().setAlert_SuspectedCollionGPS_str(actualPacketString.substring(70,118));
		dataPacket.getVehicleAlertData().setAlert_SuspectedCollision_str(actualPacketString.substring(118,120));	
	}	
	private void processVehicleAlertDevicePulledOut(String actualPacketString, OBDDataPacket dataPacket) {
		logger.info("Reading Ignitioan ON Status Data !!!!!!!!!!!!!!");
		dataPacket.initVehicleAlertData();
		dataPacket.getVehicleAlertData().setAlert_DevicePulledOutTime_str(actualPacketString.substring(62,70));
		dataPacket.getVehicleAlertData().setAlert_DevicePulledOutGPS_str(actualPacketString.substring(70,118));
		dataPacket.getVehicleAlertData().setAlert_DevicePulledoutState_str(actualPacketString.substring(118,120));	
	}	
	
	private void sendPingResponse(OBDDataPacket dataPacket, Socket s) throws DecoderException {
		logger.info("Sending Ping Response ");

		ConnectionAck ack = new ConnectionAck();
		ack.setHeader("5555");
		ack.setDataLength("002A");
		ack.setVersion(dataPacket.getProtocolVersion());
		ack.setEncryptionMethod(dataPacket.getEncryptionMethod());
		ack.setReserved("0000");
		ack.setDeviceId(dataPacket.getDeviceId());
		ack.setFrameType("0D");
		ack.setFrameNumber(dataPacket.getFrameNumber());
		ack.setEffectiveDataLength("0000");
		String frameHeader = ack.getDataLength() + ack.getVersion() + ack.getEncryptionMethod() + ack.getReserved()
				+ ack.getDeviceId();
		String frameData = ack.getFrameType() + ack.getFrameNumber() + ack.getEffectiveDataLength();

		byte[] ackBytes = Hex.decodeHex((frameHeader + frameData).toCharArray());

		Checksum checksum = new Adler32();
		checksum.update(ackBytes, 0, ackBytes.length);

		byte[] checksumBytes = ByteBuffer.allocate(4).putInt((int) checksum.getValue()).array();

		String checksumString = Hex.encodeHexString(checksumBytes);

		String ackPacket = frameData + checksumString+"ffffffffffffff";
		logger.info("Ack packet "+ackPacket);
		
		String mcuKey = MCUKeyManager.getInstance().getMCUKeyForIMEINumber(dataPacket.getDeviceId());		

		String encryptedPacket = Hex.encodeHexString(encrypt(Hex.decodeHex(ackPacket.toCharArray()), Hex.decodeHex(mcuKey.toCharArray())));

		String finalAckPacket = ack.getHeader() + frameHeader + encryptedPacket + "AAAA";
		logger.info("PING Ack packet " + finalAckPacket);

		try {
			DataOutputStream dout = new DataOutputStream(s.getOutputStream());
			dout.write(Hex.decodeHex(finalAckPacket.toCharArray()));
			dout.flush();
		} catch (DecoderException e) {
			logger.error(e.toString());

		} catch (IOException e) {
			logger.error(e.toString());

		}

	}

}
