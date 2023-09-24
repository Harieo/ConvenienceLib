package uk.co.harieo.ConvenienceLib.database;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import net.harieo.ConvenienceLib.spigot.config.SpigotConfigurationHandler;
import uk.co.harieo.ConvenienceLib.database.specific.RedisConfiguration;
import uk.co.harieo.ConvenienceLib.database.specific.SQLConfiguration;

/**
 * @deprecated Use {@link net.harieo.ConvenienceLib.spigot.database.SpigotDatabaseConfiguration}.
 */
@Deprecated
public class DatabaseConfiguration extends SpigotConfigurationHandler {

	private static final String FILE_NAME = "database.yml";

	private boolean loaded = false;
	private SQLConfiguration sqlConfiguration;
	private RedisConfiguration redisConfiguration;

	/**
	 * A handler which retrieves database configuration values
	 *
	 * @param plugin which is running this configuraton
	 */
	public DatabaseConfiguration(JavaPlugin plugin) {
		super(plugin, FILE_NAME);
		try {
			FileConfiguration configuration = getConfiguration();
			sqlConfiguration = new SQLConfiguration(configuration);
			redisConfiguration = new RedisConfiguration(configuration);
			loaded = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return whether the values have been successfully parsed
	 */
	public boolean isLoaded() {
		return loaded;
	}

	/**
	 * @return the {@link SQLConfiguration} which holds the SQL database values
	 */
	public SQLConfiguration sql() {
		return sqlConfiguration;
	}

	/**
	 * @return the {@link RedisConfiguration} which holds the Redis database values
	 */
	public RedisConfiguration redis() {
		return redisConfiguration;
	}

}
