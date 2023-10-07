package uk.co.harieo.ConvenienceLib.database.specific;

import net.harieo.ConvenienceLib.spigot.database.YamlSQLConfiguration;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * @deprecated Use {@link YamlSQLConfiguration}.
 */
@Deprecated
public class SQLConfiguration {

	private final String host;
	private final int port;
	private final String database;
	private final String username;
	private final String password;
	private final String append;

	/**
	 * Takes a {@link FileConfiguration} assuming it is formatted like the resource database.yml and parses all
	 * values relating to MySQL configuration
	 *
	 * @param configuration which holds the SQL database configuration values
	 */
	public SQLConfiguration(FileConfiguration configuration) {
		host = configuration.getString("mysql.host");
		port = configuration.getInt("mysql.port");
		database = configuration.getString("mysql.database");
		username = configuration.getString("mysql.username");
		password = configuration.getString("mysql.password");
		append = configuration.getString("mysql.append");
	}

	/**
	 * @return the host of the MySQL server
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @return the port of the MySQL server
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @return the address of the MySQL database consisting of host and port
	 */
	public String getAddress() {
		return getHost() + ":" + getPort();
	}

	/**
	 * @return the database to run queries on
	 */
	public String getDatabase() {
		return database;
	}

	/**
	 * @return the username of the user to use to execute queries
	 */
	public String getUsername() {
		return username;
	}

	/**
	 * @return the password of the user to use to execute queries
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @return a string to append to the end of any database connection url
	 */
	public String getAppend() {
		return append;
	}

}
