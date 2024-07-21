package net.harieo.ConvenienceLib.velocity.config;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.Map;

/**
 * Deserializes YAML files into the basic {@link Map} format.
 */
public class MappedVelocityYamlConfigurationHandler extends VelocityYamlConfigurationHandler<Map<String, Object>> {

    /**
     * Deserializes YAML files into the basic {@link Map} format.
     *
     * @param directoryPath the path of the directory containing the file
     * @param fileName      the canonical name of the file
     */
    public MappedVelocityYamlConfigurationHandler(@NotNull Path directoryPath, @NotNull String fileName) {
        super(directoryPath, fileName);
    }

}