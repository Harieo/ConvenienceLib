package net.harieo.ConvenienceLib.spigot.database.specific;

import net.harieo.ConvenienceLib.common.database.specific.RedisConfiguration;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * A {@link RedisConfiguration} which can parse values from a YAML configuration.
 */
public class YamlRedisConfiguration extends RedisConfiguration {

	/**
	 * Takes a {@link FileConfiguration} and parses it for Redis configuration values with the prefix 'redis.'
	 * (e.g., 'redis.host').
	 *
	 * @param configuration the YAML configuration to parse values from
	 */
	public YamlRedisConfiguration(FileConfiguration configuration) {
		super(configuration.getString("redis.host"),
				configuration.getInt("redis.port"),
				configuration.getInt("redis.timeout"),
				configuration.getString("redis.password"),
				configuration.getInt("redis.database"));
	}

}
