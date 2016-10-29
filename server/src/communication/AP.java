package communication;

/**
 * This class contains the basic properties of an AP.
 *
 */
public class AP {
	private String ap_ssid;
	private String ap_bssid;
	private int ap_rssi;
	private int ap_chan;

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
	public AP(String ap_ssid, String ap_bssid, int ap_rssi, int ap_chan) {
		this.ap_ssid = ap_ssid;
		this.ap_bssid = ap_bssid;
		this.ap_rssi = ap_rssi;
		this.ap_chan = ap_chan;
	}

	/**
	 * This method is used to get the SSID of the AP.
	 * 
	 * @return String The SSID.
	 */
	public String getAp_ssid() {
		return ap_ssid;
	}

	/**
	 * This method is used to set the SSID of the AP.
	 * 
	 * @param String
	 *            The SSID.
	 */
	public void setAp_ssid(String ap_ssid) {
		this.ap_ssid = ap_ssid;
	}

	/**
	 * This method is used to get the BSSID of the AP.
	 * 
	 * @return String The BSSID.
	 */
	public String getAp_bssid() {
		return ap_bssid;
	}

	/**
	 * This method is used to set the BSSID of the AP.
	 * 
	 * @param String
	 *            The BSSID.
	 */
	public void setAp_bssid(String ap_bssid) {
		this.ap_bssid = ap_bssid;
	}

	/**
	 * This method is used to get the stored RSSI of the AP.
	 * 
	 * @return int The RSSI.
	 */
	public int getAp_rssi() {
		return ap_rssi;
	}

	/**
	 * This method is used to get the RSSI value of the AP.
	 * 
	 * @param int
	 *            The RSSI.
	 */
	public void setAp_rssi(int ap_rssi) {
		this.ap_rssi = ap_rssi;
	}

	/**
	 * This method is used to get the channel of the AP.
	 * 
	 * @return int The channel.
	 */
	public int getAp_chan() {
		return ap_chan;
	}

	/**
	 * This method is used to set the channel of the AP.
	 * 
	 * @param int
	 *            The channel.
	 */
	public void setAp_chan(int ap_chan) {
		this.ap_chan = ap_chan;
	}

	@Override
	public String toString() {
		return "\nAP [ap_ssid=" + ap_ssid + ", ap_bssid=" + ap_bssid + ", ap_rssi=" + ap_rssi + ", ap_chan=" + ap_chan
				+ "]";
	}

}
