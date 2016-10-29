package communication;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * This class manages an UDP socket connection. 
 *
 */
public class SocketConnection {
	private DatagramSocket serverSocket;
	private InetAddress inetAddress;
	private int port;
	
	
	/**
	 * Constructor for the SocketConnection. 
	 * This method creates an UDP socket listening to the LOCAL_PORT specified in the ConnectionParams class.
	 * 
	 * @throws SocketException When the creation of the socket fail.
	 * @throws UnknownHostException When the creation of the socket fail.
	 */
	public SocketConnection() throws SocketException, UnknownHostException {
		serverSocket = new DatagramSocket(ConnectionParams.LOCAL_PORT);
	}
	
	/**
	 * This method is used to send a message through an UDP packet.
	 * 
	 * @param address The destination address.
	 * @param port The listening port of the destination device.
	 * @param data The message to send.
	 * @return boolean true if success, false otherwise.
	 */
	public boolean send(String address, int port, String data) {
		byte[] sendData = data.getBytes();
		
		this.port = port;
		try {
			this.inetAddress = InetAddress.getByName(address);
		} catch (UnknownHostException e1) {
			e1.printStackTrace();
			return false;
		}
		
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, inetAddress, port); 
		try {
			serverSocket.send(sendPacket);
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	
	/**
	 * This method wait for a reception of an UDP packet. It is a blocking receive.
	 * 
	 * @return String The received message.
	 */
	public String receive() {
		
		byte[] receiveData = new byte[1024];
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		try {
			serverSocket.receive(receivePacket);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		this.port = receivePacket.getPort();
		this.inetAddress = receivePacket.getAddress();
		
		String dataRcv = new String(receivePacket.getData());
		dataRcv = dataRcv.trim();		
		return dataRcv;
	}
	
	/**
	 * This method is used to get the IP address of the last device a connection was made with it. 
	 * It is the IP of the device that sent the last received message or
	 * the destination IP of the last message sent. The one that happened last.
	 * 
	 * @return String The IP address.
	 */
	public String remoteIP() {
		return inetAddress.toString().substring(1);
	}
	
	
	/**
	 * This method is used to get the port of the last device a connection was made with it. 
	 * It is the receiving port of the last received message or
	 * the destination port of the last message sent. The one that happened last.
	 * 
	 * @return String The IP address.
	 */
	public int remotePort() {
		return this.port;
	}

	
	/**
	 * This method close the socket.
	 * 
	 */
	public void close() {
		serverSocket.close();
	}
}





