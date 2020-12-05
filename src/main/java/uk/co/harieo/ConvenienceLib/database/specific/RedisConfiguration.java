package uk.co.harieo.ConvenienceLib.database.specific;

import org.bukkit.configuration.file.FileConfiguration;

public class RedisConfiguration {

	private final String host;
	private final int port;
	private final int timeout;
	private final String password;
	private final int database;

	/**
	 * Takes a {@link FileConfiguration} and parses it for Redis configuration values, assuming it is formatted like
	 * the resource database.yml
	 *
	 * @param configuration to parse values from
	 */
	public RedisConfiguration(FileConfiguration configuration) {
		host = configuration.getString("redis.host");
		port = configuration.getInt("redis.port");
		timeout = configuration.getInt("redis.timeout");
		password = configuration.getString("redis.password");
		database = configuration.getInt("redis.database");
	}

	/**
	 * @return the hostname of the Redis database
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @return the port for the Redis database
	 */
	public int getPort() {
		return port;
	}

	/**
	 * @return the millisecond timeout for connection
	 */
	public int getTimeout() {
		return timeout;
	}

	/**
	 * @return the database password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @return the database identifier
	 */
	public int getDatabase() {
		return database;
	}

}
