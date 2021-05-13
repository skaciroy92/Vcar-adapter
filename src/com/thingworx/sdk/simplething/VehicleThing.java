package com.thingworx.sdk.simplething;

import java.util.ArrayList;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inw.adapter.DeviceCommand;
import com.inw.adapter.OBDDataPacket;
import com.thingworx.communications.client.ConnectedThingClient;
import com.thingworx.communications.client.things.VirtualThing;
import com.thingworx.metadata.PropertyDefinition;
import com.thingworx.metadata.annotations.ThingworxPropertyDefinition;
import com.thingworx.metadata.annotations.ThingworxPropertyDefinitions;
import com.thingworx.metadata.annotations.ThingworxServiceDefinition;
import com.thingworx.metadata.annotations.ThingworxServiceParameter;
import com.thingworx.metadata.annotations.ThingworxServiceResult;
import com.thingworx.types.primitives.IPrimitiveType;
import com.thingworx.types.primitives.InfoTablePrimitive;
import com.thingworx.types.primitives.IntegerPrimitive;
import com.thingworx.types.primitives.structs.Location;
import com.thingworx.types.primitives.structs.VTQ;
import com.thingworx.types.InfoTable;
import com.thingworx.types.constants.QualityStatus;
import com.thingworx.types.primitives.DatetimePrimitive;
import com.thingworx.types.primitives.LocationPrimitive;
import com.thingworx.types.primitives.NumberPrimitive;
import com.thingworx.types.primitives.StringPrimitive;

@ThingworxPropertyDefinitions(properties = {

        // Vehicle Flow Parameters

        @ThingworxPropertyDefinition(name = "DistanceTravelledAfterDTC", description = "Distance travelled after all fault codes are cleared",
        baseType = "NUMBER",
        aspects = { "dataChangeType:VALUE", "dataChangeThreshold:0", "cacheTime:0",
                "isPersistent:FALSE", "isReadOnly:FALSE", "pushType:VALUE",
                "defaultValue:0" })
        ,@ThingworxPropertyDefinition(name = "VehicleBatteryVoltage", description = "Vehicle battery Voltage",
        baseType = "NUMBER",
        aspects = { "dataChangeType:VALUE", "dataChangeThreshold:0", "cacheTime:0",
                "isPersistent:FALSE", "isReadOnly:FALSE", "pushType:VALUE",
                "defaultValue:0" })
        ,@ThingworxPropertyDefinition(name = "VehicleDataRPM", description = "Vehicle Engine RPM",
        baseType = "NUMBER",
        aspects = { "dataChangeType:VALUE", "dataChangeThreshold:0", "cacheTime:0",
                "isPersistent:FALSE", "isReadOnly:FALSE", "pushType:VALUE",
                "defaultValue:0" })
        ,@ThingworxPropertyDefinition(name = "VehicleEngineCoolantTemperature", description = "Vehicle Engine Coolant temperature",
        baseType = "NUMBER",
        aspects = { "dataChangeType:VALUE", "dataChangeThreshold:0", "cacheTime:0",
                "isPersistent:FALSE", "isReadOnly:FALSE", "pushType:VALUE",
                "defaultValue:0" })
        ,@ThingworxPropertyDefinition(name = "VehicleFuelLevelInput", description = "Vehicle Fuel level %",
        baseType = "NUMBER",
        aspects = { "dataChangeType:VALUE", "dataChangeThreshold:0", "cacheTime:0",
                "isPersistent:FALSE", "isReadOnly:FALSE", "pushType:VALUE",
                "defaultValue:0" })
        ,@ThingworxPropertyDefinition(name = "VehicleSpeed", description = "Vehicle Ground Speed",
        baseType = "NUMBER",
        aspects = { "dataChangeType:VALUE", "dataChangeThreshold:0", "cacheTime:0",
                "isPersistent:FALSE", "isReadOnly:FALSE", "pushType:VALUE",
                "defaultValue:0" })
        ,@ThingworxPropertyDefinition(name = "VehicleDataGPS", description = "Vehicle GPS Position",
        baseType = "LOCATION",
        aspects = { "dataChangeType:VALUE", "dataChangeThreshold:0", "cacheTime:0",
                "isPersistent:FALSE", "isReadOnly:FALSE", "pushType:VALUE" })
        ,@ThingworxPropertyDefinition(name = "VehicleTotalMileage", description = "Vehicle Total Mileage",
        baseType = "NUMBER",
        aspects = { "dataChangeType:VALUE", "dataChangeThreshold:0", "cacheTime:0",
                        "isPersistent:FALSE", "isReadOnly:FALSE", "pushType:VALUE",
                        "defaultValue:0" })
		,@ThingworxPropertyDefinition(name = "VehicleDirectionAngle", description = "Vehicle heading direction angle from GPS",
		baseType = "NUMBER",
		aspects = { "dataChangeType:VALUE", "dataChangeThreshold:0", "cacheTime:0",
		        "isPersistent:FALSE", "isReadOnly:FALSE", "pushType:VALUE",
		        "defaultValue:0" })   
		,@ThingworxPropertyDefinition(name = "IgnitionOffTime", description = "Engine Ignition Off Time",
		baseType = "DATETIME",
		aspects = { "dataChangeType:VALUE", "dataChangeThreshold:0", "cacheTime:0",
		        "isPersistent:FALSE", "isReadOnly:FALSE", "pushType:VALUE" })         
		,@ThingworxPropertyDefinition(name = "IgnitionOnTime", description = "Engine Ignition ON time",
		baseType = "DATETIME",
		aspects = { "dataChangeType:VALUE", "dataChangeThreshold:0", "cacheTime:0",
		        "isPersistent:FALSE", "isReadOnly:FALSE", "pushType:VALUE" })         
        
        // Trip Summary Parameters
        
        ,@ThingworxPropertyDefinition(name = "DrivingDistance", description = "Trip Driving Distance",
        baseType = "NUMBER",
        aspects = { "dataChangeType:VALUE", "dataChangeThreshold:0", "cacheTime:0",
                "isPersistent:FALSE", "isReadOnly:FALSE", "pushType:VALUE",
                "defaultValue:0" })
		,@ThingworxPropertyDefinition(name = "DrivingFuelConsumption", description = "Trip FuelConsumption",
		baseType = "NUMBER",
		aspects = { "dataChangeType:VALUE", "dataChangeThreshold:0", "cacheTime:0",
		        "isPersistent:FALSE", "isReadOnly:FALSE", "pushType:VALUE",
		        "defaultValue:0" })        
		,@ThingworxPropertyDefinition(name = "HistoricalTotalMileage", description = "Trip Historical Mileage",
		baseType = "NUMBER",
		aspects = { "dataChangeType:VALUE", "dataChangeThreshold:0", "cacheTime:0",
		        "isPersistent:FALSE", "isReadOnly:FALSE", "pushType:VALUE",
		        "defaultValue:0" })        
		,@ThingworxPropertyDefinition(name = "IdleTime", description = "Trip Idel Time Duration",
		baseType = "NUMBER",
		aspects = { "dataChangeType:VALUE", "dataChangeThreshold:0", "cacheTime:0",
		        "isPersistent:FALSE", "isReadOnly:FALSE", "pushType:VALUE",
		        "defaultValue:0" }) 
		,@ThingworxPropertyDefinition(name = "TripIgnitionOffTime", description = "Trip Ignition Off Time",
		baseType = "DATETIME",
		aspects = { "dataChangeType:VALUE", "dataChangeThreshold:0", "cacheTime:0",
		        "isPersistent:FALSE", "isReadOnly:FALSE", "pushType:VALUE" }) 
		,@ThingworxPropertyDefinition(name = "TripIgnitionOnTime", description = "Trip Ignition ON time",
		baseType = "DATETIME",
		aspects = { "dataChangeType:VALUE", "dataChangeThreshold:0", "cacheTime:0",
		        "isPersistent:FALSE", "isReadOnly:FALSE", "pushType:VALUE" }) 
		,@ThingworxPropertyDefinition(name = "LocationWhenIgnitionOFF", description = "Trip Location WHen Ignition OFF",
		baseType = "LOCATION",
		aspects = { "dataChangeType:VALUE", "dataChangeThreshold:0", "cacheTime:0",
		        "isPersistent:FALSE", "isReadOnly:FALSE", "pushType:VALUE" }) 
		,@ThingworxPropertyDefinition(name = "LocationWhenIgnitionON", description = "Trip Location When Ignition ON",
		baseType = "LOCATION",
		aspects = { "dataChangeType:VALUE", "dataChangeThreshold:0", "cacheTime:0",
		        "isPersistent:FALSE", "isReadOnly:FALSE", "pushType:VALUE" }) 
		,@ThingworxPropertyDefinition(name = "MaximumSpeed", description = "Trip MaximumSpeed",
		baseType = "NUMBER",
		aspects = { "dataChangeType:VALUE", "dataChangeThreshold:0", "cacheTime:0",
		        "isPersistent:FALSE", "isReadOnly:FALSE", "pushType:VALUE",
		        "defaultValue:0" }) 
		,@ThingworxPropertyDefinition(name = "NumberOfRapidAcceleration", description = "Trip Number Of Rapid Acceleration",
		baseType = "NUMBER",
		aspects = { "dataChangeType:VALUE", "dataChangeThreshold:0", "cacheTime:0",
		        "isPersistent:FALSE", "isReadOnly:FALSE", "pushType:VALUE",
		        "defaultValue:0" }) 
		,@ThingworxPropertyDefinition(name = "NumberOfRapidDeceleration", description = "Trip Number Of Rapid Deceleration",
		baseType = "NUMBER",
		aspects = { "dataChangeType:VALUE", "dataChangeThreshold:0", "cacheTime:0",
		        "isPersistent:FALSE", "isReadOnly:FALSE", "pushType:VALUE",
		        "defaultValue:0" }) 
		,@ThingworxPropertyDefinition(name = "NumberOfSharpTurns", description = "Trip Number Of Sharp Turns",
		baseType = "NUMBER",
		aspects = { "dataChangeType:VALUE", "dataChangeThreshold:0", "cacheTime:0",
		        "isPersistent:FALSE", "isReadOnly:FALSE", "pushType:VALUE",
		        "defaultValue:0" })
                          
        
        //VIN Related Parameters
		,@ThingworxPropertyDefinition(name = "VIN", description = "Vehicle Identification number",
		baseType = "STRING",
		aspects = { "dataChangeType:VALUE", "dataChangeThreshold:0", "cacheTime:0",
		        "isPersistent:FALSE", "isReadOnly:FALSE", "pushType:VALUE" }) 
        
        
        // DTC Error code related parameters
        // Low battery alert DTC Error
		,@ThingworxPropertyDefinition(name = "Alert_BatteryVoltageTime", description = "Time at which Battery Low alert is triggered",
		baseType = "DATETIME",
		aspects = { "dataChangeType:VALUE", "dataChangeThreshold:0", "cacheTime:0",
		        "isPersistent:FALSE", "isReadOnly:FALSE", "pushType:VALUE" }) 
		,@ThingworxPropertyDefinition(name = "Alert_BatteryVoltageGPS", description = "Location at which Battery Low alert is triggered",
		baseType = "LOCATION",
		aspects = { "dataChangeType:VALUE", "dataChangeThreshold:0", "cacheTime:0",
		        "isPersistent:FALSE", "isReadOnly:FALSE", "pushType:VALUE" })         
		,@ThingworxPropertyDefinition(name = "Alert_BatteryVoltage", description = "Battery Voltage at which Battery Low alert is triggered",
		baseType = "NUMBER",
		aspects = { "dataChangeType:VALUE", "dataChangeThreshold:0", "cacheTime:0",
		        "isPersistent:FALSE", "isReadOnly:FALSE", "pushType:VALUE",
		        "defaultValue:0" })  
        
        //OBD Device pulled out Alert
		,@ThingworxPropertyDefinition(name = "Alert_DevicePulledOutTime", description = "Time at which OBD Device is pulled out",
		baseType = "DATETIME",
		aspects = { "dataChangeType:VALUE", "dataChangeThreshold:0", "cacheTime:0",
		        "isPersistent:FALSE", "isReadOnly:FALSE", "pushType:VALUE" }) 
		,@ThingworxPropertyDefinition(name = "Alert_DevicePulledOutGPS", description = "Location at which OBD Device is pulled out",
		baseType = "LOCATION",
		aspects = { "dataChangeType:VALUE", "dataChangeThreshold:0", "cacheTime:0",
		        "isPersistent:FALSE", "isReadOnly:FALSE", "pushType:VALUE" })         
		,@ThingworxPropertyDefinition(name = "Alert_DevicePulledoutState", description = "Status when OBD Device is pulled out",
		baseType = "NUMBER",
		aspects = { "dataChangeType:VALUE", "dataChangeThreshold:0", "cacheTime:0",
		        "isPersistent:FALSE", "isReadOnly:FALSE", "pushType:ALWAYS",
		        "defaultValue:0" })        
        
        //Suspected collision Alert
		,@ThingworxPropertyDefinition(name = "Alert_SuspectedCollisionTime", description = "Time at which Suspected Collition detected",
		baseType = "DATETIME",
		aspects = { "dataChangeType:VALUE", "dataChangeThreshold:0", "cacheTime:0",
		        "isPersistent:FALSE", "isReadOnly:FALSE", "pushType:VALUE" }) 
		,@ThingworxPropertyDefinition(name = "Alert_SuspectedCollionGPS", description = "Location at which Suspected Collition detected",
		baseType = "LOCATION",
		aspects = { "dataChangeType:VALUE", "dataChangeThreshold:0", "cacheTime:0",
		        "isPersistent:FALSE", "isReadOnly:FALSE", "pushType:VALUE" })         
		,@ThingworxPropertyDefinition(name = "Alert_SuspectedCollision", description = "Collion value when suspected collition detected",
		baseType = "NUMBER",
		aspects = { "dataChangeType:VALUE", "dataChangeThreshold:0", "cacheTime:0",
		        "isPersistent:FALSE", "isReadOnly:FALSE", "pushType:VALUE",
		        "defaultValue:0" })          
        
        //Vibration after ignition off Alert
		,@ThingworxPropertyDefinition(name = "Alert_VibrationAfterIngnitionOFFTime", description = "Time at which vibration detected",
		baseType = "DATETIME",
		aspects = { "dataChangeType:VALUE", "dataChangeThreshold:0", "cacheTime:0",
		        "isPersistent:FALSE", "isReadOnly:FALSE", "pushType:VALUE" }) 
		,@ThingworxPropertyDefinition(name = "Alert_VibrationAfterIgnitionOnGPS", description = "Location at which Vibration detected",
		baseType = "LOCATION",
		aspects = { "dataChangeType:VALUE", "dataChangeThreshold:0", "cacheTime:0",
		        "isPersistent:FALSE", "isReadOnly:FALSE", "pushType:VALUE" })         
		,@ThingworxPropertyDefinition(name = "Alert_VibrationAfterIngnitionOFF", description = "Vibration value when detected",
		baseType = "NUMBER",
		aspects = { "dataChangeType:VALUE", "dataChangeThreshold:0", "cacheTime:0",
		        "isPersistent:FALSE", "isReadOnly:FALSE", "pushType:VALUE",
		        "defaultValue:0" })
        
        //DTC Error codes
		,@ThingworxPropertyDefinition(name = "DTCErrorData", description = "DTC Errocodes and description infotable",
		baseType = "INFOTABLE",
		aspects = { "dataChangeType:VALUE", "dataChangeThreshold:0", "cacheTime:0",
		        "isPersistent:FALSE", "isReadOnly:FALSE", "pushType:VALUE" })
        
		,@ThingworxPropertyDefinition(name = "DTCErrorTime", description = "Time at which DTC Error detected",
		baseType = "DATETIME",
		aspects = { "dataChangeType:VALUE", "dataChangeThreshold:0", "cacheTime:0",
		        "isPersistent:FALSE", "isReadOnly:FALSE", "pushType:VALUE" })         
        
        //OBD Dongle Related Parameters
		,@ThingworxPropertyDefinition(name = "IMEI", description = "OBD Device GSM Modem Identification number",
		baseType = "STRING",
		aspects = { "dataChangeType:VALUE", "dataChangeThreshold:0", "cacheTime:0",
		        "isPersistent:FALSE", "isReadOnly:FALSE", "pushType:VALUE" }) 
		,@ThingworxPropertyDefinition(name = "HardwareVersion", description = "OBD Device hardware version number",
		baseType = "STRING",
		aspects = { "dataChangeType:VALUE", "dataChangeThreshold:0", "cacheTime:0",
		        "isPersistent:FALSE", "isReadOnly:FALSE", "pushType:VALUE" }) 
		,@ThingworxPropertyDefinition(name = "SoftwareVersionMCU", description = "OBD Device software version number",
		baseType = "STRING",
		aspects = { "dataChangeType:VALUE", "dataChangeThreshold:0", "cacheTime:0",
		        "isPersistent:FALSE", "isReadOnly:FALSE", "pushType:VALUE" }) 
		,@ThingworxPropertyDefinition(name = "SoftwareVersionModem", description = "OBD Device software version number",
		baseType = "STRING",
		aspects = { "dataChangeType:VALUE", "dataChangeThreshold:0", "cacheTime:0",
		        "isPersistent:FALSE", "isReadOnly:FALSE", "pushType:VALUE" })         
		,@ThingworxPropertyDefinition(name = "ProtocolVersion", description = "OBD Device protocol version number",
		baseType = "STRING",
		aspects = { "dataChangeType:VALUE", "dataChangeThreshold:0", "cacheTime:0",
		        "isPersistent:FALSE", "isReadOnly:FALSE", "pushType:VALUE" })          
		,@ThingworxPropertyDefinition(name = "WiFiHotSpotSSID", description = "OBD Device Wifi Hotspot SSID",
		baseType = "STRING",
		aspects = { "dataChangeType:VALUE", "dataChangeThreshold:0", "cacheTime:0",
		        "isPersistent:FALSE", "isReadOnly:FALSE", "pushType:VALUE" })
		,@ThingworxPropertyDefinition(name = "WiFiHotspotPassword", description = "OBD Device Wifi Hotspot password",
		baseType = "STRING",
		aspects = { "dataChangeType:VALUE", "dataChangeThreshold:0", "cacheTime:0",
		        "isPersistent:FALSE", "isReadOnly:FALSE", "pushType:VALUE" })          
		,@ThingworxPropertyDefinition(name = "WiFiHotSpotStatus", description = "OBD Device Wifi Hotspot ON/OFF Status",
		baseType = "BOOLEAN",
		aspects = { "dataChangeType:VALUE", "dataChangeThreshold:0", "cacheTime:0",
		        "isPersistent:FALSE", "isReadOnly:FALSE", "pushType:VALUE" })
		
        // SIM & WIFI Parameters
        ,@ThingworxPropertyDefinition(name = "simPhoneNumber", description = "Phone number of SIM inserted in OBD Dongle",
		baseType = "STRING",
		aspects = { "dataChangeType:VALUE", "dataChangeThreshold:0", "cacheTime:0",
		        "isPersistent:FALSE", "isReadOnly:FALSE", "pushType:VALUE" })        
		,@ThingworxPropertyDefinition(name = "simICCIDNumber", description = "ICCID number of SIM inserted in OBD Dongle",
		baseType = "STRING",
		aspects = { "dataChangeType:VALUE", "dataChangeThreshold:0", "cacheTime:0",
		        "isPersistent:FALSE", "isReadOnly:FALSE", "pushType:VALUE" })          
		,@ThingworxPropertyDefinition(name = "simIMSINumber", description = "IMSI number of SIM inserted in OBD Dongle",
		baseType = "STRING",
		aspects = { "dataChangeType:VALUE", "dataChangeThreshold:0", "cacheTime:0",
		        "isPersistent:FALSE", "isReadOnly:FALSE", "pushType:VALUE" })
		,@ThingworxPropertyDefinition(name = "wifiMACid", description = "WiFi Mac ID of OBD Dongle",
		baseType = "STRING",
		aspects = { "dataChangeType:VALUE", "dataChangeThreshold:0", "cacheTime:0",
		        "isPersistent:FALSE", "isReadOnly:FALSE", "pushType:VALUE" })
		,@ThingworxPropertyDefinition(name = "bluetoothMACid", description = "bluetooth MAC id of OBD Dongle",
		baseType = "STRING",
		aspects = { "dataChangeType:VALUE", "dataChangeThreshold:0", "cacheTime:0",
		        "isPersistent:FALSE", "isReadOnly:FALSE", "pushType:VALUE" })
        
        // OBD Dongle State parameters
        //Sleep Event parameters
		,@ThingworxPropertyDefinition(name = "State_SleepTime", description = "Time at which Sleep Event occured",
		baseType = "DATETIME",
		aspects = { "dataChangeType:VALUE", "dataChangeThreshold:0", "cacheTime:0",
		        "isPersistent:FALSE", "isReadOnly:FALSE", "pushType:VALUE" }) 
		,@ThingworxPropertyDefinition(name = "State_SleepGPS", description = "Location at which Sleep Event occured",
		baseType = "LOCATION",
		aspects = { "dataChangeType:VALUE", "dataChangeThreshold:0", "cacheTime:0",
		        "isPersistent:FALSE", "isReadOnly:FALSE", "pushType:VALUE" })          
		,@ThingworxPropertyDefinition(name = "State_SleepVoltage", description = "Battery Voltage when Sleep Event occured",
		baseType = "NUMBER",
		aspects = { "dataChangeType:VALUE", "dataChangeThreshold:0", "cacheTime:0",
		        "isPersistent:FALSE", "isReadOnly:FALSE", "pushType:VALUE",
		        "defaultValue:0" })
        
        //Wake-up Event Parameters
		,@ThingworxPropertyDefinition(name = "State_WakeUpTime", description = "Time at which WakeUp Event occured",
		baseType = "DATETIME",
		aspects = { "dataChangeType:VALUE", "dataChangeThreshold:0", "cacheTime:0",
		        "isPersistent:FALSE", "isReadOnly:FALSE", "pushType:VALUE" }) 
		,@ThingworxPropertyDefinition(name = "State_WakeUpGPS", description = "Location at which WakeUp Event occured",
		baseType = "LOCATION",
		aspects = { "dataChangeType:VALUE", "dataChangeThreshold:0", "cacheTime:0",
		        "isPersistent:FALSE", "isReadOnly:FALSE", "pushType:VALUE" })          
		,@ThingworxPropertyDefinition(name = "State_WakeUpVoltage", description = "Battery Voltage when WakeUp Event occured",
		baseType = "NUMBER",
		aspects = { "dataChangeType:VALUE", "dataChangeThreshold:0", "cacheTime:0",
		        "isPersistent:FALSE", "isReadOnly:FALSE", "pushType:VALUE",
		        "defaultValue:0" })        
		,@ThingworxPropertyDefinition(name = "State_WakeUpType", description = "Reason for Waking up",
		baseType = "INTEGER",
		aspects = { "dataChangeType:VALUE", "dataChangeThreshold:0", "cacheTime:0",
		        "isPersistent:FALSE", "isReadOnly:FALSE", "pushType:VALUE",
		        "defaultValue:0" })
        
        //Can't GPS Locate Event Parameters
		,@ThingworxPropertyDefinition(name = "State_CantLocateForLongTime", description = "Time at which WakeUp Event occured",
		baseType = "DATETIME",
		aspects = { "dataChangeType:VALUE", "dataChangeThreshold:0", "cacheTime:0",
		        "isPersistent:FALSE", "isReadOnly:FALSE", "pushType:VALUE" }) 
		,@ThingworxPropertyDefinition(name = "State_CantLocateLastGPS", description = "Location at which WakeUp Event occured",
		baseType = "LOCATION",
		aspects = { "dataChangeType:VALUE", "dataChangeThreshold:0", "cacheTime:0",
		        "isPersistent:FALSE", "isReadOnly:FALSE", "pushType:VALUE" })  
        // Power On After Reboot Event Parameters
		,@ThingworxPropertyDefinition(name = "State_PowerONAfterRebootTime", description = "Time at which WakeUp Event occured",
		baseType = "DATETIME",
		aspects = { "dataChangeType:VALUE", "dataChangeThreshold:0", "cacheTime:0",
		        "isPersistent:FALSE", "isReadOnly:FALSE", "pushType:VALUE" }) 
		,@ThingworxPropertyDefinition(name = "State_PowerDownLastTime", description = "Time at which WakeUp Event occured",
		baseType = "DATETIME",
		aspects = { "dataChangeType:VALUE", "dataChangeThreshold:0", "cacheTime:0",
		        "isPersistent:FALSE", "isReadOnly:FALSE", "pushType:VALUE" }) 
		,@ThingworxPropertyDefinition(name = "State_PowerONAfterRebootType", description = "Reason for Waking up",
		baseType = "INTEGER",
		aspects = { "dataChangeType:VALUE", "dataChangeThreshold:0", "cacheTime:0",
		        "isPersistent:FALSE", "isReadOnly:FALSE", "pushType:VALUE",
		        "defaultValue:0" })
        
        // Dongle Upgrade Event
		,@ThingworxPropertyDefinition(name = "State_UpgradeCompleteTime", description = "Time at which WakeUp Event occured",
		baseType = "DATETIME",
		aspects = { "dataChangeType:VALUE", "dataChangeThreshold:0", "cacheTime:0",
		        "isPersistent:FALSE", "isReadOnly:FALSE", "pushType:VALUE" }) 
		,@ThingworxPropertyDefinition(name = "State_UpgradeStatus", description = "Reason for Waking up",
		baseType = "INTEGER",
		aspects = { "dataChangeType:VALUE", "dataChangeThreshold:0", "cacheTime:0",
		        "isPersistent:FALSE", "isReadOnly:FALSE", "pushType:VALUE",
		        "defaultValue:0" })        
        
})


/**
 * A very basic VirtualThing with two properties and a service implementation. It also implements
 * processScanRequest to handle periodic actions.
 */
public class VehicleThing extends VirtualThing {

    private static final Logger LOG = LoggerFactory.getLogger(VehicleThing.class);

    private OBDDataPacket srcPacket;
    /**
     * A custom constructor. We implement this so we can call initializeFromAnnotations, which
     * processes all of the VirtualThing's annotations and applies them to the object.
     * 
     * @param name The name of the thing.
     * @param description A description of the thing.
     * @param client The client that this thing is associated with.
     */
    public VehicleThing(String name, String description, ConnectedThingClient client,OBDDataPacket srcPacket)
            throws Exception {

        super(name, description, client);
        this.srcPacket = srcPacket;
        this.initializeFromAnnotations();

    }

    public OBDDataPacket getSrcPacket() {
    	return this.srcPacket;
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

    // The following annotation allows you to make a method available to the
    // ThingWorx Server for remote invocation. The annotation includes the
    // name of the server, the name and base types for its parameters, and
    // the base type of its result.
    @ThingworxServiceDefinition(name = "Add", description = "Add two numbers")
    @ThingworxServiceResult(name = "result", description = "The sum of the two parameters",
            baseType = "NUMBER")
    public Double Add(
            @ThingworxServiceParameter(name = "p1",
                    description = "The first addend of the operation",
                    baseType = "NUMBER") Double p1,
            @ThingworxServiceParameter(name = "p2",
                    description = "The second addend of the operation",
                    baseType = "NUMBER") Double p2)
            throws Exception {
        LOG.info("Adding the numbers {} and {}", p1, p2);
        return p1 * p2;
    }
        
    private ArrayList< DeviceCommand > pendingCommads = new ArrayList<DeviceCommand>();
    private Integer frameNumber = 1;
    
    private Integer curHistoricMileage = 0;
    
    private DateTime ignitionOnTime, ignitionOffTime; 
    
    public Integer getNewFrameNumber() {
    	
    	return this.frameNumber++;
    }
    
    public void addDeviceCommandToPendingList(DeviceCommand command) {
    	
    	this.pendingCommads.add(command);
    }   
    
    public ArrayList<DeviceCommand> getPendingCommands(){
    	
    	return pendingCommads;
    }

    public void deleteFromPendingCommands(DeviceCommand command) {
    	
    	pendingCommads.remove(command);
    }

	public Integer getCurHistoricMileage() {
		return curHistoricMileage;
	}

	public void setCurHistoricMileage(Integer curHistoricMileage) {
		this.curHistoricMileage = curHistoricMileage;
	}
	
	private DateTime propertyUpdateTimeStamp =null;
	public void initPropertyUpdate() {
		this.propertyUpdateTimeStamp = new DateTime();
	}
	
	public void setNumericPropertyValue(String propertyName, Double value) {
		NumberPrimitive numPrem = new NumberPrimitive();
		numPrem.setValue(value);
	    VTQ vtq = new VTQ();
	    vtq.setValue(numPrem);
	    vtq.setTime(propertyUpdateTimeStamp);
	    vtq.setQuality(QualityStatus.GOOD);
	    try {
			this.setPropertyVTQ(propertyName, vtq, true);
		} catch (Exception e) {
			LOG.error("Error setting numeric property {} :",propertyName,e);
		}
	}
	public void setIntegerPropertyValue(String propertyName, Integer value) {
		IntegerPrimitive intPrem = new IntegerPrimitive();
		intPrem.setValue(value);
	    VTQ vtq = new VTQ();
	    vtq.setValue(intPrem);
	    vtq.setTime(propertyUpdateTimeStamp);
	    vtq.setQuality(QualityStatus.GOOD);
	    try {
			this.setPropertyVTQ(propertyName, vtq, true);
		} catch (Exception e) {
			LOG.error("Error -setting Integer property {} :",propertyName,e);
		}
	}	
	public void setStringPropertyValue(String propertyName, String value) {
	    try {
			StringPrimitive prem = new StringPrimitive();
			prem.setValue(value);
		    VTQ vtq = new VTQ();
		    vtq.setValue(prem);
		    vtq.setTime(propertyUpdateTimeStamp);
		    vtq.setQuality(QualityStatus.GOOD);
			this.setPropertyVTQ(propertyName, vtq, true);
		} catch (Exception e) {
			LOG.error("Error -setting String property {} :",propertyName,e);
		}
	}
	public void setDateTimePropertyValue(String propertyName, DateTime value) {
		DatetimePrimitive prem = new DatetimePrimitive();
		prem.setValue(value);
	    VTQ vtq = new VTQ();
	    vtq.setValue(prem);
	    vtq.setTime(propertyUpdateTimeStamp);
	    vtq.setQuality(QualityStatus.GOOD);
	    try {
			this.setPropertyVTQ(propertyName, vtq, true);
		} catch (Exception e) {
			LOG.error("Error -setting Date Time property {} :",propertyName,e);
		}
	}
	public void setLocationPropertyValue(String propertyName, Location value) {
		LocationPrimitive prem = new LocationPrimitive();
		prem.setValue(value);
	    VTQ vtq = new VTQ();
	    vtq.setValue(prem);
	    vtq.setTime(propertyUpdateTimeStamp);
	    vtq.setQuality(QualityStatus.GOOD);
	    try {
			this.setPropertyVTQ(propertyName, vtq, true);
		} catch (Exception e) {
			LOG.error("Error -setting Location property {} :",propertyName,e);
		}
	}
	public void setInfoTablePropertyValue(String propertyName, InfoTable value) {
		InfoTablePrimitive prem = new InfoTablePrimitive();
		prem.setValue(value);
	    VTQ vtq = new VTQ();
	    vtq.setValue(prem);
	    vtq.setTime(propertyUpdateTimeStamp);
	    vtq.setQuality(QualityStatus.GOOD);
	    try {
			this.setPropertyVTQ(propertyName, vtq, true);
		} catch (Exception e) {
			LOG.error("Error -setting Infotable property {} :",propertyName,e);
		}
	}

	public DateTime getIgnitionOnTime() {
		return ignitionOnTime;
	}

	public void setIgnitionOnTime(DateTime ignitionOnTime) {
		this.ignitionOnTime = ignitionOnTime;
	}

	public DateTime getIgnitionOffTime() {
		return ignitionOffTime;
	}

	public void setIgnitionOffTime(DateTime ignitionOffTime) {
		this.ignitionOffTime = ignitionOffTime;
	}	
}
