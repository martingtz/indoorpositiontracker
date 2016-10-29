package communication;

/**
 * This class groups the connection parameters, such as: the local receiving port and 
 * the database name, address and username.
 *
 */
public class ConnectionParams {
	
	public static final int CELL_PORT = 8888;
	public static final int LOCAL_PORT = 8888; 
	public static final String SERVER_IP = "192.168.0.106"; 
	public static final int SERVER_PORT = 8080; 
	
	public static final String dbAddress = "localhost";//"192.168.0.107";
	public static final String dbName = "plamex_fingerprints";
	public static final String dbUser = "root";
	public static final String dbPass = "";
	
/*	public static final String SERVER_IP = "192.168.1.70"; 
	public static final int SERVER_PORT = 8080; 
	
	public static final String dbAddress = "192.168.1.70";
	public static final String dbName = "plamex";
	public static final String dbUser = "external";
	public static final String dbPass = "1234567";*/
	
}
