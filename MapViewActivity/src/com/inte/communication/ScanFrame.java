package com.inte.communication;

import java.util.ArrayList;

import com.inte.SQLiteHandlerUpdated.AccessPoint;

public class ScanFrame implements Frame, ScanInfoFrame {
	public static final String ID = "s";

	String bssid;
	float rot_x;
	float rot_y;
	float rot_z;

	int length;
	private ArrayList<AccessPoint> apList;

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

		apList = new ArrayList<AccessPoint>();
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
		String csv = ScanFrame.ID + "," + bssid + "," + rot_x + "," + rot_y + "," + rot_z + "," + length;

		for (int i = 0; i < length; i++) {
			AccessPoint newAP = apList.get(i);
			csv += "," + newAP.getAp_bssid() + "," + newAP.getAp_rssi();
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
