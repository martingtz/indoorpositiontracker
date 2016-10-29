package communication;

import java.sql.SQLException;

import storage.DatabaseManager;

/**
 * This interface contains the minimum requirements of a frame.
 *
 */
public interface Frame {
	
	
	/**
	 * This method returns the ID of the type of frame.
	 * 
	 * @return String the frame ID.
	 */
	public String getID();
	
	/**
	 * This method is used to process the frame, according to the type of frame.
	 * @param socket The connection with the device that sent the frame.
	 * @param dbManager The database connector to the database.
	 * @throws SQLException 
	 */
	public void process(SocketConnection socket, DatabaseManager dbManager) throws SQLException;
}
