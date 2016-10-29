package communication;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import app.Main;
import navigation.Fingerprint;
import storage.DatabaseManager;

/**
 * This class represents a Handshake frame, which is composed of:\n <b>'#' |
 * BSSID |</b>
 *
 */
public class HandshakeFrame implements Frame {
	public static final String ID = "#";
	private String bssid;

	/**
	 * Constructor of a Handshake frame.
	 * 
	 * @param bssid
	 *            The BSSID of the source device of the frame.
	 * 
	 */
	public HandshakeFrame(String bssid) {
		this.bssid = bssid;
	}

	/**
	 * This method is used to get the ID of this type of frame.
	 * 
	 * @return String the frame ID.
	 */
	@Override
	public String getID() {
		return HandshakeFrame.ID;
	}

	/**
	 * This method is used to process the frame. This corresponds to add to the
	 * database the device that sent the frame if it is not currently
	 * registered.
	 * 
	 * @param socket
	 *            The connection with the device that sent this fingerprint.
	 * @param dbManager
	 *            The database connector to the database.
	 * @throws SQLException
	 */
	@Override
	public void process(SocketConnection socket, DatabaseManager dbManager) throws SQLException {
		ResultSet rs;
		rs = dbManager.readDB("select idUserDevice from UserDevice where BSSID = \"" + bssid + "\"");
		if (!rs.next()) {
			dbManager.writeDB("insert into UserDevice values(null,\"" + bssid + "\")");
			System.out.println("Registrado [" + bssid + "] en bd");
			
			ArrayList<Fingerprint> list = new ArrayList<Fingerprint>();//lista vacia
			Main.listMap.put(bssid, list);
		} else
			System.out.println("[" + bssid + "] ya esta Registrado");
		System.out.println(
				"\nSending response 'Alive' to: " + socket.remoteIP() + " on port : " + ConnectionParams.CELL_PORT);
		socket.send(socket.remoteIP(), ConnectionParams.CELL_PORT, "Alive");
	}

	@Override
	public String toString() {
		return "HandshakeFrame [bssid=" + bssid + "]";
	}

}
