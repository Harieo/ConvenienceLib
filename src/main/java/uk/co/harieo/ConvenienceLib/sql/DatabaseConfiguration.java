package uk.co.harieo.ConvenienceLib.sql;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import uk.co.harieo.ConvenienceLib.config.ConfigurationHandler;

public class DatabaseConfiguration extends ConfigurationHandler {

	private static final String FILE_NAME = "database.yml";

	private boolean loaded = false;

	private String host;
	private int port;
	private String database;
	private String username;
	private String password;
	private String append;

	/**
	 * A handler which retrieves database configuration values
	 *
	 * @param plugin which is running this configuraton
	 */
	public DatabaseConfiguration(JavaPlugin plugin) {
		super(plugin, FILE_NAME);
		try {
			parseValues(getYamlConfiguration());
			loaded = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Parses values from the configuration, setting them as fields of this class
	 *
	 * @param configuration to parse values from
	 */
	private void parseValues(FileConfiguration configuration) {
		host = configuration.getString("mysql.host");
		port = configuration.getInt("mysql.port");
		database = configuration.getString("mysql.database");
		username = configuration.getString("mysql.username");
		password = configuration.getString("mysql.password");
		append = configuration.getString("mysql.append");
	}

	/**
	 * @return whether the values have been successfully parsed
	 */
	public boolean isLoaded() {
		return loaded;
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
