package communication;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import app.Main;
import navigation.Fingerprint;
import storage.DatabaseManager;
import storage.QueryHandler;

/**
 * This class represents a Fingerprint frame, which is composed of: \n <b>'f' |
 * BSSID | Rot_X | Rot_Y | Rot_Z | Pos_X | Pos_Y | Length | an array of APs with
 * the length specified |</b>
 *
 */
public class FingerprintFrame implements Frame, ScanInfoFrame {
	public static final String ID = "f";

	private String bssid;
	private float rot_x;
	private float rot_y;
	private float rot_z;
	private float pos_x;
	private float pos_y;
	private int length;
	private ArrayList<AP> apList;

	/**
	 * Constructor of a Fingerprint frame.
	 * 
	 * @param bssid
	 *            The BSSID of the source device of the frame.
	 * @param rot_x
	 *            The rotation on the x axis of the source device of the frame.
	 * @param rot_y
	 *            The rotation on the y axis.
	 * @param rot_z
	 *            The rotation on the z axis.
	 * @param pos_x
	 *            The virtual position on the x axis of the device on a map.
	 * @param pos_y
	 *            The virtual position on the y axis.
	 * @param length
	 *            The number of APs that this frame contain.
	 * 
	 */
	public FingerprintFrame(String bssid, float rot_x, float rot_y, float rot_z, float pos_x, float pos_y, int length) {

		this.bssid = bssid;
		this.rot_x = rot_x;
		this.rot_y = rot_y;
		this.rot_z = rot_z;
		this.pos_x = pos_x;
		this.pos_y = pos_y;
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
		return FingerprintFrame.ID;
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
	 * This method is used to process the frame. This corresponds to add this
	 * fingerprint to the database.
	 * 
	 * @param socket
	 *            The connection with the device that sent this fingerprint.
	 * @param dbManager
	 *            The database connector to the database.
	 * @throws SQLException
	 */
	@Override
	public void process(SocketConnection socket, DatabaseManager dbManager) throws SQLException {

		// verficamos si existe una lista de fingerprints asociada al bssid del
		// dsipositivo
		if (Main.listMap.containsKey(bssid)) {
			System.out.println("Agregando fingerprint a la lista de fingerprints existente");
			HashMap<String, Integer> measurements = new HashMap<String, Integer>();
			Fingerprint f = new Fingerprint();
			Float[] mOrientation = new Float[3];

			mOrientation[0] = rot_x;
			mOrientation[1] = rot_y;
			mOrientation[2] = rot_z;

			for (int i = 0; i < apList.size(); i++)
				measurements.put(apList.get(i).getAp_bssid(), apList.get(i).getAp_rssi());

			f.setLocation(pos_x, pos_y);
			f.setOrientation(mOrientation);
			f.setMeasurements(measurements);

			// Se agrega el finger recien creado a la lista de fingerprint
			// relacionada al bssid actual
			Main.listMap.get(bssid).add(f);

			System.out.println("Lista de fingerprint actualizada");
			System.out.println("Res:" + Main.listMap.get(bssid).toString());

		}
		// Aqui se agrega la bd
		System.out.println("Agregando fingerprint frame a la BD");
		ResultSet rs;
		QueryHandler query = new QueryHandler(dbManager);
		int idOrientation, idUserDevice, idScanSession, idPosition, idAP;
		rs = dbManager.readDB("select idUserDevice from UserDevice where BSSID = \"" + bssid + "\"");
		if (rs.next()) {
			idUserDevice = rs.getInt("idUserDevice");
			idOrientation = query.insert(query.Orientation_T, rot_x + "," + rot_y + "," + rot_z);
			idPosition = query.insert(query.Position_T, MAP_ID + "," + pos_x + "," + pos_y);
			idScanSession = query.insert(query.ScanSession_T,
					idOrientation + "," + idPosition + "," + idUserDevice + ",now()");
			// Search if the APs are stored in the DB
			for (int i = 0; i < apList.size(); i++) {
				AP ap = apList.get(i);
				rs = dbManager.readDB(
						"select idAccessPoint,BSSID from AccessPoint where BSSID = \"" + ap.getAp_bssid() + "\"");
				if (rs.next())
					idAP = rs.getInt("idAccessPoint");
				else
					idAP = query.insert("AccessPoint",
							"'" + ap.getAp_ssid() + "'" + "," + "'" + ap.getAp_bssid() + "'" + "," + ap.getAp_chan());
				query.insert("Fingerprint", idScanSession + "," + idAP + "," + ap.getAp_rssi());
			}
		}

	}

	@Override
	public String toString() {
		return "FingerprintFrame [bssid=" + bssid + ", rot_x=" + rot_x + ", rot_y=" + rot_y + ", rot_z=" + rot_z
				+ ", pos_x=" + pos_x + ", pos_y=" + pos_y + ", length=" + length + ", apList=" + apList + "]";
	}

}
