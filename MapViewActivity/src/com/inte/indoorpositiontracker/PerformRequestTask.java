/**
 *  Filename: PerformRequestTask.java (in org.repin.android.net)
 *  This file is part of the Redpin project.
 * 
 *  Redpin is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published
 *  by the Free Software Foundation, either version 3 of the License, or
 *  any later version.
 *
 *  Redpin is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with Redpin. If not, see <http://www.gnu.org/licenses/>.
 *
 *  (c) Copyright ETH Zurich, Pascal Brogle, Philipp Bolliger, 2010, ALL RIGHTS RESERVED.
 * 
 *  www.redpin.org
 */
package com.inte.indoorpositiontracker;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.AsyncTask;
import android.util.Log;

/**
 * PerformRequestTask is a specialized {@link AsyncTask} to perform an server
 * request on the background
 * 
 * @see AsyncTask
 * @author Pascal Brogle (broglep@student.ethz.ch)
 * 
 */
public class PerformRequestTask extends
		AsyncTask<String, String, Void> {

	public static int port = 8000;
	public static String host = "192.168.0.101";// "10.0.2.2" is the host that runs the emulator
	public static String httpProtocol = "http://";

	Socket socket = null;
	BufferedReader in = null;
	BufferedWriter out = null;

	/**
	 * Performs an server request on the background
	 * 
	 * @param params
	 *            Request to be performed (only the first is used)
	 * @return {@link Response} from the server
	 */
	@Override
	protected Void doInBackground(String... params) {
		boolean socketTaskDone = false;
		String response = "";
		if(params[0].equals("SOCKET_BIND_SEND")) { // pasar a enum

			try {                    
	            socket = new Socket(host,port);  
	        	publishProgress("connected");
	        	in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

				out.write(params[1] + "\n");
				out.flush();
				socketTaskDone = true;
				response = in.readLine();
				publishProgress("SOCKET_RESPONSE: " + response);
		    } catch (UnknownHostException e) {      
		        publishProgress("SOCKET_FAIL: UnknownHostException");
		    } catch (IOException e) {                   
		        publishProgress("SOCKET_FAIL: IOException");
		    } finally {
		    	if(out != null)	{
	            	try {
						out.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	        	}
	        	if(in != null)
					try {
						in.close();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
	            
	        	if(socket != null) {
	        		try {
						socket.close();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
	        	}
		    }
		}

		if(socketTaskDone)
			publishProgress("SOCKET_TASK_DONE");
		
		return null;
	}

	@Override
	protected void onProgressUpdate(String... progress) {
		System.out.println("onProgressUpdate: " + progress[0]);
	}


	@Override
	protected void onPostExecute(Void result) {
		System.out.println("SOCKET_TASK_END");
		cleanup();
	}

	public void cleanup() {
		socket = null;
		in = null;
		out = null;
	}

}
