package net.harieo.ConvenienceLib.velocity.database;

import net.harieo.ConvenienceLib.common.database.api.RedisConfiguration;
import net.harieo.ConvenienceLib.velocity.config.MappedVelocityYamlConfigurationHandler;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

public class YamlRedisConfiguration extends MappedVelocityYamlConfigurationHandler implements RedisConfiguration {

    public static final String DEFAULT_FILE_NAME = "database.yml";

    private final String host;
    private final int port;
    private final int timeout;
    private final String password;
    private final int database;

    /**
     * Parses a file to retrieve Redis configuration details stored in YAML format.
     *
     * @param directoryPath the path of the directory containing the file
     * @param fileName      the canonical name of the file
     */
    public YamlRedisConfiguration(@NotNull Path directoryPath, @NotNull String fileName) {
        super(directoryPath, fileName);

        try {
            Map<String, Object> yamlRoot = getConfiguration();
            if (yamlRoot.containsKey("redis")) {
                Map<String, Object> redisRoot = (Map<String, Object>) yamlRoot.get("redis");
                host = (String) redisRoot.get("host");
                port = (Integer) redisRoot.get("port");
                timeout = (Integer) redisRoot.get("timeout");
                password = (String) redisRoot.get("password");
                database = (Integer) redisRoot.get("database");
            } else {
                throw new IOException(fileName + " does not contain redis data");
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to deserialize Redis configuration", e);
        }
    }

    /**
     * An overload of {@link #YamlRedisConfiguration(Path, String)} which uses {@link #DEFAULT_FILE_NAME} as the file name.
     *
     * @param directoryPath the path of the directory containing the file
     */
    public YamlRedisConfiguration(@NotNull Path directoryPath) {
        this(directoryPath, DEFAULT_FILE_NAME);
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
    public int getTimeout() {
        return timeout;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public int getDatabase() {
        return database;
    }

}
