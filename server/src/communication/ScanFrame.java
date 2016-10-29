package communication;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import app.Main;
import navigation.Fingerprint;
import storage.DatabaseManager;

/**
 * This class represents a Fingerprint frame, which is composed of:\n <b>'s' |
 * BSSID | Rot_X | Rot_Y | Rot_Z | | | Length | an array of APs with the length
 * specified. The array contains only the BSSID and RSSI of the APs | </b>
 *
 */
public class ScanFrame implements Frame, ScanInfoFrame {
	public static final String ID = "s";

	private String bssid;
	private float rot_x;
	private float rot_y;
	private float rot_z;

	private int length;
	private ArrayList<AP> apList;

	/**
	 * Constructor of a Scan frame.
	 * 
	 * @param bssid
	 *            The BSSID of the source device of the frame.
	 * @param rot_x
	 *            The rotation on the x axis of the source device of the frame.
	 * @param rot_y
	 *            The rotation on the y axis.
	 * @param rot_z
	 *            The rotation on the z axis.
	 * @param length
	 *            The number of APs that this frame contain.
	 * 
	 */
	public ScanFrame(String bssid, float rot_x, float rot_y, float rot_z, int length) {

		this.bssid = bssid;
		this.rot_x = rot_x;
		this.rot_y = rot_y;
		this.rot_z = rot_z;
		this.length = length;

		apList = new ArrayList<AP>();
	}

	/**
	 * This method is used to get the ID of this type of frame.
	 * 
	 * @return String the frame ID.
	 */
	@Override
	public String getID() {
		return ScanFrame.ID;
	}

	/**
	 * This method is used to add an AP to the array of APs.
	 * 
	 * @param ap
	 *            The AP to add.
	 */
	@Override
	public void addAPInfo(AP ap) {
		apList.add(ap);
	}

	/**
	 * This method is used to process the frame. This corresponds to try to find
	 * a match between the AP info of this frame and the AP info of the
	 * fingerprints stored in the database.
	 * 
	 * @param socket
	 *            The connection with the device that sent this scan frame.
	 * @param dbManager
	 *            The database connector to the database.
	 * @throws SQLException
	 */
	@Override
	public void process(SocketConnection socket, DatabaseManager dbManager) throws SQLException {

		if (Main.listMap.containsKey(bssid)) {

			System.out.println("Lista de fingerprints de " + bssid);
			System.out.println(Main.listMap.get(bssid).toString());

			HashMap<String, Integer> measurements = new HashMap<String, Integer>();
			for (int i = 0; i < apList.size(); i++)
				measurements.put(apList.get(i).getAp_bssid(), apList.get(i).getAp_rssi());

			Fingerprint f = new Fingerprint(measurements);
			Fingerprint closestMatch = f.getClosestMatch(Main.listMap.get(bssid));

			System.out.println("\nclosestmatch: " + closestMatch.toString());
			// se envia al cliente
			socket.send(socket.remoteIP(), ConnectionParams.CELL_PORT, closestMatch.getCSV());
		} else {
			System.out.println("No hay una lista asociada a: " + bssid);
		}

	}

	@Override
	public String toString() {
		return "ScanFrame [bssid=" + bssid + ", rot_x=" + rot_x + ", rot_y=" + rot_y + ", rot_z=" + rot_z + ", length="
				+ length + ", apList=" + apList + "]";
	}

}
