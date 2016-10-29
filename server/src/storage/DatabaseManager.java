package storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This class manages a connection with a database.
 *
 */
public class DatabaseManager {
	private Connection connection;
	private String dbAddress;
	private String dbName;
	private String dbUser;
	private String dbPass;

	/**
	 * Constructor for the DatabaseManager.
	 * 
	 * @param dbAddress
	 *            The IP address of the database.
	 * @param dbName
	 *            The database name.
	 * @param dbUser
	 *            The database user.
	 * @param dbPass
	 *            The database password.
	 * @throws SQLException
	 *             When the connection to the database fail.
	 */
	public DatabaseManager(String dbAddress, String dbName, String dbUser, String dbPass) throws SQLException {
		this.dbAddress = dbAddress;
		this.dbName = dbName;
		this.dbUser = dbUser;
		this.dbPass = dbPass;

		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		}

		connection = DriverManager.getConnection("jdbc:mysql://" + this.dbAddress + "/" + this.dbName + "?" + "user="
				+ this.dbUser + "&password=" + this.dbPass);
	}

	/**
	 * This method is used to perform a query to the database that does not
	 * return a result other than success or fail.
	 * 
	 * @param query
	 *            The query to perform.
	 * @return boolean true if success, false otherwise.
	 */
	public int writeDB(String query) {
		try {
			PreparedStatement preparedSt = connection.prepareStatement(query);
			return preparedSt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return -1;
		}
	}

	/**
	 * This method is used to perform a query to the database when a returning
	 * result is expected.
	 * 
	 * @param query
	 *            The query to perform.
	 * @return ResultSet The method returns the result that the database send as
	 *         a ResultSet object.
	 */
	public ResultSet readDB(String query) {
		Statement st;
		ResultSet rs = null;
		try {
			st = connection.createStatement();
			rs = st.executeQuery(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}

	/**
	 * This method is used to close the connection with the database
	 */
	public void close() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
