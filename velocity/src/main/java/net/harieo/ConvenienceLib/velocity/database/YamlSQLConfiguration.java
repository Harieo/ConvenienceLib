package net.harieo.ConvenienceLib.velocity.database;

import net.harieo.ConvenienceLib.common.database.api.SQLConfiguration;
import net.harieo.ConvenienceLib.velocity.config.MappedVelocityYamlConfigurationHandler;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

public class YamlSQLConfiguration extends MappedVelocityYamlConfigurationHandler implements SQLConfiguration {

    private final String host;
    private final int port;
    private final String database;
    private final String username;
    private final String password;
    private final String append;

    /**
     * Parses a file to retrieve SQL connection data.
     *
     * @param directoryPath the path of the directory containing the file
     * @param fileName      the canonical name of the file
     */
    public YamlSQLConfiguration(@NotNull Path directoryPath, @NotNull String fileName) {
        super(directoryPath, fileName);

        try {
            Map<String, Object> yamlRoot = getConfiguration();
            if (yamlRoot.containsKey("mysql")) {
                Map<String, Object> redisRoot = (Map<String, Object>) yamlRoot.get("mysql");
                host = (String) redisRoot.get("host");
                port = (Integer) redisRoot.get("port");
                username = (String) redisRoot.get("username");
                password = (String) redisRoot.get("password");
                database = (String) redisRoot.get("database");
                append = (String) redisRoot.get("append");
            } else {
                throw new IOException(fileName + " does not contain SQL data");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize SQL configuration", e);
        }
    }

    /**
     * An overload of {@link #YamlSQLConfiguration(Path, String)} which uses {@link YamlRedisConfiguration#DEFAULT_FILE_NAME}
     * as the file name.
     *
     * @param directoryPath the path of the directory containing the file
     */
    public YamlSQLConfiguration(@NotNull Path directoryPath) {
        this(directoryPath, YamlRedisConfiguration.DEFAULT_FILE_NAME);
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public String getDatabase() {
        return database;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getAppend() {
        return append;
    }

}
