package net.harieo.ConvenienceLib.common.database;

import lombok.Getter;
import net.harieo.ConvenienceLib.common.database.api.RedisConfiguration;
import net.harieo.ConvenienceLib.common.database.api.SQLConfiguration;
import net.harieo.ConvenienceLib.common.redis.RedisClient;
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

	@Getter
	private final RedisClient redisClient;

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
		this.redisClient = new RedisClient(this);
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
	 * Overloads {@link #getConnection(SQLConfiguration)} but uses the {@link SQLConfiguration}already stored in this
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
	 * Creates a {@link JedisPool} with the provided {@link RedisConfiguration} details.
	 *
	 * @return the created pool
	 */
	public JedisPool createJedisPool() {
		RedisConfiguration configuration = this.configuration.getRedisConfiguration();
		return new JedisPool(new JedisPoolConfig(), configuration.getHost(), configuration.getPort(),
				configuration.getTimeout(), configuration.getPassword(), configuration.getDatabase());
	}

}
