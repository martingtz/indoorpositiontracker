package com.inte.communication;

import java.util.ArrayList;

import com.inte.SQLiteHandlerUpdated.AccessPoint;

public class FingerprintFrame implements Frame, ScanInfoFrame {
	public static final String ID = "f";

	String bssid;
	float rot_x;
	float rot_y;
	float rot_z;
	float pos_x;
	float pos_y;
	int length;
	ArrayList<AccessPoint> apList;

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

		apList = new ArrayList<AccessPoint>();
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
	public void addAPInfo(AccessPoint ap) {
		apList.add(ap);
	}

	/**
	 * This method is used to get the CSV formated String of the type of frame.
	 * 
	 * @return String the CSV formated string.
	 */
	@Override
	public String getCSV() {
		String csv = FingerprintFrame.ID + "," + bssid + "," + rot_x + "," + rot_y + "," + rot_z + "," + pos_x + ","
				+ pos_y + "," + length;

		for (int i = 0; i < length; i++) {
			AccessPoint newAP = apList.get(i);
			csv += "," + newAP.getAp_ssid() + "," + newAP.getAp_bssid() + "," + newAP.getAp_rssi() + ","
					+ newAP.getAp_chan();
		}
		return csv;
	}

	public ArrayList<AccessPoint> getApList() {
		return apList;
	}

	public void setApList(ArrayList<AccessPoint> apList) {
		this.apList = apList;
	}

}
