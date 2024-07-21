package net.harieo.ConvenienceLib.bungee.config;

import net.harieo.ConvenienceLib.common.config.ConfigurationHandler;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class BungeeConfigurationHandler implements ConfigurationHandler<Configuration> {

    private final Plugin plugin;
    private final String fileName;

    /**
     * A handler which retrieves a configuration file from a BungeeCord plugin data folder.
     *
     * @param plugin the BungeeCord plugin handling the data
     * @param fileName the name of the configuration file within the data folder
     */
    public BungeeConfigurationHandler(@NotNull Plugin plugin, @NotNull String fileName) {
        this.plugin = plugin;
        this.fileName = fileName;
    }

    @Override
    public File getFolder() throws IOException {
        File dataFolder = plugin.getDataFolder();
        if (!dataFolder.exists() && !dataFolder.mkdir()) {
            throw new FileNotFoundException("Plugin data folder cannot be created");
        }
        return dataFolder;
    }

    @Override
    public File getFile() throws IOException {
        File file = new File(getFolder(), fileName);
        if (!file.exists()) {
            try (InputStream stream = plugin.getResourceAsStream(fileName)) {
                if (stream != null) {
                    Files.copy(stream, file.toPath());
                }
            }
        }
        return file;
    }

    @Override
    public Configuration getConfiguration(@NotNull File serializedFile) throws IOException {
        return ConfigurationProvider.getProvider(YamlConfiguration.class).load(serializedFile);
    }

}
