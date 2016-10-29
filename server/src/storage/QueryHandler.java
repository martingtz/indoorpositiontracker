package storage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import navigation.Fingerprint;

public class QueryHandler {
	private DatabaseManager mDatabaseManager;
	public final String Orientation_T = "Orientation";
	public final String Position_T = "Position";
	public final String ScanSession_T = "ScanSession";

	public QueryHandler(DatabaseManager databaseManager) {
		mDatabaseManager = databaseManager;
	}

	public ArrayList<Fingerprint> getAllFingerprints() {
		// TODO Auto-generated method stub
		// mDatabaseManager.readDB("")
		return null;
	}

	public int selectLast(String tableName) throws SQLException {
		ResultSet rs = mDatabaseManager
				.readDB("select id" + tableName + " from " + tableName + " order by id" + tableName + " desc limit 1");
		if (rs.next()) {
			return rs.getInt(1);
		}
		return -1;
	}

	public int insert(String tableName, String values) throws SQLException {
		mDatabaseManager.writeDB("insert into " + tableName + " values(null," + values + ")");
		return selectLast(tableName);

	}

	public ArrayList<Fingerprint> getFingerprintListFromBD(String bssid) throws SQLException {
		ArrayList<Fingerprint> list = new ArrayList<Fingerprint>();
		int idUserDevice = 0;
		ResultSet rs;
		rs = mDatabaseManager.readDB("select idUserDevice from UserDevice where BSSID = \"" + bssid + "\"");
		if (rs.next())
			idUserDevice = rs.getInt("idUserDevice");
		rs = mDatabaseManager.readDB(
				"SELECT idScanSession from scansession where userdevice_idUserDevice = \"" + idUserDevice + "\"");
		while (rs.next())
			list.add(getFingerprint(rs.getInt("idScanSession")));
		return list;
	}

	private Fingerprint getFingerprint(int scanId) throws SQLException {
		Fingerprint f = new Fingerprint();
		ResultSet rs1, rs2;

		// position and orientation
		rs1 = mDatabaseManager
				.readDB("SELECT orientation.x,orientation.y,orientation.z,position.posX,position.posY FROM scansession INNER JOIN orientation on orientation.idOrientation=scansession.Orientation_idOrientation INNER JOIN position on position.idPosition=scansession.Position_idPosition WHERE scansession.idScanSession = \""
						+ scanId + "\"");
		if (rs1.next()) {
			// crear fingerprint
			Float[] mOrientation = new Float[3];
			mOrientation[0] = rs1.getFloat("x");
			mOrientation[1] = rs1.getFloat("y");
			mOrientation[2] = rs1.getFloat("z");
			f.setOrientation(mOrientation);
			f.setLocation(rs1.getFloat("posX"), rs1.getFloat("posY"));

			// Lista de aps
			rs2 = mDatabaseManager
					.readDB("SELECT accesspoint.BSSID,fingerprint.RSSI from scansession INNER JOIN fingerprint on fingerprint.ScanSession_idScanSession=scansession.idScanSession INNER JOIN accesspoint on accesspoint.idAccessPoint=fingerprint.AccesPoint_idAccesPoint WHERE scansession.idScanSession = \""
							+ scanId + "\"");
			HashMap<String, Integer> measurements = new HashMap<String, Integer>();
			while (rs2.next())
				measurements.put(rs2.getString("BSSID"), rs2.getInt("RSSI"));
			f.setMeasurements(measurements);
			return f;
		}
		return null;
	}

}
