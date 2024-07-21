package uk.co.harieo.ConvenienceLib.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import uk.co.harieo.ConvenienceLib.database.specific.RedisConfiguration;
import uk.co.harieo.ConvenienceLib.database.specific.SQLConfiguration;

/**
 * @deprecated Use {@link net.harieo.ConvenienceLib.common.database.DatabaseManager}.
 */
@Deprecated
public class DatabaseManager {

	private final DatabaseConfiguration configuration;
	private final Properties sqlUserDetails;

	private final JedisPool publishPool;
	private final JedisPool subscribePool;

	/**
	 * A manager which handles opening SQL connections
	 *
	 * @param configuration which holds the connection data for the database
	 */
	public DatabaseManager(DatabaseConfiguration configuration) {
		if (configuration.isLoaded()) {
			this.configuration = configuration;
			this.sqlUserDetails = new Properties();
			sqlUserDetails.put("user", configuration.sql().getUsername());
			sqlUserDetails.put("password", configuration.sql().getPassword());

			publishPool = createJedisPool(configuration.redis());
			subscribePool = createJedisPool(configuration.redis());
		} else {
			throw new IllegalArgumentException("Attempt to load database with invalid configuration");
		}
	}

	/**
	 * Creates a new connection to the database. Note: This should be auto-closed or a severe connection leak could
	 * occur.
	 *
	 * @return the new connection
	 * @throws SQLException which may occur if there is an error connecting to the database
	 */
	public Connection getConnection() throws SQLException {
		SQLConfiguration sqlConfiguration = configuration.sql();
		return DriverManager.getConnection(
				"jdbc:mysql://" + sqlConfiguration.getAddress() + "/" + sqlConfiguration.getDatabase()
						+ sqlConfiguration
						.getAppend(), sqlUserDetails);
	}

	/**
	 * This pool should be used for publishing Redis messages to avoid conflict with subscribing
	 *
	 * @return a pool for pushing information to Redis
	 */
	public JedisPool getPublishPool() {
		return publishPool;
	}

	/**
	 * This pool should be used for all subscribing Redis listeners to avoid conflict with publishing
	 *
	 * @return a pool for retrieving information from Redis
	 */
	public JedisPool getSubscribePool() {
		return subscribePool;
	}

	/**
	 * Creates a {@link JedisPool} with the provided {@link RedisConfiguration} details
	 *
	 * @param configuration which holds the database configuration values
	 * @return the created pool
	 */
	private JedisPool createJedisPool(RedisConfiguration configuration) {
		return new JedisPool(new JedisPoolConfig(), configuration.getHost(), configuration.getPort(),
				configuration.getTimeout(), configuration.getPassword(), configuration.getDatabase());
	}

}
