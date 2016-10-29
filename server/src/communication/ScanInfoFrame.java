package communication;

/**
 * This interface contains the minimum requirements of a frame which contains information
 * of scanned APs.
 *
 */
public interface ScanInfoFrame {
	public static final int MAP_ID = 1;
	
	/**
	 * This method is used to add an AP to an array of APs.
	 * 
	 * @param ap The AP to add.
	 */
	public void addAPInfo(AP ap);

}
