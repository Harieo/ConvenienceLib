package net.harieo.ConvenienceLib.bungee.database.specific;

import net.harieo.ConvenienceLib.common.database.specific.RedisConfiguration;
import net.md_5.bungee.config.Configuration;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link RedisConfiguration} which can parse values from a YAML configuration.
 */
public class YamlRedisConfiguration extends RedisConfiguration {

	/**
	 * Takes a Bungee {@link net.md_5.bungee.config.Configuration} and parses it for Redis configuration values with the prefix 'redis.'
	 * (e.g., 'redis.host').
	 *
	 * @param configuration the YAML configuration to parse values from
	 */
	public YamlRedisConfiguration(@NotNull Configuration configuration) {
		super(configuration.getString("redis.host"),
				configuration.getInt("redis.port"),
				configuration.getInt("redis.timeout"),
				configuration.getString("redis.password"),
				configuration.getInt("redis.database"));
	}

}
