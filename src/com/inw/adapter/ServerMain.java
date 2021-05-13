package com.inw.adapter;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

import com.thingworx.sdk.simplething.ThingWorxClientForOBD;

public class ServerMain extends AdapterBase {

	private static ReceivePackets receivePackets = new ReceivePackets();
	private static int ServerPort = 9090;
	private static boolean shouldStopServer = false;

	public static void main(String[] args) throws IOException {
		 
		try {
			FileInputStream fileInputStream = new FileInputStream("config/adapterconfig.xml");
		    Properties props = new Properties();
		    props.loadFromXML(fileInputStream);
		 
		    ServerPort = Integer.parseInt(props.getProperty("AdapterServerPort"));
		    String thingWorxServerURL = props.getProperty("ThingWorxServerURL");
		    String thingWorxAppKey = props.getProperty("ThingWorxApplicationKey");
		    
		    ThingWorxClientForOBD.initClient(thingWorxServerURL, thingWorxAppKey);		    
		    fileInputStream.close();
		} catch (Exception ex) {
		    logger.error("Error occured during startup : ",ex );
		    return;
		}		
		
		
		logger.info("Adapter Server started listening on port {}",ServerPort );
		
		ServerSocket servSock = new ServerSocket(ServerPort);
		
		
		while (!shouldStopServer) {
			logger.info("Server waiting for new connections from OBD Devices" );
			Socket cliSock = servSock.accept(); 
			logger.info("Accepted connection from device {}", cliSock.getRemoteSocketAddress());
			
			Thread commThread = new Thread (()->{
				byte[] resultBuff = new byte[0];
			    byte[] buff = new byte[1024];
			    int k = -1;
			    try {
					while((k = cliSock.getInputStream().read(buff, 0, buff.length)) > -1) {
					    try {
							resultBuff = new byte[k];
							System.arraycopy(buff, 0, resultBuff, 0, k);
							buff = new byte[1024];
							//logger.info("Received {} bytes from {} Encrypted Bytes : {}",k, cliSock.getRemoteSocketAddress()
													//,Hex.encodeHexString(resultBuff));
							
							ThingWorxClientForOBD twxClient = ThingWorxClientForOBD.getInstance();

							OBDDataPacket newOBDPacket = receivePackets.processPacket(resultBuff, cliSock);
							if(newOBDPacket.getMessageType().equals("03") 
									|| newOBDPacket.getMessageType().equalsIgnoreCase("0C")
									|| newOBDPacket.getMessageType().equals("01")) {
								twxClient.processOBDDataPacket(newOBDPacket, cliSock);
							}
							
					    } catch (Exception e) {
					    	logger.error("Exception occured: ",e);
						}
					}
				} catch (IOException e) {
					logger.error("Exception occured: ",e);
				}				
			});
			commThread.start();
			//my test code		   
		}
		servSock.close();

	}

}
