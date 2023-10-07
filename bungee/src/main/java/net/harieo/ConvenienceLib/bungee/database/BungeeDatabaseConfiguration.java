package net.harieo.ConvenienceLib.bungee.database;

import net.harieo.ConvenienceLib.bungee.config.BungeeConfigurationHandler;
import net.harieo.ConvenienceLib.bungee.database.specific.YamlRedisConfiguration;
import net.harieo.ConvenienceLib.bungee.database.specific.YamlSQLConfiguration;
import net.harieo.ConvenienceLib.common.database.DatabaseConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * A version of {@link DatabaseConfiguration} which can be constructed from a single {@link BungeeDatabaseConfiguration}.
 */
public class BungeeDatabaseConfiguration extends DatabaseConfiguration {

    /**
     * Creates a {@link YamlSQLConfiguration} and a {@link YamlRedisConfiguration} from a single {@link BungeeConfigurationHandler}
     * representing a single YAMl file containing all database configuration values.
     *
     * @param configurationHandler the configuration handler containing database values
     * @throws IOException if an error occurs parsing the configuration
     */
    public BungeeDatabaseConfiguration(@NotNull BungeeConfigurationHandler configurationHandler) throws IOException {
        super(
                new YamlSQLConfiguration(configurationHandler.getConfiguration()),
                new YamlRedisConfiguration(configurationHandler.getConfiguration())
        );
    }

}
