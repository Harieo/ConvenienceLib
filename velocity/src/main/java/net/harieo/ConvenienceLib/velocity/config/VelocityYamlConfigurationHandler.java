package net.harieo.ConvenienceLib.velocity.config;

import net.harieo.ConvenienceLib.common.config.ConfigurationHandler;
import org.jetbrains.annotations.NotNull;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * This class uses {@link Yaml} to deserialize a file into a specified type.
 *
 * @param <T> the type to deserialize to
 */
public class VelocityYamlConfigurationHandler<T> implements ConfigurationHandler<T> {

    private final Yaml yaml = new Yaml();
    private final File directory;
    private final String fileName;

    /**
     * This class uses {@link Yaml} to deserialize a file into a specified type.
     *
     * @param directoryPath the path of the directory containing the file
     * @param fileName the canonical name of the file
     */
    public VelocityYamlConfigurationHandler(@NotNull Path directoryPath, @NotNull String fileName) {
        this.directory = directoryPath.toFile();
        this.fileName = fileName;
    }


    @Override
    public File getFolder() throws IOException {
        if (!this.directory.exists() && !this.directory.mkdir()) {
            throw new FileNotFoundException("Plugin directory does not exist and cannot be created");
        } else if (!this.directory.isDirectory()) {
            throw new FileNotFoundException("Plugin directory is not a directory");
        } else {
            return directory;
        }
    }

    @Override
    public File getFile() throws IOException {
        File file = new File(this.directory, this.fileName);
        if (!file.exists()) {
            try (InputStream stream = this.getClass().getClassLoader().getResourceAsStream(fileName)) {
                if (stream != null) {
                    Files.copy(stream, file.toPath());
                }
            }
        }
        return file;
    }

    /**
     * Deserializes the serialized {@link File} into the specified type using {@link Yaml#load(Reader)}.
     *
     * @param serializedFile the serialized file to deserialize
     * @return the constructed object
     * @throws IOException if an error occurs loading the file
     */
    @Override
    public T getConfiguration(@NotNull File serializedFile) throws IOException {
        try (FileReader reader = new FileReader(serializedFile)) {
            return yaml.load(reader);
        }
    }

}
