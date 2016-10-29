package com.inte.communication;

/**
 * This class represents a Handshake frame, which is composed of:\n <b>'#' |
 * BSSID |</b>
 *
 */
public class HandshakeFrame {
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

	public String getBSSID() {
		return bssid;
	}

	public String getCSV() {
		String csv = HandshakeFrame.ID + "," + getBSSID();
		return csv;
	}

}
