package uk.co.harieo.ConvenienceLib.config;

import net.harieo.ConvenienceLib.spigot.config.SpigotConfigurationHandler;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

/**
 * @deprecated This class exists only to prevent catastrophic errors for users of the pre-1.2.0 update. Instead, use
 * {@link SpigotConfigurationHandler} to conform to the updated naming convention.
 */
@Deprecated
public class ConfigurationHandler extends SpigotConfigurationHandler {

	/**
	 * A handler which retrieves data from a plugin-based configuration file
	 *
	 * @param plugin which the configuration file belongs to
	 * @param fileName of the file itself
	 */
	public ConfigurationHandler(JavaPlugin plugin, String fileName) {
		super(plugin, fileName);
	}

	/**
	 * Retrieves the data folder where files should be stored based on {@link JavaPlugin#getDataFolder()} and creates
	 * the folder if it doesn't already exist
	 *
	 * @return the data folder for the plugin
	 * @throws IOException if an error occurs attempting to create the folder
	 * @deprecated Use {@link #getFile()} to match with super interface.
	 */
	@Deprecated
	public File getDataFolder() throws IOException {
		return getFolder();
	}

	/**
	 * Calls {@link #getFile()} then parses it through {@link YamlConfiguration}
	 *
	 * @return the parsed yaml configuration
	 * @throws IOException if an error occurs loading the file
	 * @deprecated Use {@link #getConfiguration()} to match with super interface.
	 */
	@Deprecated
	public YamlConfiguration getYamlConfiguration() throws IOException {
		return getConfiguration();
	}

}
