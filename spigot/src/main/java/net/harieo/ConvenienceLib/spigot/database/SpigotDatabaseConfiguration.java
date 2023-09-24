package net.harieo.ConvenienceLib.spigot.database;

import net.harieo.ConvenienceLib.common.database.DatabaseConfiguration;
import net.harieo.ConvenienceLib.spigot.config.SpigotConfigurationHandler;
import net.harieo.ConvenienceLib.spigot.database.specific.YamlRedisConfiguration;
import net.harieo.ConvenienceLib.spigot.database.specific.YamlSQLConfiguration;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * A version of {@link DatabaseConfiguration} which can be constructed from a single {@link SpigotConfigurationHandler}.
 */
public class SpigotDatabaseConfiguration extends DatabaseConfiguration {

    /**
     * Creates a {@link YamlSQLConfiguration} and a {@link YamlRedisConfiguration} from a single {@link SpigotConfigurationHandler}
     * representing a single YAMl file containing all database configuration values.
     *
     * @param configurationHandler the configuration handler containing database values
     * @throws IOException if an error occurs parsing the configuration
     */
    public SpigotDatabaseConfiguration(@NotNull SpigotConfigurationHandler configurationHandler) throws IOException {
        super(
                new YamlSQLConfiguration(configurationHandler.getConfiguration()),
                new YamlRedisConfiguration(configurationHandler.getConfiguration())
        );
    }

}
