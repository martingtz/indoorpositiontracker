package app;

import java.net.SocketException;
import java.net.UnknownHostException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import communication.ConnectionParams;
import communication.Frame;
import communication.Parser;
import communication.SocketConnection;
import navigation.Fingerprint;
import storage.DatabaseManager;
import storage.QueryHandler;

/**
 * This program helps a user to follow a prespecified navigation route.
 * 
 * First a device sends fingerprints of the environment and this application
 * stores them in a database and consider them as identification info of a
 * location. Then the device can send scanning info of its current location and
 * this application will try to find a match between this new info and the
 * previously stored fingerprint, the meaning is to know where currently is the
 * device and send him commands that will help him to follow a route and arrive
 * to a destination.
 *
 */

public class Main {
	// Lista con listas hashmap
	public static HashMap<String, ArrayList<Fingerprint>> listMap = new HashMap<String, ArrayList<Fingerprint>>();

	public static void main(String[] args) {
		System.out.println("---- Init ----");
		try {
			go();
		} catch (SocketException | UnknownHostException e) {
			System.err.println("Error creating a socket connection");
			e.printStackTrace();
		}
		System.out.println("---- End ----");
	}

	/**
	 * This is the core method of the application.
	 * 
	 * The method creates a socket and a connection with the database. The
	 * database address, name and user are specified in the
	 * <b>ConnectionParams</b> class. After that waits for receive a message,
	 * parses it and process it depending of the type of frame that was
	 * received. The types are: Handshake, Fingerprint and Scanning frame.
	 * 
	 * @throws SocketException
	 *             When the creation of the socket fail.
	 * @throws UnknownHostException
	 *             When the creation of the socket fail.
	 */
	public static void go() throws SocketException, UnknownHostException {
		SocketConnection socket = new SocketConnection();
		DatabaseManager dbManager;
		String rcv_msg = new String();
		String remote_ip = new String();
		Parser msg_parser = new Parser();
		Frame frame;

		try {

			dbManager = new DatabaseManager(ConnectionParams.dbAddress, ConnectionParams.dbName,
					ConnectionParams.dbUser, ConnectionParams.dbPass);
			System.out.println("Connection with DB success");

			// cargamos todos los fingerprints
			QueryHandler query = new QueryHandler(dbManager);
			ResultSet rs = dbManager.readDB("SELECT BSSID FROM userdevice");
			boolean flag = false;
			while (rs.next()) {
				flag = true;
				String bssid = rs.getString("BSSID");
				Main.listMap.put(bssid, query.getFingerprintListFromBD(bssid));
			}
			if (flag)
				System.out.println("Todos los fingerprints han sido cargados en el HashMap");
			else
				System.out.println("No existen fingerprints ");

		} catch (SQLException e) {
			System.err.println("Error connecting with Database");
			e.printStackTrace();
			return;
		}

		while (true) {

			rcv_msg = socket.receive();
			frame = msg_parser.getFrame(rcv_msg);

			System.out.println("----------------------------------------------------------------------");
			remote_ip = socket.remoteIP();
			System.out.println(frame.toString() + "\nIP: " + remote_ip + "\n");

			try {
				frame.process(socket, dbManager);

			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
