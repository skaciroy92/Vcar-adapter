package com.inw.adapter;

import java.nio.ByteBuffer;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.joda.time.DateTime;

public class VehTripSummaryData extends AdapterBase implements OBDDataSection {

	private String ignition_ONTime_str,
					ignition_ONGPSLocation_str,
					ignition_OFFTime_str,
					ignition_OFFGPSLocation_str,
					DrivingDistance_str,
					FuelConsumption_str,
					MaximumSpeed_str,
					IdleTime_str,
					IdleFuelConsumption_str,
					NumRapidAcceleration_str,
					NumRapidDeceleration_str,
					NumRapidSharpTurns_str,
					HistoricalTotalMileage_str,
					HistoricalTotalFuelConsumption_str,
					HistoricalTotalDrivingTime_str;
	
	private GPSData ign_ON_GPSData, ign_OFF_GPSData;
	
	private Integer drivingDistance_int,
					fueleConsumption_int,
					MaximumSpeed_int, 
					IdleTime_int, 
					idleFuelConsumption_int,
					NumRapidAccelerations_int,
					NumRapidDecelerations_int,
					NumRapidSharpTurns_int;
	
	private long    HistoricaTotalMileage_int,
					HistoricalTotalFuelConsumption_int,
					HistoricalDrivingTime_int;
	
	private DateTime ignition_ON_Time_dt,ignition_OFF_Time_dt;
	
	public String toString() {
		String str = " \n ignition_ONTime = " + ignition_ON_Time_dt 
				+ " \n ignitionON GPSData : " + ign_ON_GPSData.toString()
				+ " \n ignition_OFFTime = " + ignition_OFF_Time_dt 
				+ " \n ignitionOFFGPS Data : " + ign_OFF_GPSData.toString()				
				+ " \n drivingDistance_int = " + drivingDistance_int
				+ " \n fueleConsumption_int = " + fueleConsumption_int
				+ " \n MaximumSpeed_int = " + MaximumSpeed_int
				+ " \n IdleTime_int = " + IdleTime_int
				+ " \n idleFuelConsumption_int = " + idleFuelConsumption_int
				+ " \n NumRapidAccelerations_int = " + NumRapidAccelerations_int
				+ " \n NumRapidDecelerations_int = " + NumRapidDecelerations_int
				+ " \n NumRapidSharpTurns_int = " + NumRapidSharpTurns_int
				+ " \n HistoricaTotalMileage_int = " + HistoricaTotalMileage_int
				+ " \n HistoricalTotalFuelConsumption_int = " + HistoricalTotalFuelConsumption_int
				+ " \n HistoricalDrivingTime_int = " + HistoricalDrivingTime_int;
		return str;
	}

	public Integer getDrivingDistance_int() {
		return drivingDistance_int;
	}

	public Integer getFueleConsumption_int() {
		return fueleConsumption_int;
	}

	public Integer getMaximumSpeed_int() {
		return MaximumSpeed_int;
	}

	public Integer getIdleTime_int() {
		return IdleTime_int;
	}

	public Integer getIdleFuelConsumption_int() {
		return idleFuelConsumption_int;
	}

	public Integer getNumRapidAccelerations_int() {
		return NumRapidAccelerations_int;
	}

	public Integer getNumRapidDecelerations_int() {
		return NumRapidDecelerations_int;
	}

	public Integer getNumRapidSharpTurns_int() {
		return NumRapidSharpTurns_int;
	}

	public long getHistoricaTotalMileage_int() {
		return HistoricaTotalMileage_int;
	}

	public long getHistoricalTotalFuelConsumption_int() {
		return HistoricalTotalFuelConsumption_int;
	}

	public long getHistoricalDrivingTime_int() {
		return HistoricalDrivingTime_int;
	}


	public String getIgnition_ONTime_str() {
		return ignition_ONTime_str;
	}


	public void setIgnition_ONTime_str(String ignition_ONTime_str) {
		this.ignition_ONTime_str = ignition_ONTime_str;
		logger.info(" ignition_ONTime_str {}",ignition_ONTime_str);
		this.ignition_ON_Time_dt = this.getDateFromHexString(ignition_ONTime_str);
		logger.info(" ignition_ON_Time_dt {}",this.ignition_ON_Time_dt);
	}


	public String getIgnition_ONGPSLocation_str() {
		return ignition_ONGPSLocation_str;
	}


	public void setIgnition_ONGPSLocation_str(String ignition_ONGPSLocation_str) {
		this.ignition_ONGPSLocation_str = ignition_ONGPSLocation_str;
		this.ign_ON_GPSData = new GPSData(this.ignition_ONGPSLocation_str);		
	}


	public String getIgnition_OFFTime_str() {
		return ignition_OFFTime_str;
	}


	public void setIgnition_OFFTime_str(String ignition_OFFTime_str) {
		this.ignition_OFFTime_str = ignition_OFFTime_str;
		logger.info(" ignition_OFFTime_str {}",ignition_OFFTime_str);
		this.ignition_OFF_Time_dt = this.getDateFromHexString(ignition_OFFTime_str);
		logger.info(" ignition_OFF_Time_dt {}",this.ignition_OFF_Time_dt);

	}


	public String getIgnition_OFFGPSLocation_str() {
		return ignition_OFFGPSLocation_str;

	}

	public void setIgnition_OFFGPSLocation_str(String ignition_OFFGPSLocation_str) {
		this.ignition_OFFGPSLocation_str = ignition_OFFGPSLocation_str;
		this.ign_OFF_GPSData = new GPSData(this.ignition_OFFGPSLocation_str);				
	}


	public String getDrivingDistance_str() {
		return DrivingDistance_str;
	}


	public void setDrivingDistance_str(String drivingDistance_str) {
		DrivingDistance_str = drivingDistance_str;

		logger.info("Trip Summary : drivingDistance HEX STR:{} ", drivingDistance_str);
		try {
			byte[] distanceBytes;

			distanceBytes = Hex.decodeHex(drivingDistance_str.toCharArray());
			String distanceBin1 = String.format("%8s", Integer.toBinaryString(distanceBytes[0] & 0xFF)).replace(' ', '0')
					+ String.format("%8s", Integer.toBinaryString(distanceBytes[1] & 0xFF)).replace(' ', '0')
					+ String.format("%8s", Integer.toBinaryString(distanceBytes[2] & 0xFF)).replace(' ', '0');
			logger.info("DISTANCE BYTES BEFORE MASKING:{} ", distanceBin1);
			distanceBytes[0] = (byte)( distanceBytes[0] & 0x7F);
			String distanceBin2 = String.format("%8s", Integer.toBinaryString(distanceBytes[0] & 0xFF)).replace(' ', '0')
					+ String.format("%8s", Integer.toBinaryString(distanceBytes[1] & 0xFF)).replace(' ', '0')
					+ String.format("%8s", Integer.toBinaryString(distanceBytes[2] & 0xFF)).replace(' ', '0');			
			logger.info("DISTANCE BYTES AFTER MASKING:{} ", distanceBin2);
			Integer distanceInt = Integer.parseInt(distanceBin2, 2);
			logger.info("Int representation of Trip DrivingDistance {} is {}", distanceBin2, distanceInt);
			this.drivingDistance_int = distanceInt;				
		} catch (DecoderException e) {
			logger.error("Exception occured: ",e);
		}		
	}


	public String getFuelConsumption_str() {
		return FuelConsumption_str;
	}


	public void setFuelConsumption_str(String fuelConsumption_str) {
		FuelConsumption_str = fuelConsumption_str;
		logger.info("Trip Summary : FuelConsumption HEX STR:{} ", FuelConsumption_str);
		try {
			byte[] fuelConsBytes;

			fuelConsBytes = Hex.decodeHex(FuelConsumption_str.toCharArray());
			String fuelBin1 = String.format("%8s", Integer.toBinaryString(fuelConsBytes[0] & 0xFF)).replace(' ', '0')
					+ String.format("%8s", Integer.toBinaryString(fuelConsBytes[1] & 0xFF)).replace(' ', '0')
					+ String.format("%8s", Integer.toBinaryString(fuelConsBytes[2] & 0xFF)).replace(' ', '0');
			logger.info("FUEL BYTES :{} ", fuelBin1);
			Integer fuelConsInt = Integer.parseInt(fuelBin1, 2);
			logger.info("Int representation of Trip Summary : FuelConsumption {} is {}", fuelBin1, fuelConsInt);
			this.fueleConsumption_int = fuelConsInt;				
		} catch (DecoderException e) {
			logger.error("Exception occured: ",e);
		}		
		
	}


	public String getMaximumSpeed_str() {
		return MaximumSpeed_str;
	}


	public void setMaximumSpeed_str(String maximumSpeed_str) {
		MaximumSpeed_str = maximumSpeed_str;
		try {
			logger.info("Trip Max Speed Hex {}",MaximumSpeed_str);
			this.MaximumSpeed_int = Short.toUnsignedInt(ByteBuffer.wrap(Hex.decodeHex(MaximumSpeed_str.toCharArray())).getShort());
			logger.info("MaximumSpeed_int {}",MaximumSpeed_int);
		} catch (DecoderException e) {
			OBDDataPacket.logger.error(e.getStackTrace().toString());
		}			
	}


	public String getIdleTime_str() {
		return IdleTime_str;
	}


	public void setIdleTime_str(String idleTime_str) {
		IdleTime_str = idleTime_str;
		try {
			logger.info("Trip IdleTime_str Hex {}",IdleTime_str);
			this.IdleTime_int = Short.toUnsignedInt(ByteBuffer.wrap(Hex.decodeHex(IdleTime_str.toCharArray())).getShort());
			logger.info("IdleTime_int {}",IdleTime_int);
		} catch (DecoderException e) {
			OBDDataPacket.logger.error(e.getStackTrace().toString());
		}		
	}


	public String getIdleFuelConsumption_str() {
		return IdleFuelConsumption_str;
	}


	public void setIdleFuelConsumption_str(String idleFuelConsumption_str) {
		IdleFuelConsumption_str = idleFuelConsumption_str;
		try {
			logger.info("Trip IdleFuelConsumption_str Hex {}",IdleFuelConsumption_str);
			this.idleFuelConsumption_int = Short.toUnsignedInt(ByteBuffer.wrap(Hex.decodeHex(IdleFuelConsumption_str.toCharArray())).getShort());
			logger.info("idleFuelConsumption_int {}",idleFuelConsumption_int);
		} catch (DecoderException e) {
			OBDDataPacket.logger.error(e.getStackTrace().toString());
		}	
	}


	public String getNumRapidAcceleration_str() {
		return NumRapidAcceleration_str;
	}


	public void setNumRapidAcceleration_str(String numRapidAcceleration_str) {
		NumRapidAcceleration_str = numRapidAcceleration_str;
		try {
			logger.info("Trip NumRapidAcceleration_str Hex {}",NumRapidAcceleration_str);
			this.NumRapidAccelerations_int = Byte.toUnsignedInt(ByteBuffer.wrap(Hex.decodeHex(NumRapidAcceleration_str.toCharArray())).get());
			logger.info("NumRapidAccelerations_int {}",NumRapidAccelerations_int);
		} catch (DecoderException e) {
			OBDDataPacket.logger.error(e.getStackTrace().toString());
		}		
	}


	public String getNumRapidDeceleration_str() {
		return NumRapidDeceleration_str;
	}


	public void setNumRapidDeceleration_str(String numRapidDeceleration_str) {
		NumRapidDeceleration_str = numRapidDeceleration_str;
		try {
			logger.info("Trip NumRapidDeceleration_str Hex {}",NumRapidDeceleration_str);
			this.NumRapidDecelerations_int = Byte.toUnsignedInt(ByteBuffer.wrap(Hex.decodeHex(NumRapidDeceleration_str.toCharArray())).get());
			logger.info("NumRapidDecelerations_int {}",NumRapidDecelerations_int);
		} catch (DecoderException e) {
			OBDDataPacket.logger.error(e.getStackTrace().toString());
		}		
	}


	public String getNumRapidSharpTurns_str() {
		return NumRapidSharpTurns_str;
	}


	public void setNumRapidSharpTurns_str(String numRapidSharpTurns_str) {
		NumRapidSharpTurns_str = numRapidSharpTurns_str;
		try {
			logger.info("Trip NumRapidSharpTurns_str Hex {}",NumRapidSharpTurns_str);
			this.NumRapidSharpTurns_int = Byte.toUnsignedInt(ByteBuffer.wrap(Hex.decodeHex(NumRapidSharpTurns_str.toCharArray())).get());
			logger.info("NumRapidSharpTurns_int {}",NumRapidSharpTurns_int);
		} catch (DecoderException e) {
			OBDDataPacket.logger.error(e.getStackTrace().toString());
		}		
	}


	public String getHistoricalTotalMileage_str() {
		return HistoricalTotalMileage_str;
	}


	public void setHistoricalTotalMileage_str(String historicalTotalMileage_str) {
		HistoricalTotalMileage_str = historicalTotalMileage_str;
		try {
			logger.info("Trip historicalTotalMileage_str Hex {}",historicalTotalMileage_str);
			this.HistoricaTotalMileage_int = Integer.toUnsignedLong(ByteBuffer.wrap(Hex.decodeHex(historicalTotalMileage_str.toCharArray())).getInt());
			logger.info("HistoricaTotalMileage_int {}",HistoricaTotalMileage_int);
		} catch (DecoderException e) {
			OBDDataPacket.logger.error(e.getStackTrace().toString());
		}		
	}


	public String getHistoricalTotalFuelConsumption_str() {
		return HistoricalTotalFuelConsumption_str;
	}


	public void setHistoricalTotalFuelConsumption_str(String historicalTotalFuelConsumption_str) {
		HistoricalTotalFuelConsumption_str = historicalTotalFuelConsumption_str;
		try {
			logger.info("Trip HistoricalTotalFuelConsumption_str Hex {}",HistoricalTotalFuelConsumption_str);
			this.HistoricalTotalFuelConsumption_int = Integer.toUnsignedLong(ByteBuffer.wrap(Hex.decodeHex(HistoricalTotalFuelConsumption_str.toCharArray())).getInt());
			logger.info("HistoricalTotalFuelConsumption_int {}",HistoricalTotalFuelConsumption_int);
		} catch (DecoderException e) {
			OBDDataPacket.logger.error(e.getStackTrace().toString());
		}			
	}


	public String getHistoricalTotalDrivingTime_str() {
		return HistoricalTotalDrivingTime_str;
	}


	public void setHistoricalTotalDrivingTime_str(String historicalTotalDrivingTime_str) {
		HistoricalTotalDrivingTime_str = historicalTotalDrivingTime_str;
		try {
			logger.info("Trip HistoricalTotalDrivingTime_str Hex {}",HistoricalTotalDrivingTime_str);
			this.HistoricalDrivingTime_int = Integer.toUnsignedLong(ByteBuffer.wrap(Hex.decodeHex(HistoricalTotalDrivingTime_str.toCharArray())).getInt());
			logger.info("HistoricalDrivingTime_int {}",HistoricalDrivingTime_int);
		} catch (DecoderException e) {
			OBDDataPacket.logger.error(e.getStackTrace().toString());
		}		
	}


	public GPSData getIgn_ON_GPSData() {
		return ign_ON_GPSData;
	}


	public GPSData getIgn_OFF_GPSData() {
		return ign_OFF_GPSData;
	}

	public DateTime getIgnition_ON_Time_dt() {
		return ignition_ON_Time_dt;
	}

	public DateTime getIgnition_OFF_Time_dt() {
		return ignition_OFF_Time_dt;
	}
	
}
