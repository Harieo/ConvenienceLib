package net.harieo.ConvenienceLib.spigot.database;

import net.harieo.ConvenienceLib.common.database.api.RedisConfiguration;
import net.harieo.ConvenienceLib.spigot.config.SpigotConfigurationHandler;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * A {@link RedisConfiguration} which can parse values from a YAML configuration.
 */
public class YamlRedisConfiguration
		extends SpigotConfigurationHandler
		implements RedisConfiguration {

	public static final String DEFAULT_FILE_NAME = "database.yml";

	private final String host;
	private final int port;
	private final int timeout;
	private final String password;
	private final int database;

	/**
	 * Takes a {@link FileConfiguration} and parses it for Redis configuration values with the prefix 'redis.'
	 * (e.g., 'redis.host').
	 *
	 * @param plugin the plugin to retrieve the database configuration from
	 * @param fileName the name of the database configuration file
	 */
	public YamlRedisConfiguration(@NotNull Plugin plugin, @NotNull String fileName) {
		super(plugin, fileName);
		try {
			YamlConfiguration configuration = getConfiguration();
			this.host = configuration.getString("redis.host");
			this.port = configuration.getInt("redis.port");
			this.timeout = configuration.getInt("redis.timeout");
			this.password = configuration.getString("redis.password");
			this.database = configuration.getInt("redis.database");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * An overload of {@link YamlRedisConfiguration} which uses the {@link #DEFAULT_FILE_NAME} as the
	 * file name.
	 *
	 * @param plugin the plugin containing the data file
	 */
	public YamlRedisConfiguration(@NotNull Plugin plugin) {
		this(plugin, DEFAULT_FILE_NAME);
	}

	@Override
	public String getHost() {
		return host;
	}

	@Override
	public int getPort() {
		return port;
	}

	@Override
	public int getTimeout() {
		return timeout;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public int getDatabase() {
		return database;
	}

}
