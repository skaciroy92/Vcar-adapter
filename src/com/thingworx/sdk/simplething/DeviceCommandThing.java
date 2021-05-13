package com.thingworx.sdk.simplething;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inw.adapter.DeviceCommand;
import com.thingworx.communications.client.things.VirtualThing;
import com.thingworx.metadata.PropertyDefinition;
import com.thingworx.metadata.annotations.ThingworxPropertyDefinitions;
import com.thingworx.metadata.annotations.ThingworxServiceDefinition;
import com.thingworx.metadata.annotations.ThingworxServiceParameter;
import com.thingworx.metadata.annotations.ThingworxServiceResult;
import com.thingworx.types.primitives.IPrimitiveType;

@ThingworxPropertyDefinitions(properties = {

         
        })

/**
 * A very basic VirtualThing with two properties and a service implementation. It also implements
 * processScanRequest to handle periodic actions.
 */
public class DeviceCommandThing extends VirtualThing {

    private static final Logger LOG = LoggerFactory.getLogger(DeviceCommandThing.class);

    
    // static variable single_instance of type Singleton 
    private static DeviceCommandThing single_instance = null; 
    private ThingWorxClientForOBD thingClient =null;
    /**
     * A custom constructor. We implement this so we can call initializeFromAnnotations, which
     * processes all of the VirtualThing's annotations and applies them to the object.
     * 
     * @param name The name of the thing.
     * @param description A description of the thing.
     * @param client The client that this thing is associated with.
     */
    private DeviceCommandThing( ThingWorxClientForOBD client)
            throws Exception {

        super("VT.DeviceCommand.T", "Remote thing to relay command coming form ThingWorx server to OBD DEvice", client);
        this.thingClient = client;
        this.initializeFromAnnotations();
        
    }
    
    
    // static method to create instance of Singleton class 
    public static DeviceCommandThing getInstance(ThingWorxClientForOBD client) 
    { 
        if (single_instance == null)
			try {
				single_instance = new DeviceCommandThing(client);
			} catch (Exception e) {
				LOG.error("Error occured {}", e);
			} 
  
        return single_instance; 
    }    

    /**
     * This method provides a common interface amongst VirtualThings for processing periodic
     * requests. It is an opportunity to access data sources, update property values, push new
     * values to the server, and take other actions.
     */
    @Override
    public void processScanRequest() {

        try {

            // Here we set the thing's internal property values to the new values
            // that we accessed above. This does not update the server. It simply
            // sets the new property value in memory.

            // This call evaluates all properties and determines if they should be pushed
            // to the server, based on their pushType aspect. A pushType of ALWAYS means the
            // property will always be sent to the server when this method is called. A
            // setting of VALUE means it will be pushed if has changed since the last
            // push. A setting of NEVER means it will never be pushed.
            //
            // Our Temperature property is set to ALWAYS, so its value will be pushed
            // every time processScanRequest is called. This allows the platform to get
            // periodic updates and store the time series data. Humidity is set to
            // VALUE, so it will only be pushed if it changed.
            this.updateSubscribedProperties(10000);

        } catch (Exception e) {
            // This will occur if we provide an unknown property name. We'll ignore
            // the exception in this case and just log it.
            LOG.error("Exception occurred while updating properties.", e);
        }
    }

    /**
     * This is where we handle property writes from the server. The only property we want to update
     * is the SetPoint. Temperature and Humidity write requests should be rejected, since their
     * values are controlled from within this application.
     * 
     * @see VirtualThing#processPropertyWrite(PropertyDefinition, IPrimitiveType)
     */
    @Override
    public void processPropertyWrite(PropertyDefinition property,
            @SuppressWarnings("rawtypes") IPrimitiveType value) throws Exception {

        // Find out which property is being updated
        String propName = property.getName();

        if (!"SetPoint".equals(propName)) {
            throw new Exception("The property " + propName + " is read only on the simple device.");
        }

        this.setPropertyValue(propName, value);
    }
            
    //This service can be called from TWX Server and this in-turn sends a command to OBD Device turn ON/OFF WiFi Hotspot
    @ThingworxServiceDefinition(name = "EnableWifiHotSpot", description = "Enable OBD Device WiFi HotSpot")
    @ThingworxServiceResult(name = "result", description = "True if Operation succeeded",
            baseType = "BOOLEAN")
    public Boolean EnableWifiHotSpot(
            @ThingworxServiceParameter(name = "isEnabled",
                    description = "True if WIFi HotSpot needs to be enabled/ False if needs to disable WiFihotspot",
                    baseType = "BOOLEAN") Boolean isEnabled
            ,@ThingworxServiceParameter(name = "IMEI",
            description = "IMEI Number of the OBD Dongle to which this command is to be sent",
            baseType = "STRING") String IMEI)
            throws Exception {
    	
        LOG.info("Queuing command to change WiFi Hotspot status of device {} to {}", IMEI, isEnabled);
        VehicleThing targetThing = this.thingClient.getVehicleThingWithIMEI(IMEI);
        if(targetThing != null) {
        	DeviceCommand newCommand = DeviceCommand.getWifiStatusSetCommand(isEnabled, targetThing.getSrcPacket(), targetThing.getNewFrameNumber());
        	targetThing.addDeviceCommandToPendingList(newCommand);
        } else {
        	LOG.warn("OBD Device appears offline, ignoring the remote command!");
        }
        return true;
    }
    
    
    //This service can be called from TWX Server and this in-turn sends a command to OBD Device to set SSID name of WiFi Hotspot
    @ThingworxServiceDefinition(name = "SetWifiSSID", description = "Sets SSID name of OBD Device WiFi HotSpot")
    @ThingworxServiceResult(name = "result", description = "True if Operation succeeded",
            baseType = "BOOLEAN")
    public Boolean SetWifiSSID(
            @ThingworxServiceParameter(name = "ssid",
                    description = "New SSID Name of WiFihotspot",
                    baseType = "STRING") String ssid
            ,@ThingworxServiceParameter(name = "IMEI",
            description = "IMEI Number of the OBD Dongle to which this command is to be sent",
            baseType = "STRING") String IMEI)
            throws Exception {
    	
        LOG.info("Queuing command to change WiFi SSID name of device {} to {}", IMEI, ssid);
        VehicleThing targetThing = this.thingClient.getVehicleThingWithIMEI(IMEI);
        if(targetThing != null) {
        	DeviceCommand newCommand = DeviceCommand.getWifiSSIDSetCommand(ssid, targetThing.getSrcPacket(), targetThing.getNewFrameNumber());
        	targetThing.addDeviceCommandToPendingList(newCommand);
        } else {
        	LOG.warn("OBD Device appears offline, ignoring the remote command!");
        }
        return true;
    }    
    
    //This service can be called from TWX Server and this in-turn sends a command to OBD Device to set password of WiFi Hotspot.
    @ThingworxServiceDefinition(name = "SetWifiPassword", description = "Sets Password of OBD Device WiFi HotSpot")
    @ThingworxServiceResult(name = "result", description = "True if Operation succeeded",
            baseType = "BOOLEAN")
    public Boolean SetWifiPassword(
            @ThingworxServiceParameter(name = "password",
                    description = "New Password of WiFihotspot",
                    baseType = "STRING") String password
            ,@ThingworxServiceParameter(name = "IMEI",
            description = "IMEI Number of the OBD Dongle to which this command is to be sent",
            baseType = "STRING") String IMEI)
            throws Exception {
    	
        LOG.info("Queuing command to change WiFi Password of device {} to {}", IMEI,password);
        VehicleThing targetThing = this.thingClient.getVehicleThingWithIMEI(IMEI);
        if(targetThing != null) {
        	DeviceCommand newCommand = DeviceCommand.getWifiPasswordSetCommand(password, targetThing.getSrcPacket(), targetThing.getNewFrameNumber());
        	targetThing.addDeviceCommandToPendingList(newCommand); 
        } else {
        	LOG.warn("OBD Device appears offline, ignoring the remote command!");
        }
        return true;
    }
    
    //This service can be called from TWX Server and this in-turn sends a command to OBD Device to set GPS Update frequency.
    @ThingworxServiceDefinition(name = "SetGPSUpdateFrequency", description = "Sets Password of OBD Device WiFi HotSpot")
    @ThingworxServiceResult(name = "result", description = "True if Operation succeeded",
            baseType = "BOOLEAN")
    public Boolean SetGPSUpdateFrequency(
            @ThingworxServiceParameter(name = "frequencyInseconds",
                    description = "GPS Update frequency in seconds",
                    baseType = "INTEGER") Integer frequencyInseconds
            ,@ThingworxServiceParameter(name = "IMEI",
            description = "IMEI Number of the OBD Dongle to which this command is to be sent",
            baseType = "STRING") String IMEI)
            throws Exception {
    	
        LOG.info("Queuing command to change GPS Update frequencyof device {} to {}", IMEI, frequencyInseconds);
        VehicleThing targetThing = this.thingClient.getVehicleThingWithIMEI(IMEI);
        if(targetThing != null) {        
        	//DeviceCommand newCommand = DeviceCommand.getGPSFrequencySetCommand(frequencyInseconds, targetThing.getSrcPacket(), targetThing.getNewFrameNumber());
        	//targetThing.addDeviceCommandToPendingList(newCommand);
        } else {
        	LOG.warn("OBD Device appears offline, ignoring the remote command!");
        }
        return true;
    }
    
    //This service can be called from TWX Server and this in-turn sends a command to OBD Device to send all DTC error codes from ECU
    @ThingworxServiceDefinition(name = "DetectVehicle", description = "Detect DTC Error Codes from Vehicle")
    @ThingworxServiceResult(name = "result", description = "True if Operation succeeded",
            baseType = "BOOLEAN")
    public Boolean DetectVehicle(
            @ThingworxServiceParameter(name = "IMEI",
            description = "IMEI Number of the OBD Dongle to which this command is to be sent",
            baseType = "STRING") String IMEI)
            throws Exception {
    	
        LOG.info("Queuing command ->Detect DTC erro codes of device {} ", IMEI);
        VehicleThing targetThing = this.thingClient.getVehicleThingWithIMEI(IMEI);
        if(targetThing != null) {
        	DeviceCommand newCommand = DeviceCommand.getVehicleDetectionCommand( targetThing.getSrcPacket(), targetThing.getNewFrameNumber());
        	targetThing.addDeviceCommandToPendingList(newCommand);
        } else {
        	LOG.warn("OBD Device appears offline, ignoring the remote command!");
        }
        return true;
    }    
    
    
    
    
}
