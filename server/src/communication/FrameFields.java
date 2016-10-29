package communication;

/**
 * This class groups the positions of the fields of the supported frames.
 *
 */
public class FrameFields {
	
	// Frame info position
	final public static int ID = 0; 
	final public static int BSSID = 1; 
	final public static int ROT_X = 2; 
	final public static int ROT_Y = 3; 
	final public static int ROT_Z = 4; 
	final public static int POS_X = 5; 
	final public static int POS_Y = 6;
	final public static int LENGTH = 7;
	
	final public static int AP_SSID_FINGERPRINT = 0;
	final public static int AP_BSSID_FINGERPRINT = 1;
	final public static int AP_RSSI_FINGERPRINT = 2;
	final public static int AP_CHAN_FINGERPRINT = 3;
	
	final public static int AP_FINGERPRINT_FRAME_LENGTH = 4;
	final public static int AP_SCAN_FRAME_LENGTH = 2;
	
	final public static int AP_BSSID_SCAN = 0;
	final public static int AP_RSSI_SCAN = 1;
	
	/*
	public static String BSSID = "bssid"; 
	public static String ROT_X = "rot_x"; 
	public static String ROT_Y = "rot_y"; 
	public static String ROT_Z = "rot_z"; 
	public static String POS_X = "pos_x"; 
	public static String POS_Y = "pos_y";
	public static String LENGTH = "length";
	public static String AP_SSID = "ap_ssid";
	public static String AP_BSSID = "ap_bssid";
	public static String AP_RSSI = "ap_rssi";
	public static String AP_CHAN = "ap_chan";
	*/
	
	
	
}
