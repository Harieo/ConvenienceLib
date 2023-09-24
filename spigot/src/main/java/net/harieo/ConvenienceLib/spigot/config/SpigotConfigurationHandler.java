package net.harieo.ConvenienceLib.spigot.config;

import net.harieo.ConvenienceLib.common.config.ConfigurationHandler;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class SpigotConfigurationHandler implements ConfigurationHandler<YamlConfiguration> {

    private final Plugin plugin;
    private final String fileName;

    /**
     * A handler which retrieves data from a plugin-based configuration file
     *
     * @param plugin the plugin which the configuration file belongs to
     * @param fileName the name of the file
     */
    public SpigotConfigurationHandler(@NotNull Plugin plugin, @NotNull String fileName) {
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
    @Override
    public File getFolder() throws IOException {
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
    @Override
    public File getFile() throws IOException {
        File file = new File(getFolder(), fileName);
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
     * Converts serialized {@link File} into the local {@link YamlConfiguration}.
     *
     * @return the parsed yaml configuration
     */
    @Override
    public YamlConfiguration getConfiguration(@NotNull File serializedFile) {
        return YamlConfiguration.loadConfiguration(serializedFile);
    }

}
