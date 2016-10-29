package communication;

/**
 * This class parses a frame currently represented as a String object.
 *
 */
public class Parser {


	/**
	 * This method is used to parse a frame currently represented as a String object.
	 * 
	 * @param data The frame as a String
	 * @return Frame Returns the corresponding frame of the received data. The types are: Handshake, Fingerprint and Scan frame.
	 */
	public Frame getFrame(String data) {
		Frame frame = null;
		String [] array = data.split(",");
		//System.out.println("Received ["+array[FrameFields.LENGTH]+"]");
		switch(array[FrameFields.ID]) {
		
		case FingerprintFrame.ID:
			//System.out.println("LLego un FingerprintFrame");
			FingerprintFrame fingerprintFrame = new FingerprintFrame(array[FrameFields.BSSID], Float.parseFloat(array[FrameFields.ROT_X]), Float.parseFloat(array[FrameFields.ROT_Y]), 
					Float.parseFloat(array[FrameFields.ROT_Z]), Float.parseFloat(array[FrameFields.POS_X]), Float.parseFloat(array[FrameFields.POS_Y]), 
					Integer.parseInt(array[FrameFields.LENGTH]));
			setAPInfo(fingerprintFrame, array, FrameFields.LENGTH+1, Integer.parseInt(array[FrameFields.LENGTH]));
			frame = fingerprintFrame;
			break;
			
			
		case ScanFrame.ID:
			//System.out.println("LLego un ScanFrame");
			ScanFrame scanFrame = new ScanFrame(array[FrameFields.BSSID], Float.parseFloat(array[FrameFields.ROT_X]), Float.parseFloat(array[FrameFields.ROT_Y]), 
					Float.parseFloat(array[FrameFields.ROT_Z]), Integer.parseInt(array[FrameFields.LENGTH]));
			
			setAPInfo(scanFrame, array, FrameFields.LENGTH-1, Integer.parseInt(array[FrameFields.LENGTH-2]));
			frame = scanFrame;
			break;
			
		case HandshakeFrame.ID:
			frame = new HandshakeFrame(array[FrameFields.BSSID]);
			break;
		default: break;
		}

		return frame;
	}
	
	/**
	 * This internal method is used to parse the list of APs received in a String array and adding them to the array of AP of the frame. 
	 * 
	 * @param frame The frame where to store the list of AP
	 * @param buffer The String array that contains the APs information
	 * @param offset The offset of the String array where the APs info is
	 * @param length The number of APs stored on the String array
	 */
	private void setAPInfo(ScanInfoFrame frame, String [] buffer, int offset, int length) {
		int i = 0;
		AP ap = null;
		boolean allData = false;
		int ssid_index  = 0;
		int bssid_index = 0;
		int rssi_index  = 0;
		int chan_index  = 0;
		
		
		if(frame instanceof FingerprintFrame) {
			allData = true;
			ssid_index  = FrameFields.AP_SSID_FINGERPRINT;
			bssid_index = FrameFields.AP_BSSID_FINGERPRINT;
			rssi_index  = FrameFields.AP_RSSI_FINGERPRINT;
			chan_index  = FrameFields.AP_CHAN_FINGERPRINT;
		}
		else {
			bssid_index = FrameFields.AP_BSSID_SCAN;
			rssi_index  = FrameFields.AP_RSSI_SCAN;
		}
		
		
		while(i < length) {
			if(allData) {
				//System.out.println("Received ["+buffer[rssi_index+offset]+"],["+buffer[chan_index+offset]+"]");
				ap = new AP(buffer[ssid_index+offset], buffer[bssid_index+offset], 
						Integer.parseInt(buffer[rssi_index+offset]), Integer.parseInt(buffer[chan_index+offset]));
				offset += FrameFields.AP_FINGERPRINT_FRAME_LENGTH;
			}
			else {
				//System.out.println("Received ["+buffer[bssid_index+offset]+"],["+buffer[rssi_index+offset]+"]");
				ap = new AP("", buffer[bssid_index+offset], 
						Integer.parseInt(buffer[rssi_index+offset]), -1);
				offset += FrameFields.AP_SCAN_FRAME_LENGTH;
			}
			
			frame.addAPInfo(ap);
			
			i++;
		}
	}
}








