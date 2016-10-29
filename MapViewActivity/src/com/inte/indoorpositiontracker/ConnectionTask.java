package com.inte.indoorpositiontracker;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import android.os.AsyncTask;
import android.util.Log;

public class ConnectionTask extends AsyncTask<String, Void, String> {

	String dstAddress;
	int dstPort;
	int srcPort;
	String response = new String();
	DatagramSocket connectedSocket;

	ConnectionTask(String server, int server_port, int localPort) {
		dstAddress = server;
		dstPort = server_port;
		srcPort = localPort;
	}

	@Override
	protected String doInBackground(String... arg0) {
		String response = null;
		String[] array = arg0[0].split(",");
		sendUDP(arg0[0]);
		String opc;
		opc = array[0];
		switch (opc) {
		case "f":
			break;

		case "s":
			response = receiveUDP();
			break;

		case "#":
			response = receiveUDP();
			break;
		}

		return response;
	}

	@Override
	protected void onPostExecute(String result) {
		// Log.i("response", "Server Response: " + result);
		super.onPostExecute(result);
	}

	public void sendUDP(String data) {
		try {
			connectedSocket = new DatagramSocket();
			InetAddress local = InetAddress.getByName(dstAddress);
			int msg_length = data.length();
			byte[] payload = data.getBytes();
			DatagramPacket p = new DatagramPacket(payload, msg_length, local, dstPort);
			connectedSocket.send(p);
			connectedSocket.close();
		} catch (SocketException e) {
			Log.i("exceptions", "1:" + e.toString());
			e.printStackTrace();
			connectedSocket.close();
		} catch (UnknownHostException e) {
			Log.i("exceptions", "2:" + e.toString());
			e.printStackTrace();
			connectedSocket.close();
		} catch (IOException e) {
			Log.i("exceptions", "3:" + e.toString());
			e.printStackTrace();
			connectedSocket.close();
		}

	}

	public String receiveUDP() {
		try {
			connectedSocket = new DatagramSocket(srcPort);
			byte[] receiveData = new byte[1024];
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			connectedSocket.setSoTimeout(1000);
			connectedSocket.receive(receivePacket);
			String dataRcv = new String(receivePacket.getData());
			dataRcv = dataRcv.trim();
			connectedSocket.close();
			Log.i("logs", "El servidor dice: "+dataRcv);
			return dataRcv;
		} catch (SocketException e) {
			Log.i("exceptions", "4:" + e.toString());
			e.printStackTrace();
			connectedSocket.close();
			return null;
		} catch (IOException e) {
			
			Log.i("exceptions", "5:" +e.toString());
			e.printStackTrace();
			connectedSocket.close();
			return null;
		}

	}

}
