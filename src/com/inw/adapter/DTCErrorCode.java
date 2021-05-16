package com.inw.adapter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Arrays;
import java.util.Hashtable;

import org.apache.commons.codec.binary.Hex;
import org.joda.time.DateTime;

import com.thingworx.metadata.FieldDefinition;
import com.thingworx.types.BaseTypes;
import com.thingworx.types.InfoTable;
import com.thingworx.types.collections.ValueCollection;

public class DTCErrorCode extends AdapterBase implements OBDDataSection {

	private DateTime dtcErrorOccuranceTime_dt;
	private String dtcErrorOccuranceTime_str;

	InfoTable dtcErroCodesInfoTbl = new InfoTable();
	private Hashtable<String,String> dtcOBDFaultCodes = new Hashtable<String,String>();
	private Hashtable<String,String> dtcPrivateFaultCodes = new Hashtable<String,String> ();
	private String dtcErroCode_str;
	
	public DTCErrorCode() {
		super();
		FieldDefinition Field1 = new FieldDefinition("ErroCode", BaseTypes.STRING);
		FieldDefinition Field2 = new FieldDefinition("ErrDescription", BaseTypes.STRING);		
		dtcErroCodesInfoTbl.addField(Field1);
		dtcErroCodesInfoTbl.addField(Field2);

	}
	
	public String toString() {
		
		String str = " \n DTC Error occurance time =" + dtcErrorOccuranceTime_dt 
						+ " \n dtcOBDFaultCodes =" + dtcOBDFaultCodes
						+ " \n dtcPrivateFaultCodes =" + dtcPrivateFaultCodes;		
		return str;		
	}
	
	public void setDTCErrorOccuranceTime_str(String hexStr) {
		this.dtcErrorOccuranceTime_str = hexStr;
		logger.info("DTCErrorOccuranceTime_str {}",dtcErrorOccuranceTime_str);
		this.dtcErrorOccuranceTime_dt = this.getDateFromHexString(dtcErrorOccuranceTime_str);
	}
	public DateTime getDTCErroCodeTime() {
		
		return this.dtcErrorOccuranceTime_dt;
	}
	public InfoTable getDTCErroCodeInfoTable() {
		
		return this.dtcErroCodesInfoTbl;
	}
	public void setDTCErrorCode_str(String hexStr) {
		this.dtcErroCode_str = hexStr;
		logger.info("dtcErroCode_str {}",dtcErroCode_str);
		try {
			byte[] dtcBytes = Hex.decodeHex(hexStr.toCharArray());
			int obdCodeLen = dtcBytes[0];
			{
				for(int i =1 ; i <= obdCodeLen * 3; i+=3) {
					byte[] dtcCode = Arrays.copyOfRange(dtcBytes, i, i+2);
					byte stateCode = dtcBytes[i+2];
					if(stateCode == 0) {
						String errorCode  = "P" + Hex.encodeHexString(dtcCode);
						ValueCollection valcol = new ValueCollection();
						valcol.SetStringValue("ErrorCode", errorCode);
						String descr = getDTCErrorCodesMap().get(errorCode);
						valcol.SetStringValue("ErrorDescription", descr);
						dtcErroCodesInfoTbl.addRow(valcol);
						dtcOBDFaultCodes.put(errorCode, descr);
					}
				}
			}
			{
				int prvCodeLen = dtcBytes[obdCodeLen+1];
				for(int i =obdCodeLen+2 ; i <= (obdCodeLen + 2 + prvCodeLen *  3); i+=3) {
					//byte[] dtcCode = Arrays.copyOfRange(dtcBytes, i, i+2);
					byte stateCode = dtcBytes[i+2];
					if(stateCode == 0) {
						//String errorCode  = "P" + Hex.encodeHexString(dtcCode);
						//dtcPrivateFaultCodes.put(errorCode, getDTCErrorCodesMap().get(errorCode));
					}				
				}
			}
			
		} catch (Exception e) {
			logger.error("Error occured : ",e);
		}
		
	}
	
	
	private static Hashtable<String, String>  dtcErroCodeMap = null;
	
	public static Hashtable<String, String> getDTCErrorCodesMap() {
		if(dtcErroCodeMap == null) {
			dtcErroCodeMap = new Hashtable<String,String>();
	        try {
	        		BufferedReader in = new BufferedReader(new FileReader("obd-trouble-codes-master/obd-trouble-codes.csv"));
	        		String line;
	
			        while ((line = in.readLine()) != null) {
			            String columns[] = line.split(",");
			            if (!dtcErroCodeMap.containsKey(columns[0])) {
			            	dtcErroCodeMap.put(columns[0], columns[1]);
			            }
			        }	        	
					in.close();
			} catch (Exception e) {
				logger.error("Erro occured : ", e);
			}
		}
		return dtcErroCodeMap;
	}
}
