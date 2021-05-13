package com.inw.adapter;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.zip.Adler32;
import java.util.zip.Checksum;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

public class DeviceCommand extends AdapterBase {

	private OBDDataPacket recvPkt = null;
	private ConnectionAck ack =null;
	private String commandString;
	
	public static DeviceCommand getWifiStatusSetCommand(boolean wifiEnabled, OBDDataPacket receivedPacket, int frameNumber) {
		DeviceCommand command =  new DeviceCommand();
		command.recvPkt = receivedPacket;
		command.ack = new ConnectionAck();
		command.ack.setDataTypeMajor("F1");
		command.ack.setServerRequestType("02");		
		command.ack.setDataLength("002A");
		command.ack.setFrameType("03");
		command.ack.setEffectiveDataLength("0007");  // F1020207000001/02
		ByteBuffer bb = ByteBuffer.allocate(2);
		bb.putShort((short)frameNumber);
		command.ack.setFrameNumber(Hex.encodeHexString(bb.array()));
		// Device parameter
		command.ack.setDeviceParameterHexStr("02070000" + (wifiEnabled?"01":"02") );
		command.commandString = "CMD -> SetWiFiStatus to : " + wifiEnabled; 

		return command;
	}

	public static DeviceCommand getWifiSSIDSetCommand(String ssid, OBDDataPacket receivedPacket, int frameNumber) {
		DeviceCommand command =  new DeviceCommand();
		command.recvPkt = receivedPacket;
		command.ack = new ConnectionAck();
		command.ack.setDataTypeMajor("F1");
		command.ack.setServerRequestType("02");
		byte[] ssidBytes = ssid.getBytes(StandardCharsets.US_ASCII);
		int baseLength = 35 + 6;  //35 is base frame size   6 is base server- > Device data length
		int dtLength =  baseLength + ssidBytes.length + 1;
		int n1 = 16 + ssidBytes.length;
		int padlen = n1 % 16 > 0 ? (16- (n1%16)) : 0;
		dtLength = dtLength + padlen;

		logger.info("getWifiSSIDSetCommand : dtLength {}", dtLength);
		String dtLengthHexStr = Hex.encodeHexString(ByteBuffer.allocate(2).putShort((short)dtLength).array());
		command.ack.setDataLength(dtLengthHexStr);
		command.ack.setFrameType("03");
		
		int effDtLength = 6 + ssidBytes.length + 1; // Here  + 1 is the length byte of SSID Bytes
		command.ack.setEffectiveDataLength(Hex.encodeHexString(ByteBuffer.allocate(2).putShort((short)effDtLength).array()));
		command.ack.setFrameNumber(Hex.encodeHexString(ByteBuffer.allocate(2).putShort((short)frameNumber).array()));
		// Device parameter
		byte[] lenBytes = new byte[] {(byte)ssid.length()};
		String ssidHex = Hex.encodeHexString(lenBytes) + Hex.encodeHexString(ssidBytes);
		
		command.ack.setDeviceParameterHexStr("02020000" + ssidHex);
		command.commandString = "CMD -> SetWiFi SSIDs to : " + ssid; 

		
		return command;
	}	
	
	public static DeviceCommand getWifiPasswordSetCommand(String password, OBDDataPacket receivedPacket, int frameNumber) {
		DeviceCommand command =  new DeviceCommand();
		command.recvPkt = receivedPacket;
		command.ack = new ConnectionAck();
		command.ack.setDataTypeMajor("F1");
		command.ack.setServerRequestType("02");
		byte[] pwdBytes = password.getBytes(StandardCharsets.US_ASCII);
		int baseLength = 35 + 6;  //35 is base frame size   6 is base server- > Device data length
		int dtLength =  baseLength + pwdBytes.length + 1;
		int n1 = 16 + pwdBytes.length;
		int padlen = n1 % 16 > 0 ? (16- (n1%16)) : 0;
		dtLength = dtLength + padlen;

		logger.info("getWifiPasswordSetCommand : dtLength {}", dtLength);
		String dtLengthHexStr = Hex.encodeHexString(ByteBuffer.allocate(2).putShort((short)dtLength).array());
		command.ack.setDataLength(dtLengthHexStr);
		command.ack.setFrameType("03");
		
		int effDtLength = 6 + pwdBytes.length + 1; // Here  + 1 is the length byte of SSID Bytes
		command.ack.setEffectiveDataLength(Hex.encodeHexString(ByteBuffer.allocate(2).putShort((short)effDtLength).array()));
		command.ack.setFrameNumber(Hex.encodeHexString(ByteBuffer.allocate(2).putShort((short)frameNumber).array()));
		// Device parameter
		byte[] lenBytes = new byte[] {(byte)password.length()};
		String passwordHex = Hex.encodeHexString(lenBytes) + Hex.encodeHexString(pwdBytes);
		
		command.ack.setDeviceParameterHexStr("02030000" + passwordHex);
		command.commandString = "CMD -> SetWiFi Password to : " + password; 
		
		return command;		
	}		

	public static DeviceCommand getGPSFrequencySetCommand(Integer  frequency, OBDDataPacket receivedPacket, int frameNumber) {
		DeviceCommand command =  new DeviceCommand();
		command.recvPkt = receivedPacket;
		command.ack = new ConnectionAck();
		command.ack.setDataTypeMajor("F1");
		command.ack.setServerRequestType("02");		// Set parameters.
		command.ack.setDataLength("002A");
		command.ack.setFrameType("03");
		command.ack.setEffectiveDataLength("0007");
		ByteBuffer bb = ByteBuffer.allocate(2);
		bb.putShort((short)frameNumber);
		command.ack.setFrameNumber(Hex.encodeHexString(bb.array()));
		// Device parameter
		//command.ack.setDeviceParameterHexStr("02020000" + (ssid?"01":"02") );
		command.commandString = "CMD -> Set GPS Frequency to : " + frequency; 
		
		return command;
	}	
	
	public static DeviceCommand getVehicleDetectionCommand( OBDDataPacket receivedPacket, int frameNumber) {
		DeviceCommand command =  new DeviceCommand();
		command.recvPkt = receivedPacket;
		command.ack = new ConnectionAck();
		command.ack.setDataTypeMajor("F1");
		command.ack.setServerRequestType("01");		 // Vehicle Detection
		command.ack.setDataLength("002A");
		command.ack.setFrameType("03");
		command.ack.setEffectiveDataLength("0002"); 
		ByteBuffer bb = ByteBuffer.allocate(2);
		bb.putShort((short)frameNumber);
		command.ack.setFrameNumber(Hex.encodeHexString(bb.array()));
		// Device parameter - for vehicle parameter, there is nothing to send as parameter.
		command.ack.setDeviceParameterHexStr("");
		return command;
	}	

	public boolean executeCommand(Socket obdSoock) {
		ack.setHeader("5555");
		ack.setVersion(recvPkt.getProtocolVersion());
		ack.setEncryptionMethod(recvPkt.getEncryptionMethod());
		ack.setReserved("0000");
		ack.setDeviceId(recvPkt.getDeviceId());
		ack.setTail("AAAA");

		String frameHeader = ack.getDataLength() + ack.getVersion() + ack.getEncryptionMethod() + ack.getReserved()
				+ ack.getDeviceId();
		logger.info( "Device Command: executeCommand {} with  frameHeader: {}", commandString ,frameHeader);
		
		String frameData = ack.getFrameType() + ack.getFrameNumber() + ack.getEffectiveDataLength() + ack.getDataTypeMajor()
				+ ack.getServerRequestType();

		if(ack.getServerRequestType().equals("02")) {  // SET Parameters request
			frameData = frameData + ack.getDeviceParamHexStr();
		}

		logger.info( "Device Command: executeCommand {} with  frameData: {}", commandString ,frameData);		
		
		try {
			
			byte[] deviceParamBytes = Hex.decodeHex(frameHeader + frameData);
		
			Checksum checksum = new Adler32();
			checksum.update(deviceParamBytes, 0, deviceParamBytes.length);


			byte[] checksumBytes = ByteBuffer.allocate(4).putInt((int) checksum.getValue()).array();
	
			String checksumString = Hex.encodeHexString(checksumBytes);
			
			String srvrReqPacket = frameData + checksumString;
			if(srvrReqPacket.length() % 32 !=0 ) { // bytes to encrypt should be a multiple of 16 length
				String padd = "F".repeat(32 - (srvrReqPacket.length() % 32) );
				srvrReqPacket = srvrReqPacket + padd;
			}
			
			logger.info("Server -> Device request Packet " + srvrReqPacket);
			
			String mcuKey = MCUKeyManager.getInstance().getMCUKeyForIMEINumber(recvPkt.getDeviceId());		
			String encryptedPacket = Hex.encodeHexString(encrypt(Hex.decodeHex(srvrReqPacket), Hex.decodeHex(mcuKey)));
	
			String finalReqPacket = ack.getHeader() + frameHeader + encryptedPacket + "AAAA";
			logger.info("Server -> Device Encrypted request Packet " + finalReqPacket);

			DataOutputStream dout = new DataOutputStream(obdSoock.getOutputStream());
			dout.write(Hex.decodeHex(finalReqPacket));
			dout.flush();
		} catch (DecoderException e) {
			logger.error("Exception occured: ",e);
		} catch (IOException e) {
			logger.error("Exception occured: ",e);
		}
		catch (Exception e) {
			logger.error("Exception occured: ",e);
		}		
		
		return false;
	}
	

	@Override
	public String toString() {
		return "ConnectionAck [header=" + ack.getHeader() + ", dataLength=" + ack.getDataLength() + ", version=" + ack.getVersion()
				+ ", messageType=" + ack.getMessageType() + ", reserved=" + ack.getReserved() + ", deviceId=" + ack.getDeviceId() + ", frameType="
				+ ack.getFrameType() + ", frameNumber=" + ack.getFrameNumber() + ", effectiveDataLength=" + ack.getEffectiveDataLength()
				+ ", connctionType=" + ack.getEncryptionMethod() + ", checksum=" + ack.getChecksum() + ", tail=" + ack.getTail() + "]";
	}

}
