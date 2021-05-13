package com.inw.adapter;

public class ConnectionAck {
	private String header;
	private String dataLength;
	private String version;
	private String messageType;
	private String reserved;
	private String deviceId;
	private String frameType;
	private String frameNumber;
	private String effectiveDataLength;
	private String encryptionMethod;
	private String checksum;
	private String tail;
	private String sessionType;
	private String utcTime;
	private String dataTypeMajor;
	private String dataTypeMinor;
	
	private String serverRequestType;
	private String deviceParameterHexStr;
	public String getDataTypeMajor() {
		return dataTypeMajor;
	}
	public void setDataTypeMajor(String dataTypeMajor) {
		this.dataTypeMajor = dataTypeMajor;
	}
	public String getDataTypeMinor() {
		return dataTypeMinor;
	}
	public void setDataTypeMinor(String dataTypeMinor) {
		this.dataTypeMinor = dataTypeMinor;
	}
	
	public String getHeader() {
		return header;
	}
	public void setHeader(String header) {
		this.header = header;
	}
	public String getDataLength() {
		return dataLength;
	}
	public void setDataLength(String dataLength) {
		this.dataLength = dataLength;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getMessageType() {
		return messageType;
	}
	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	public String getReserved() {
		return reserved;
	}
	public void setReserved(String reserved) {
		this.reserved = reserved;
	}
	public String getDeviceId() {
		return deviceId;
	}
	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	public String getFrameType() {
		return frameType;
	}
	public void setFrameType(String frameType) {
		this.frameType = frameType;
	}
	public String getFrameNumber() {
		return frameNumber;
	}
	public void setFrameNumber(String frameNumber) {
		this.frameNumber = frameNumber;
	}
	public String getEffectiveDataLength() {
		return effectiveDataLength;
	}
	public void setEffectiveDataLength(String effectiveDataLength) {
		this.effectiveDataLength = effectiveDataLength;
	}
	public String getEncryptionMethod() {
		return encryptionMethod;
	}
	public void setEncryptionMethod(String connctionType) {
		this.encryptionMethod = connctionType;
	}
	public String getChecksum() {
		return checksum;
	}
	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}
	public String getTail() {
		return tail;
	}
	public void setTail(String tail) {
		this.tail = tail;
	}
	public ConnectionAck() {
		super();
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "ConnectionAck [header=" + header + ", dataLength=" + dataLength + ", version=" + version
				+ ", messageType=" + messageType + ", reserved=" + reserved + ", deviceId=" + deviceId + ", frameType="
				+ frameType + ", frameNumber=" + frameNumber + ", effectiveDataLength=" + effectiveDataLength
				+ ", connctionType=" + encryptionMethod + ", checksum=" + checksum + ", tail=" + tail + "]";
	}
	public String getSessionType() {
		return sessionType;
	}
	public void setSessionType(String sessionType) {
		this.sessionType = sessionType;
	}
	public String getUtcTime() {
		return utcTime;
	}
	public void setUtcTime(String utcTime) {
		this.utcTime = utcTime;
	}
	public String getServerRequestType() {
		return serverRequestType;
	}
	public void setServerRequestType(String serverRequestType) {
		this.serverRequestType = serverRequestType;
	}
	public String getDeviceParamHexStr() {
		return deviceParameterHexStr;
	}
	public void setDeviceParameterHexStr(String setParamBinary) {
		this.deviceParameterHexStr = setParamBinary;
	}
//	public String getPaddingHexStr() {
//		return paddingHexStr;
//	}
//	public void setPaddingHexStr(String paddingHexStr) {
//		this.paddingHexStr = paddingHexStr;
//	}
	
	
}
