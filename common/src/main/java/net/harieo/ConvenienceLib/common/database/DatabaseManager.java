package net.harieo.ConvenienceLib.common.database;

import lombok.Getter;
import net.harieo.ConvenienceLib.common.database.specific.RedisConfiguration;
import net.harieo.ConvenienceLib.common.database.specific.SQLConfiguration;
import org.jetbrains.annotations.NotNull;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseManager {

	private final DatabaseConfiguration configuration;
	private final Properties sqlUserDetails;

	/**
	 * -- GETTER --
	 *  This pool should be used for publishing Redis messages to avoid conflict with subscribing
	 *
	 * @return a pool for pushing information to Redis
	 */
	@Getter
	private final JedisPool publishPool;
	/**
	 * -- GETTER --
	 *  This pool should be used for all subscribing Redis listeners to avoid conflict with publishing
	 *
	 * @return a pool for retrieving information from Redis
	 */
	@Getter
	private final JedisPool subscribePool;

	/**
	 * A manager which handles opening SQL connections
	 *
	 * @param configuration which holds the connection data for the database
	 */
	public DatabaseManager(@NotNull DatabaseConfiguration configuration) {
		this.configuration = configuration;
		this.sqlUserDetails = new Properties();

		SQLConfiguration sqlConfiguration = configuration.getSqlConfiguration();
		sqlUserDetails.put("user", sqlConfiguration.getUsername());
		sqlUserDetails.put("password", sqlConfiguration.getPassword());

		RedisConfiguration redisConfiguration = configuration.getRedisConfiguration();
		publishPool = createJedisPool(redisConfiguration);
		subscribePool = createJedisPool(redisConfiguration);
	}

	/**
	 * Creates a new connection to the MySQL database.
	 *
	 * @param sqlConfiguration the configuration data to connect to the database
	 * @return the new MySQL connection
	 * @throws SQLException if there is an error connecting to the database
	 * @apiNote <b>Always</b> auto-close your connections and your statements.
	 */
	public Connection getConnection(@NotNull SQLConfiguration sqlConfiguration) throws SQLException {
		return DriverManager.getConnection(
				"jdbc:mysql://" + sqlConfiguration.getAddress() + "/" + sqlConfiguration.getDatabase()
						+ sqlConfiguration
						.getAppend(),
				sqlUserDetails);
	}

	/**
	 * Overloads {@link #getConnection(SQLConfiguration)} but uses the {@link SQLConfiguration} already stored in this
	 * object.
	 *
	 * @return the new MySQL connection
	 * @throws SQLException if there is an error connecting to the database
	 * @apiNote <b>Always</b> auto-close your connections and your statements.
	 */
	public Connection getConnection() throws SQLException {
		SQLConfiguration sqlConfiguration = configuration.getSqlConfiguration();
		return getConnection(sqlConfiguration);
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
