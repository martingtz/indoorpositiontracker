package com.inte.SQLiteHandlerUpdated;

public class AccessPoint {

	private int idAccessPoint;
	private String ssid;
	private String bssid;
	private int channel;
	private int rssi;

	/**
	 * Constructor for an AP.
	 * 
	 * @param ap_ssid
	 *            The SSID of the AP.
	 * @param ap_bssid
	 *            The MAC address of the AP.
	 * @param ap_rssi
	 *            The perceived RSSI of the AP at the moment of creation.
	 * @param ap_chan
	 *            The AP channel.
	 */
	public AccessPoint(String ap_ssid, String ap_bssid, int ap_rssi, int ap_chan) {
		this.ssid = ap_ssid;
		this.bssid = ap_bssid;
		this.rssi = ap_rssi;
		this.channel = ap_chan;
	}

	public AccessPoint() {
		super();
	}

	public int getIdAccessPoint() {
		return idAccessPoint;
	}

	public void setIdAccessPoint(int idAccessPoint) {
		this.idAccessPoint = idAccessPoint;
	}

	/**
	 * This method is used to get the SSID of the AP.
	 * 
	 * @return String The SSID.
	 */
	public String getAp_ssid() {
		return ssid;
	}

	/**
	 * This method is used to set the SSID of the AP.
	 * 
	 * @param String
	 *            The SSID.
	 */
	public void setAp_ssid(String ap_ssid) {
		this.ssid = ap_ssid;
	}

	/**
	 * This method is used to get the BSSID of the AP.
	 * 
	 * @return String The BSSID.
	 */
	public String getAp_bssid() {
		return bssid;
	}

	/**
	 * This method is used to set the BSSID of the AP.
	 * 
	 * @param String
	 *            The BSSID.
	 */
	public void setAp_bssid(String ap_bssid) {
		this.bssid = ap_bssid;
	}

	/**
	 * This method is used to get the stored RSSI of the AP.
	 * 
	 * @return int The RSSI.
	 */
	public int getAp_rssi() {
		return rssi;
	}

	/**
	 * This method is used to get the RSSI value of the AP.
	 * 
	 * @param int
	 *            The RSSI.
	 */
	public void setAp_rssi(int ap_rssi) {
		this.rssi = ap_rssi;
	}

	/**
	 * This method is used to get the channel of the AP.
	 * 
	 * @return int The channel.
	 */
	public int getAp_chan() {
		return channel;
	}

	/**
	 * This method is used to set the channel of the AP.
	 * 
	 * @param int
	 *            The channel.
	 */
	public void setAp_chan(int ap_chan) {
		this.channel = ap_chan;
	}

	@Override
	public String toString() {
		return "AccessPoint [idAccessPoint=" + idAccessPoint + ", ssid=" + ssid + ", bssid=" + bssid + ", rssi=" + rssi
				+ ", channel=" + channel + "]";
	}

	public boolean equals(AccessPoint a) {

		if (a.getAp_bssid().equals(this.bssid)) {
			return true;
		} else {
			return false;
		}
	}

}
