package com.thingworx.sdk.simplething;

import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.concurrent.TimeoutException;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inw.adapter.DeviceCommand;
import com.inw.adapter.OBDDataPacket;
import com.thingworx.communications.client.ClientConfigurator;
import com.thingworx.communications.client.ConnectedThingClient;
import com.thingworx.communications.client.ConnectionException;
import com.thingworx.relationships.RelationshipTypes.ThingworxEntityTypes;
import com.thingworx.types.InfoTable;
import com.thingworx.types.collections.ValueCollection;
import com.thingworx.types.primitives.StringPrimitive;

public class ThingWorxClientForOBD extends ConnectedThingClient {

    private static final Logger LOG = LoggerFactory.getLogger(ThingWorxClientForOBD.class);

    Hashtable<String, VehicleThing> obdThings = new Hashtable<String, VehicleThing>();
    

 
    private ThingWorxClientForOBD(ClientConfigurator config) throws Exception {
        super(config);
        this.bindThing(DeviceCommandThing.getInstance(this));	

    }
    
//    private String getVINCodeFromIMEIMapping(String imeiNumberHex) {
//    	String imeiNumber = imeiNumberHex;
//		byte[] imeiBytes =  null;
//		try {
//			imeiBytes = Hex.decodeHex(imeiNumberHex);
//		} catch (DecoderException e) {
//			LOG.error(e.toString());
//		}    		
//		imeiNumber = new String(imeiBytes);
//		String vinNumber = null;
//		if(imeiToVINMappings.containsKey(imeiNumber)) {
//			vinNumber = imeiToVINMappings.get(imeiNumber);
//		}else
//		{
//			
//			LOG.error("VIN Number mapping to given IMEI Number {} Not Exist!",imeiNumber);
//		}
//    	return vinNumber;
//    }
    
    public void processOBDDataPacket(OBDDataPacket packet, Socket obdSock) {
    	
    	if( packet.isSkipSendingToServer()) {
        	LOG.info("Inside  processOBDDataPacket Packet is marked to skip sending to server {}", packet);
        	return;
    	}
    	LOG.info("Inside  processOBDDataPacket Device ID = {}", packet.getDeviceId());
    	

    	VehicleThing obdThing = getVehicleThing(packet);
    	
    	if(obdThing != null) {
    		
    		obdThing.initPropertyUpdate();
    		
    		if(packet.getMessageType().equals("03")) {
    			
    			processPublishMessage(packet, obdThing);
    			
    		}else if(packet.getMessageType().equals("01")) {
    			
    			try {
					updateOBDDeviceParameters(packet, obdThing);
	    			obdThing.updateSubscribedProperties(10000);
	    			this.InitiaiseSession(packet);
					
				}  catch (Exception e) {
					LOG.error("Error occured : ",e);
				}
    		}

			// if there is any commands to be sent to the OBD device, send it now.
			LOG.info(" For Device {} # of command pending is {}",obdThing.getName() , obdThing.getPendingCommands().size());
			//Maximum 10 of last queues command will be executed and remaining all will be purged.
			ArrayList<DeviceCommand> pendingCmds = obdThing.getPendingCommands();
			if(pendingCmds.size() > 0) {
				final Integer maxPermittedCommands = 4;
				if( pendingCmds.size() > maxPermittedCommands) {
					List<DeviceCommand> purgeList = pendingCmds.subList( 0,  pendingCmds.size() - maxPermittedCommands);
					pendingCmds.removeAll(purgeList);
				}
				DeviceCommand command = pendingCmds.get(0);
				command.executeCommand(obdSock);
				pendingCmds.remove(command);
				LOG.info(" For Device {} # of command pending after removing is {}",obdThing.getName() , obdThing.getPendingCommands().size());
			}
		} else {
			LOG.info("obdThing is null so skipping property update ");

    	}
    }

	private void processPublishMessage(OBDDataPacket packet, VehicleThing obdThing) {
		LOG.info("Starting property update for obdThing based on published data from OBD Dongle");

		try {
			boolean isDataModified = true;
			//Vehicle Data
			if(packet.getDataTypeMajor().equals("03") ) {
				//Flow Data
				if(packet.getDataTypeMinor().equals("01")){
				
					updateVehicleFlowParameters(packet, obdThing);
				}
				// VIN Data
				else if( packet.getDataTypeMinor().equals("00") ) {
					
					updateVehicleVINParameters(packet, obdThing);
				}
			}
			// GPS Data
			else if(packet.getDataTypeMajor().equals("02")){
				
				if(packet.getDataTypeMinor().equals("00")) {
					updateVehicleGPSParameters(packet, obdThing);
				}
			}
			//Trip Data
			else if(packet.getDataTypeMajor().equals("00")) {
				//Trip summary
				if(packet.getDataTypeMinor().equals("00")) {
					
					isDataModified = this.updateTripSummaryParameters(packet, obdThing);
				}
				//Ignition ON Status update
				else if(packet.getDataTypeMinor().equals("01")) {
	    			this.InitiaiseSession(packet);
					this.updateIgnitionONStatusParameters(packet, obdThing);  
				}
				//Ignition OFF Status update
				else if(packet.getDataTypeMinor().equals("02")) {
					
					this.updateIgnitionOFFStatusParameters(packet, obdThing);
				}
			}
			// DTC Error Codes
			else if(packet.getDataTypeMajor().equals("05")) {
				//DTC Error Codes
				if(packet.getDataTypeMinor().equals("00")) {
					
					this.updateDTCErroeCodeParameters(packet, obdThing);
				}
				//Battery Voltage
				else if(packet.getDataTypeMinor().equals("01")) {
					
					this.updateVehAlertBatteryVoltageParameters(packet, obdThing);
				}
				//Vibration after ignition off
				else if(packet.getDataTypeMinor().equals("02")) {
					
					this.updateVehAlertVibrationAfterIgnitionOffParameters(packet, obdThing);
				} 
				
				//Suspected collision
				else if(packet.getDataTypeMinor().equals("05")) {
					
					this.updateVehAlertSuspectedCollisionParameters(packet, obdThing);
				}     				
				//Device Pulled out Alert
				else if(packet.getDataTypeMinor().equals("07")) {
					
					this.updateVehAlertDevicePulledOutParameters(packet, obdThing);  
				}
			}
			// Device Info
			else if(packet.getDataTypeMajor().equals("04")) {
				// Sim & Wifi Info
				if(packet.getDataTypeMinor().equalsIgnoreCase("0C")) {
					isDataModified = this.updateSimWiFiParameters(packet, obdThing); 
				}else {
					this.updateOBDDongleStateEventParameters(packet, obdThing);
				}
			}
			if (isDataModified) {
				obdThing.updateSubscribedProperties(10000);
			}
		} catch (Exception e) {
			LOG.error("Exception occured: ",e);
		}
	}

	private VehicleThing getVehicleThing(OBDDataPacket packet) {
		String imeiNumber = packet.getDeviceId();
    	//LOG.info("obdThings Size : {}", obdThings.size() );		
		VehicleThing obdThing = obdThings.get(imeiNumber);
    	if(obdThing == null) {
    		LOG.info("obdThing for the deviceID doesnt exist!, create new");

			try {

				String thingName = getVehicleThingNameFromServer(imeiNumber);
    			LOG.info("Creating a ClientSide Virtual Thing and Binding to remote Thing named : {}", thingName);
				obdThing = new VehicleThing(thingName, "A OBD Vehicle Thing", this, packet);
				obdThings.put(imeiNumber,obdThing);
				this.bindThing(obdThing);	
				
			} catch (Exception e) {
				LOG.error(e.toString());
			}
		}
		return obdThing;		
	}

	public VehicleThing getVehicleThingWithIMEI(String  imeiNumber) {
		String imeiHexString = null;
		try {
			imeiHexString = Hex.encodeHexString(imeiNumber.getBytes(StandardCharsets.US_ASCII));
		} catch (Exception e) {
			LOG.error("error occured ",e);
		}
		
		return obdThings.get(imeiHexString);
	}
	
	private void InitiaiseSession(OBDDataPacket packet){
		try {
			String imeiNumber = packet.getDeviceId();
			VehicleThing vehThing = this.getVehicleThingWithIMEI(imeiNumber);
			this.invokeService(ThingworxEntityTypes.Things, vehThing.getName(), 
			        "InitiaiseSession", null, 5000);			
		} catch (Exception e) {
			LOG.error("error occured ",e);
		}
	}
	
	private void updateVehicleFlowParameters(OBDDataPacket packet, VehicleThing obdThing)
			throws Exception, TimeoutException, ConnectionException {
		
		obdThing.setNumericPropertyValue("DistanceTravelledAfterDTC"
				, (double)packet.getFlowData().getDistanceSinceDTCClear_int());
		
		obdThing.setNumericPropertyValue("VehicleDataRPM"
				, (double)packet.getFlowData().getRPM_int());		
		
		obdThing.setNumericPropertyValue("VehicleBatteryVoltage"
				, (double)packet.getFlowData().getBatteryVoltage_float());			

		obdThing.setNumericPropertyValue("VehicleEngineCoolantTemperature"
				, (double)packet.getFlowData().getEngineCoolantTemperature_int());	
		
		obdThing.setNumericPropertyValue("VehicleFuelLevelInput"
				, (double)packet.getFlowData().getFLIFuelLevelInput_float());
		
		obdThing.setNumericPropertyValue("VehicleSpeed"
				, (double)packet.getFlowData().getSpeed_int());	
		
		obdThing.setNumericPropertyValue("VehicleTotalMileage"
				, (double)packet.getFlowData().getHistoricalTotalMileage_int());
		
		obdThing.setLocationPropertyValue("VehicleDataGPS"
				, packet.getFlowData().getVehGPS_Data().getLatLong());
		
		obdThing.setNumericPropertyValue("VehicleDirectionAngle"
				, (double)packet.getFlowData().getVehGPS_Data().getHeadingAngle());		
		
	}
	
	private void updateOBDDeviceParameters(OBDDataPacket packet, VehicleThing obdThing)
			throws Exception, TimeoutException, ConnectionException {
		
		obdThing.setStringPropertyValue("ProtocolVersion"
				, packet.getOBDDeviceData().getProtocolVersion());		
		obdThing.setStringPropertyValue("HardwareVersion"
				, packet.getOBDDeviceData().getHardWareVersion());
		obdThing.setStringPropertyValue("SoftwareVersionMCU"
				,packet.getOBDDeviceData().getSoftWareVersionMCU());		
		obdThing.setStringPropertyValue("SoftwareVersionModem"
				, packet.getOBDDeviceData().getSoftWareVersionModem());

	}	

	private boolean updateSimWiFiParameters(OBDDataPacket packet, VehicleThing obdThing)
			throws Exception, TimeoutException, ConnectionException {
		
		obdThing.setStringPropertyValue("simPhoneNumber"
				, packet.getSimWifiInfo().getSimPhoneNumber());		
		obdThing.setStringPropertyValue("simICCIDNumber"
				, packet.getSimWifiInfo().getSimICCIDNumber());
		obdThing.setStringPropertyValue("simIMSINumber"
				,packet.getSimWifiInfo().getSimIMSINumber());		
		obdThing.setStringPropertyValue("wifiMACid"
				, packet.getSimWifiInfo().getWifiMACid());		
		obdThing.setStringPropertyValue("bluetoothMACid"
				, packet.getSimWifiInfo().getBluetoothMACid());
		obdThing.updateSubscribedProperties(10000);
		this.InitiaiseSession(packet);
		return false;
	}	
	
	private void updateOBDDongleStateEventParameters(OBDDataPacket packet, VehicleThing obdThing)
			throws Exception, TimeoutException, ConnectionException {
		if(packet.getDataTypeMinor().equals("02")) {
			obdThing.setDateTimePropertyValue("State_SleepTime" , packet.getObdDongleStateData().getState_SleepTime());	
			obdThing.setLocationPropertyValue("State_SleepGPS" , packet.getObdDongleStateData().getState_SleepGPS().getLatLong());
			obdThing.setNumericPropertyValue("State_SleepVoltage" , packet.getObdDongleStateData().getState_SleepVoltage());
		}else if(packet.getDataTypeMinor().equals("03")) {
			obdThing.setDateTimePropertyValue("State_WakeUpTime" , packet.getObdDongleStateData().getState_WakeUpTime());	
			obdThing.setLocationPropertyValue("State_WakeUpGPS" , packet.getObdDongleStateData().getState_WakeUpGPS().getLatLong());
			obdThing.setNumericPropertyValue("State_WakeUpVoltage" , packet.getObdDongleStateData().getState_WakeUpVoltage());
			obdThing.setIntegerPropertyValue("State_WakeUpType" , packet.getObdDongleStateData().getState_WakeUpType());
		}else if(packet.getDataTypeMinor().equals("04")) {
			obdThing.setDateTimePropertyValue("State_CantLocateForLongTime" , packet.getObdDongleStateData().getState_CantLocateForLongTime());	
			obdThing.setLocationPropertyValue("State_CantLocateLastGPS" , packet.getObdDongleStateData().getState_CantLocateLastGPS().getLatLong());
		}else if(packet.getDataTypeMinor().equals("05")) {
			obdThing.setDateTimePropertyValue("State_PowerONAfterRebootTime" , packet.getObdDongleStateData().getState_PowerONAfterRebootTime());	
			obdThing.setDateTimePropertyValue("State_PowerDownLastTime" , packet.getObdDongleStateData().getState_State_PowerDownLastTime());	
			obdThing.setIntegerPropertyValue("State_PowerONAfterRebootType" , packet.getObdDongleStateData().getState_PowerONAfterRebootType());			
		}else if(packet.getDataTypeMinor().equals("06")) {
			obdThing.setDateTimePropertyValue("State_UpgradeCompleteTime" , packet.getObdDongleStateData().getState_UpgradeCompleteTime());	
			obdThing.setIntegerPropertyValue("State_UpgradeStatus" , packet.getObdDongleStateData().getState_UpgradeStatus());			
		}
	}
	
	private void updateVehicleGPSParameters(OBDDataPacket packet, VehicleThing obdThing)
			throws Exception, TimeoutException, ConnectionException {
		
		obdThing.setLocationPropertyValue("VehicleDataGPS"
				, packet.getFlowData().getVehGPS_Data().getLatLong());

	}	

	private void updateVehicleVINParameters(OBDDataPacket packet, VehicleThing obdThing)
			throws Exception, TimeoutException, ConnectionException {

		obdThing.setStringPropertyValue("VIN", packet.getVehVIN_VINCode());
	}	
	
	private boolean updateTripSummaryParameters(OBDDataPacket packet, VehicleThing obdThing)
			throws Exception, TimeoutException, ConnectionException {
		// In many cases OBD Dongle report the same Trip summary many times. Check the last reported HistoricalTotalMileage
		// To check if this record is duplicated and avoid duplicating this entry in  ThingWorx DB
		int curMileage  = 0;
		try {
			//First try to ready locally cached Mileage value if that fails, try to read it from server.
			curMileage  = obdThing.getCurHistoricMileage();
			if(curMileage == 0 ) {
				InfoTable infoT = this.readProperty(ThingworxEntityTypes.Things, obdThing.getName(), "HistoricalTotalMileage", 10000);
				curMileage = ((Double)infoT.getFirstRow().getValue("HistoricalTotalMileage")).intValue() ;
				//String srvrMileage = obdThing.getCurrentPropertyValue("HistoricalTotalMileage").getStringValue();
				LOG.info("Server logged HistoricalTotalMileage of {} is {}",obdThing.getName(), curMileage);
				//curMileage  = (int)Double.parseDouble(srvrMileage);
			}
		}catch(Exception e) {
			LOG.error("Error reading curent HistoricalTotalMileage of {} from server :",obdThing.getName(), e);
		}
		boolean isDataModified = false;
		if (curMileage != packet.getTripSummary().getHistoricaTotalMileage_int()) {
			
			
			obdThing.setCurHistoricMileage((int)packet.getTripSummary().getHistoricaTotalMileage_int());
			
			obdThing.setNumericPropertyValue("HistoricalTotalMileage"
					, (double)packet.getTripSummary().getHistoricaTotalMileage_int());
			
			obdThing.setNumericPropertyValue("DrivingDistance"
					, (double)packet.getTripSummary().getDrivingDistance_int());			
			
			obdThing.setNumericPropertyValue("DrivingFuelConsumption"
					, (double)packet.getTripSummary().getFueleConsumption_int());	
			
			obdThing.setNumericPropertyValue("IdleTime"
					, (double)packet.getTripSummary().getIdleTime_int());
			
			obdThing.setLocationPropertyValue("LocationWhenIgnitionOFF"
					, packet.getTripSummary().getIgn_OFF_GPSData().getLatLong());
			
			obdThing.setLocationPropertyValue("LocationWhenIgnitionON"
					, packet.getTripSummary().getIgn_ON_GPSData().getLatLong());
			
			obdThing.setDateTimePropertyValue("TripIgnitionOnTime"
					,obdThing.getIgnitionOnTime());
					//, packet.getTripSummary().getIgnition_ON_Time_dt());	
			
			obdThing.setDateTimePropertyValue("TripIgnitionOffTime"
					,obdThing.getIgnitionOffTime());
					//, packet.getTripSummary().getIgnition_OFF_Time_dt());
			
			obdThing.setNumericPropertyValue("MaximumSpeed"
					, (double)packet.getTripSummary().getMaximumSpeed_int());
			
			obdThing.setNumericPropertyValue("NumberOfRapidAcceleration"
					, (double)packet.getTripSummary().getNumRapidAccelerations_int());			
			obdThing.setNumericPropertyValue("NumberOfRapidDeceleration"
					, (double)packet.getTripSummary().getNumRapidDecelerations_int());			
			obdThing.setNumericPropertyValue("NumberOfSharpTurns"
					, (double)packet.getTripSummary().getNumRapidSharpTurns_int());			
			
			isDataModified =  true;
		}else {
			LOG.error(" HistoricalTotalMileage of Device {}  is  {} which == previously recorded {} so skipping this entry :"
					, obdThing.getName()
					, packet.getTripSummary().getHistoricaTotalMileage_int()
					, curMileage);

		}
		return isDataModified;
	}	
	
	private void updateIgnitionONStatusParameters(OBDDataPacket packet, VehicleThing obdThing)
			throws Exception, TimeoutException, ConnectionException {	
		
		//obdThing.setLocationPropertyValue("LocationWhenIgnitionON", packet.getTripSummary().getIgn_ON_GPSData().getLatLong() );	
				
		obdThing.setDateTimePropertyValue("IgnitionOnTime", packet.getTripSummary().getIgnition_ON_Time_dt() );
		obdThing.setIgnitionOnTime(packet.getTripSummary().getIgnition_ON_Time_dt());
				
	}
	
	private void updateIgnitionOFFStatusParameters(OBDDataPacket packet, VehicleThing obdThing)
			throws Exception, TimeoutException, ConnectionException {
	
		//obdThing.setLocationPropertyValue("LocationWhenIgnitionOFF"
		//		, packet.getTripSummary().getIgn_OFF_GPSData().getLatLong() );	
		obdThing.setDateTimePropertyValue("IgnitionOffTime", packet.getTripSummary().getIgnition_OFF_Time_dt() );
		obdThing.setIgnitionOffTime(packet.getTripSummary().getIgnition_OFF_Time_dt());
	}
	
	
	private void updateDTCErroeCodeParameters(OBDDataPacket packet, VehicleThing obdThing)
			throws Exception, TimeoutException, ConnectionException {	

		obdThing.setInfoTablePropertyValue("DTCErrorData", packet.getDTCErrorData().getDTCErroCodeInfoTable() );								
		obdThing.setDateTimePropertyValue("DTCErrorTime",packet.getDTCErrorData().getDTCErroCodeTime());	
	}
	
	
	private void updateVehAlertBatteryVoltageParameters(OBDDataPacket packet, VehicleThing obdThing)
			throws Exception, TimeoutException, ConnectionException {	
		
		obdThing.setLocationPropertyValue("Alert_BatteryVoltageGPS"
				, packet.getVehicleAlertData().getAlert_BatteryVoltageGPS().getLatLong());	
		
		obdThing.setDateTimePropertyValue("Alert_BatteryVoltageTime"
				, packet.getVehicleAlertData().getAlert_BatteryVoltageTime());	
				
		obdThing.setNumericPropertyValue("DrivingDistance"
				, packet.getVehicleAlertData().getAlert_BatteryVoltage());	
	}
	
	
	private void updateVehAlertDevicePulledOutParameters(OBDDataPacket packet, VehicleThing obdThing)
			throws Exception, TimeoutException, ConnectionException {	
			
		obdThing.setLocationPropertyValue("Alert_DevicePulledOutGPS"
				, packet.getVehicleAlertData().getAlert_DevicePulledOutGPS().getLatLong() );	
					
		obdThing.setDateTimePropertyValue("Alert_DevicePulledOutTime"
				, packet.getVehicleAlertData().getAlert_DevicePulledOutTime());	
					
		obdThing.setNumericPropertyValue("Alert_DevicePulledoutState"
				, packet.getVehicleAlertData().getAlert_DevicePulledoutState() );	
	}	
	
	private void updateVehAlertVibrationAfterIgnitionOffParameters(OBDDataPacket packet, VehicleThing obdThing)
			throws Exception, TimeoutException, ConnectionException {	
				
		obdThing.setLocationPropertyValue("Alert_VibrationAfterIgnitionOnGPS"
				, packet.getVehicleAlertData().getAlert_VibrationAfterIgnitionOnGPS().getLatLong() );	
					
		obdThing.setDateTimePropertyValue("Alert_VibrationAfterIngnitionOFFTime"
				, packet.getVehicleAlertData().getAlert_VibrationAfterIngnitionOFFTime());	
				
		obdThing.setNumericPropertyValue("Alert_VibrationAfterIngnitionOFF"
				,packet.getVehicleAlertData().getAlert_VibrationAfterIgnitionOFF() );	
	}		
	
	private void updateVehAlertSuspectedCollisionParameters(OBDDataPacket packet, VehicleThing obdThing)
			throws Exception, TimeoutException, ConnectionException {	
			
		obdThing.setLocationPropertyValue("Alert_SuspectedCollionGPS"
				, packet.getVehicleAlertData().getAlert_SuspectedCollionGPS().getLatLong());	
					
		obdThing.setDateTimePropertyValue("Alert_SuspectedCollisionTime"
				, packet.getVehicleAlertData().getAlert_SuspectedCollisionTime());	
			
		obdThing.setNumericPropertyValue("Alert_SuspectedCollision"
				, packet.getVehicleAlertData().getAlert_SuspectedCollision());	
	}		
	
     // static variable single_instance of type Singleton 
     private static ThingWorxClientForOBD single_instance = null; 
    
    
     public static void initClient(String twxServerURL, String appKey) {
    	
   	 if (single_instance == null) {    	 

	        ClientConfigurator config = new ClientConfigurator();
	        
	        LOG.info("ThingWorxClient connecting to TWX server {}", twxServerURL);
	
	        // Set the URI of the server that we are going to connect to
	        config.setUri(twxServerURL);
	
	        // Set the ApplicationKey. This will allow the client to authenticate with the server.
	        // It will also dictate what the client is authorized to do once connected.
	        //config.setAppKey("6a799081-4685-4b98-8778-311a1a5a4517");
	        config.setAppKey(appKey);
	        // This will allow us to test against a server using a self-signed certificate.
	        // This should be removed for production systems.
	        config.ignoreSSLErrors(true); // All self signed certs
	        ThingWorxClientForOBD[] clients = {null}; //Made this an array to by pass an compile error relating to final since its used inside thread.
	
	        try {
	
	            // Create our client.
	        	clients[0] = new ThingWorxClientForOBD(config);
	
	            // Start the client. The client will connect to the server and authenticate
	            // using the ApplicationKey specified above.
	        	clients[0].start();
	
	            // Wait for the client to connect.
	            if (clients[0].waitForConnection(30000)) {
	
	                LOG.info("The client is now connected.");
	
	
	                Thread newThread = new Thread(() ->{
		                // This will prevent the main thread from exiting. It will be up to another thread
		                // of execution to call client.shutdown(), allowing this main thread to exit.
		                while (!clients[0].isShutdown()) {
		
		                    try {
								Thread.sleep(1500);
							} catch (InterruptedException e) {
								LOG.error("Exception occured: ",e);
							}
		
		                }
		                LOG.info("ThingWorxClientForOBD Thread loop is Exiting !!");
	                });
	                newThread.start();
	
	            } else {
	                // Log this as a warning. In production the application could continue
	                // to execute, and the client would attempt to reconnect periodically.
	                LOG.warn("Client did not connect within 30 seconds. Exiting");
	            }
	
	        } catch (Exception e) {
	            LOG.error("An exception occurred during execution.", e);
	        }
	        single_instance = clients[0];
 	 }    	
    }
	
    public static ThingWorxClientForOBD getInstance() throws Exception {
    	 
    if(single_instance == null) {
    	throw new Exception("ThingWorx client is not initialized properly !!!");
    }

      return single_instance;  
    }
     
     public String getMCUKeyFromServer(String IMEINumber) {

    	 String mcuKey = null;
         // A ValueCollection is used to specify a service's parameters
         ValueCollection params = new ValueCollection();

         params.put("IMEI", new StringPrimitive(IMEINumber));

		 try {
	         InfoTable result = this.invokeService(ThingworxEntityTypes.Things, "VT.Services.T",
			         "GetMcuFromImei", params, 5000);
	         // The rows of an InfoTable are ValueCollections.
	         ValueCollection row = result.getFirstRow();
	         mcuKey = row.getStringValue("result");
	
		} catch (Exception e) {
			LOG.error("Exception occured: ",e);
		}
		return mcuKey;
     }
     
     public String getVehicleThingNameFromServer(String deviceIDHex) {

     	String imeiNumber = deviceIDHex;
 		byte[] imeiBytes =  null;
 		try {
 			imeiBytes = Hex.decodeHex(deviceIDHex.toCharArray());
 		} catch (DecoderException e) {
 			LOG.error(e.toString());
 		}    		
 		imeiNumber = new String(imeiBytes);         // A ValueCollection is used to specify a service's parameters
        ValueCollection params = new ValueCollection();
        params.put("IMEI", new StringPrimitive(imeiNumber));
        String thingName = null;
		try {
	         InfoTable result = this.invokeService(ThingworxEntityTypes.Things, "VT.Services.T",
			         "GetThingNameFromImei", params, 5000);
	         // The rows of an InfoTable are ValueCollections.
	         ValueCollection row = result.getFirstRow();
	         thingName = row.getStringValue("result");
	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return thingName;
     }     
     
}
