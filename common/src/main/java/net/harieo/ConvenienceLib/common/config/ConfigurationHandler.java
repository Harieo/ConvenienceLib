package net.harieo.ConvenienceLib.common.config;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

/**
 * Represents a handler which converts data into serialized Java IO {@link File} and parses it back into
 * a local format {@code <T>}.
 *
 * @param <T> the format of the local deserialized configuration object
 */
public interface ConfigurationHandler<T> {

    /**
     * Retrieves the directory where the configuration files are stored.
     *
     * @throws IOException if an error occurs retrieving the file
     */
    File getFolder() throws IOException;

    /**
     * Retrieves the configuration {@link File} for this handler to use to create the deserialized configuration object.
     *
     * @return the configuration file
     * @throws IOException if an error occurs retrieving the file
     */
    File getFile() throws IOException;

    /**
     * Parses a {@link File} to convert the serialized configuration into a local deserialized object.
     *
     * @return the deserialized configuration in a local format
     * @throws IOException if an error occurs parsing the file
     */
    T getConfiguration(@NotNull File serializedFile) throws IOException;

    /**
     * Uses {@link #getFile()} to retrieve the serialized {@link File} then calls {@link #getConfiguration(File)} with
     * that {@link File}.
     *
     * @return the deserialized configuration in a local format
     * @throws IOException if an error occurs retrieving or parsing the file
     */
    default T getConfiguration() throws IOException {
        return getConfiguration(getFile());
    }

}
