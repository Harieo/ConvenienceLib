package uk.co.harieo.ConvenienceLib.config;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class ConfigurationHandler {

	private final JavaPlugin plugin;
	private final String fileName;

	/**
	 * A handler which retrieves data from a plugin-based configuration file
	 *
	 * @param plugin which the configuration file belongs to
	 * @param fileName of the file itself
	 */
	public ConfigurationHandler(JavaPlugin plugin, String fileName) {
		this.plugin = plugin;
		this.fileName = fileName;
	}

	/**
	 * Retrieves the data folder where files should be stored based on {@link JavaPlugin#getDataFolder()} and creates
	 * the folder if it doesn't already exist
	 *
	 * @return the data folder for the plugin
	 * @throws IOException if an error occurs attempting to create the folder
	 */
	public File getDataFolder() throws IOException {
		File dataFolder = plugin.getDataFolder();
		if (!dataFolder.exists() && !dataFolder.mkdir()) {
			throw new IOException("Failed to create data folder");
		}

		return dataFolder;
	}

	/**
	 * Retrieves the configuration file from the data folder, creating it from one of the plugin's resources if it
	 * does not exist. If there is no available resource, the file will NOT exist when returned.
	 *
	 * @return the configuration file
	 * @throws IOException if an error occurs creating the file
	 */
	public File getFile() throws IOException {
		File file = new File(getDataFolder(), fileName);
		if (!file.exists()) {
			try (InputStream stream = plugin.getResource(fileName)) {
				if (stream != null) {
					Files.copy(stream, file.toPath());
				}
			}
		}

		return file;
	}

	/**
	 * Calls {@link #getFile()} then parses it through {@link YamlConfiguration}
	 *
	 * @return the parsed yaml configuration
	 * @throws IOException if an error occurs loading the file
	 */
	public YamlConfiguration getYamlConfiguration() throws IOException {
		return YamlConfiguration.loadConfiguration(getFile());
	}

}
