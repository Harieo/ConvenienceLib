package uk.co.harieo.ConvenienceLib.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseManager {

	private final DatabaseConfiguration configuration;
	private final Properties userDetails;

	/**
	 * A manager which handles opening SQL connections
	 *
	 * @param configuration which holds the connection data for the database
	 */
	public DatabaseManager(DatabaseConfiguration configuration) {
		if (configuration.isLoaded()) {
			this.configuration = configuration;
			this.userDetails = new Properties();
			userDetails.put("user", configuration.getUsername());
			userDetails.put("password", configuration.getPassword());
		} else {
			throw new IllegalArgumentException("Attempt to load database with invalid configuration");
		}
	}

	/**
	 * Creates a new connection to the database. Note: This should be auto-closed or a severe connection
	 * leak could occur.
	 *
	 * @return the new connection
	 * @throws SQLException which may occur if there is an error connecting to the database
	 */
	public Connection getConnection() throws SQLException {
		return DriverManager.getConnection(
				"jdbc:mysql://" + configuration.getAddress() + "/" + configuration.getDatabase() + configuration
						.getAppend(), userDetails);
	}

}
