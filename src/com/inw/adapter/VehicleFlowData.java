package com.inw.adapter;

import java.nio.ByteBuffer;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.joda.time.DateTime;

public class VehicleFlowData extends AdapterBase implements OBDDataSection{
	
	private String UTC_Time;
	private String DataNumber;
	private String GPSposition_str;
	private String RPM;
	private String Speed;
	private String EngineCoolantTemperature;
	private String ThrottlePosition;
	private String EngineDuty;	
	private String IntakeAirFlow;
	private String IntakeAirTemperature;
	private String IntakeAirPressure;
	private String BatteryVoltage;
	private String FLIFuelLevelInput;
	private String DistanceSinceDTCClear;	
	private String MalFuncIndiLamp;
	private String HistoricalTotalMileage;
	private String HistoricalTotalFuelConsumption;
	private String HistoricalTotalDrivingTime;
	private String ReservedData;
	
	private Integer UTC_Time_int;
	private Integer DataNumber_int_int;
	private Integer RPM_int;
	private Integer Speed_int;
	private Integer EngineCoolantTemperature_int;
	private Float ThrottlePosition_float;
	private Integer EngineDuty_int;	
	private Integer IntakeAirFlow_int;
	private Integer IntakeAirTemperature_int;
	private Integer IntakeAirPressure_int;
	private Float BatteryVoltage_float;
	private Float FLIFuelLevelInput_float;
	private Integer DistanceSinceDTCClear_int;	
	private Integer MalFuncIndiLamp_int;
	private Long HistoricalTotalMileage_int;
	private Integer HistoricalTotalFuelConsumption_int;
	private Integer HistoricalTotalDrivingTime_int;
	private GPSData vehGPS_data;
	private DateTime utc_Time_Dt;
	
	@Override
	public String toString() {
		String str = " \n UTC_Time=" + utc_Time_Dt 
		+ " \n RPM=" + RPM_int
		+ " \n Speed=" + Speed_int 
		+ " \n BatteryVoltage=" + BatteryVoltage_float + " V "
		+ " \n FLIFuelLevelInput=" + FLIFuelLevelInput_float + " % "
		+ " \n DistanceSinceDTCClear=" + DistanceSinceDTCClear_int + " KM "
		+ " \n EngineCoolantTemperature=" + EngineCoolantTemperature_int + " Degree Celcius"
		+ " \n HistoricalTotalMileage=" + HistoricalTotalMileage_int 
		+ " \n Veh GPS Data : " + vehGPS_data.toString();
		//+ ", DataNumber=" + DataNumber 
		//+ " \n " + ", GPSposition=" + GPSposition 
		//+ " \n " + ", ThrottlePosition=" + ThrottlePosition_float + ", DataNumber=" + DataNumber 
		//+ " \n " + ", EngineDuty=" + EngineDuty + ", IntakeAirFlow=" + IntakeAirFlow 
		//+ " \n " + ", IntakeAirTemperature=" + IntakeAirTemperature  
		//+ ", MalFuncIndiLamp=" + MalFuncIndiLamp 
		//+ " \n " + ", HistoricalTotalDrivingTime=" + HistoricalTotalDrivingTime_int + ", ReservedData=" + ReservedData;			
		return str;
	}
	
	public String getUTC_Time() {
		return UTC_Time;
	}


	public void setUTC_Time(String UTC_Time) {
		this.UTC_Time = UTC_Time;
		this.utc_Time_Dt = this.getDateFromHexString(UTC_Time);
	}


	public String getDataNumber() {
		return DataNumber;
	}


	public void setDataNumber(String DataNumber) {
		this.DataNumber = DataNumber;
	}


	public String getGPSposition_str() {
		return GPSposition_str;
	}


	public void setGPSposition_str(String GPSposition) {
		this.GPSposition_str = GPSposition;
		this.vehGPS_data = new GPSData(GPSposition);
	}
	



	public String getRPM() {
		return RPM;
	}


	public void setRPM(String RPM) {
		this.RPM = RPM;
		try {
			byte[] hexByte = Hex.decodeHex(RPM);
			logger.info("RPM = " + RPM + " hexByte len =" + hexByte.length);
			this.RPM_int = (int)ByteBuffer.wrap(hexByte).getShort();
		} catch (DecoderException e) {
			logger.error("Exception occured: ",e);
		}		
	}


	public String getSpeed() {
		return Speed;
	}


	public void setSpeed(String Speed) {
		this.Speed = Speed;
		try {
			this.Speed_int = (int)ByteBuffer.wrap(Hex.decodeHex(Speed)).getShort();
		} catch (DecoderException e) {
			logger.error("Exception occured: ",e);
		}
	}


	public String getEngineCoolantTemperature() {
		return EngineCoolantTemperature;
	}


	public void setEngineCoolantTemperature(String EngineCoolantTemperature) {
		this.EngineCoolantTemperature = EngineCoolantTemperature;
		try {
			final int CoolantOffSet = -40;
			this.EngineCoolantTemperature_int = (int)ByteBuffer.wrap(Hex.decodeHex(EngineCoolantTemperature)).getShort();
			this.EngineCoolantTemperature_int = this.EngineCoolantTemperature_int + CoolantOffSet;
		} catch (DecoderException e) {
			logger.error("Exception occured: ",e);
		}
	}


	public String getThrottlePosition() {
		return ThrottlePosition;
	}


	public void setThrottlePosition(String ThrottlePosition) {
		this.ThrottlePosition = ThrottlePosition;
		try {
			this.ThrottlePosition_float = (float)ByteBuffer.wrap(Hex.decodeHex(ThrottlePosition)).getShort();
		} catch (DecoderException e) {
			logger.error("Exception occured: ",e);
		}		
	}


	public String getEngineDuty() {
		return EngineDuty;
	}


	public void setEngineDuty(String EngineDuty) {
		this.EngineDuty = EngineDuty;
	}


	public String getIntakeAirFlow() {
		return IntakeAirFlow;
	}


	public void setIntakeAirFlow(String IntakeAirFlow) {
		this.IntakeAirFlow = IntakeAirFlow;
	}


	public String getIntakeAirTemperature() {
		return IntakeAirTemperature;
	}


	public void setIntakeAirTemperature(String IntakeAirTemperature) {
		this.IntakeAirTemperature = IntakeAirTemperature;
	}


	public String getIntakeAirPressure() {
		return IntakeAirPressure;
	}


	public void setIntakeAirPressure(String IntakeAirPressure) {
		this.IntakeAirPressure = IntakeAirPressure;
	}


	public String getBatteryVoltage() {
		return BatteryVoltage;
	}


	public void setBatteryVoltage(String BatteryVoltage) {
		this.BatteryVoltage = BatteryVoltage;
		try {
			this.BatteryVoltage_float = (float) (ByteBuffer.wrap(Hex.decodeHex(BatteryVoltage)).getShort() * 0.1);
		} catch (DecoderException e) {
			logger.error("Exception occured: ",e);
		}		
	}


	public String getFLIFuelLevelInput() {
		return FLIFuelLevelInput;
	}


	public void setFLIFuelLevelInput(String FLIFuelLevelInput) {
		this.FLIFuelLevelInput = FLIFuelLevelInput;
		try {
			this.FLIFuelLevelInput_float = (float) (ByteBuffer.wrap(Hex.decodeHex(FLIFuelLevelInput)).getShort() * 0.01);
		} catch (DecoderException e) {
			logger.error("Exception occured: ",e);
		}		
	}


	public String getDistanceSinceDTCClear() {
		return DistanceSinceDTCClear;
	}


	public void setDistanceSinceDTCClear(String DistanceSinceDTCClear) {
		this.DistanceSinceDTCClear = DistanceSinceDTCClear;
		try {
			logger.info("KM->DTC Hex {}",DistanceSinceDTCClear);
			this.DistanceSinceDTCClear_int = Short.toUnsignedInt(ByteBuffer.wrap(Hex.decodeHex(DistanceSinceDTCClear)).getShort());
			logger.info("DistanceSinceDTCClear_int {}",DistanceSinceDTCClear_int);
		} catch (DecoderException e) {
			OBDDataPacket.logger.error(e.getStackTrace().toString());
		}			
	}


	public String getMalFuncIndiLamp() {
		return MalFuncIndiLamp;
	}


	public void setMalFuncIndiLamp(String MalFuncIndiLamp) {
		this.MalFuncIndiLamp = MalFuncIndiLamp;
	}


	public String getHistoricalTotalMileage() {
		return HistoricalTotalMileage;
	}


	public void setHistoricalTotalMileage(String HistoricalTotalMileage) {
		this.HistoricalTotalMileage = HistoricalTotalMileage;
		try {
			logger.info("HistoricalTotalMileage Hex {}",this.HistoricalTotalMileage);			
			this.HistoricalTotalMileage_int = Integer.toUnsignedLong(ByteBuffer.wrap(Hex.decodeHex(HistoricalTotalMileage)).getInt());
			logger.info("HistoricalTotalMileage_int {}",this.HistoricalTotalMileage_int);
		} catch (DecoderException e) {
			OBDDataPacket.logger.error(e.getStackTrace().toString());
		}			
	}


	public String getHistoricalTotalFuelConsumption() {
		return HistoricalTotalFuelConsumption;
	}


	public void setHistoricalTotalFuelConsumption(String HistoricalTotalFuelConsumption) {
		this.HistoricalTotalFuelConsumption = HistoricalTotalFuelConsumption;
		try {
			this.HistoricalTotalFuelConsumption_int = ByteBuffer.wrap(Hex.decodeHex(HistoricalTotalFuelConsumption)).getInt();
		} catch (DecoderException e) {
			OBDDataPacket.logger.error(e.getStackTrace().toString());
		}			
	}


	public String getHistoricalTotalDrivingTime() {
		return HistoricalTotalDrivingTime;
	}


	public void setHistoricalTotalDrivingTime(String HistoricalTotalDrivingTime) {
		this.HistoricalTotalDrivingTime = HistoricalTotalDrivingTime;
		try {
			this.HistoricalTotalDrivingTime_int = ByteBuffer.wrap(Hex.decodeHex(HistoricalTotalDrivingTime)).getInt();
		} catch (DecoderException e) {
			OBDDataPacket.logger.error(e.getStackTrace().toString());
		}		
	}


	public String getReservedData() {
		return ReservedData;
	}


	public void setReservedData(String ReservedData) {
		this.ReservedData = ReservedData;
	}
	public Integer getUTC_Time_int() {
		return UTC_Time_int;
	}


	public void setUTC_Time_int(Integer UTC_Time_int) {
		this.UTC_Time_int = UTC_Time_int;
	}


	public Integer getDataNumber_int_int() {
		return DataNumber_int_int;
	}


	public void setDataNumber_int_int(Integer DataNumber_int_int) {
		this.DataNumber_int_int = DataNumber_int_int;
	}


	public Integer getRPM_int() {
		return RPM_int;
	}


	public void setRPM_int(Integer RPM_int) {
		this.RPM_int = RPM_int;
	}


	public Integer getSpeed_int() {
		return Speed_int;
	}


	public void setSpeed_int(Integer Speed_int) {
		this.Speed_int = Speed_int;
	}


	public Integer getEngineCoolantTemperature_int() {
		return EngineCoolantTemperature_int;
	}


	public void setEngineCoolantTemperature_int(Integer EngineCoolantTemperature_int) {
		this.EngineCoolantTemperature_int = EngineCoolantTemperature_int;
	}


	public Float getThrottlePosition_float() {
		return ThrottlePosition_float;
	}


	public void setThrottlePosition_float(Float ThrottlePosition_float) {
		this.ThrottlePosition_float = ThrottlePosition_float;
	}


	public Integer getEngineDuty_int() {
		return EngineDuty_int;
	}


	public void setEngineDuty_int(Integer EngineDuty_int) {
		this.EngineDuty_int = EngineDuty_int;
	}


	public Integer getIntakeAirFlow_int() {
		return IntakeAirFlow_int;
	}


	public void setIntakeAirFlow_int(Integer IntakeAirFlow_int) {
		this.IntakeAirFlow_int = IntakeAirFlow_int;
	}


	public Integer getIntakeAirTemperature_int() {
		return IntakeAirTemperature_int;
	}


	public void setIntakeAirTemperature_int(Integer IntakeAirTemperature_int) {
		this.IntakeAirTemperature_int = IntakeAirTemperature_int;
	}


	public Integer getIntakeAirPressure_int() {
		return IntakeAirPressure_int;
	}


	public void setIntakeAirPressure_int(Integer IntakeAirPressure_int) {
		this.IntakeAirPressure_int = IntakeAirPressure_int;
	}


	public Float getBatteryVoltage_float() {
		return BatteryVoltage_float;
	}


	public void setBatteryVoltage_float(Float BatteryVoltage_float) {
		this.BatteryVoltage_float = BatteryVoltage_float;
	}


	public Float getFLIFuelLevelInput_float() {
		return FLIFuelLevelInput_float;
	}


	public void setFLIFuelLevelInput_int(Float FLIFuelLevelInput_float) {
		this.FLIFuelLevelInput_float = FLIFuelLevelInput_float;
	}


	public Integer getDistanceSinceDTCClear_int() {
		return DistanceSinceDTCClear_int;
	}


	public void setDistanceSinceDTCClear_int(Integer DistanceSinceDTCClear_int) {
		this.DistanceSinceDTCClear_int = DistanceSinceDTCClear_int;
	}


	public Integer getMalFuncIndiLamp_int() {
		return MalFuncIndiLamp_int;
	}


	public void setMalFuncIndiLamp_int(Integer MalFuncIndiLamp_int) {
		this.MalFuncIndiLamp_int = MalFuncIndiLamp_int;
	}


	public Long getHistoricalTotalMileage_int() {
		return HistoricalTotalMileage_int;
	}


	public void setHistoricalTotalMileage_int(Long HistoricalTotalMileage_int) {
		this.HistoricalTotalMileage_int = HistoricalTotalMileage_int;
	}


	public Integer getHistoricalTotalFuelConsumption_int() {
		return HistoricalTotalFuelConsumption_int;
	}


	public void setHistoricalTotalFuelConsumption_int(Integer HistoricalTotalFuelConsumption_int) {
		this.HistoricalTotalFuelConsumption_int = HistoricalTotalFuelConsumption_int;
	}


	public Integer getHistoricalTotalDrivingTime_int() {
		return HistoricalTotalDrivingTime_int;
	}


	public void setHistoricalTotalDrivingTime_int(Integer HistoricalTotalDrivingTime_int) {
		this.HistoricalTotalDrivingTime_int = HistoricalTotalDrivingTime_int;
	}
	public GPSData getVehGPS_Data() {
		return vehGPS_data;
	}		
}