package net.harieo.ConvenienceLib.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import lombok.Getter;
import net.harieo.ConvenienceLib.common.database.DatabaseConfiguration;
import net.harieo.ConvenienceLib.velocity.database.YamlRedisConfiguration;
import net.harieo.ConvenienceLib.velocity.database.YamlSQLConfiguration;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.logging.Logger;

@Plugin(id = "conveniencelib-velocity", name = "ConvenienceLib", version = "1.2.1",
        url = "https://github.com/Harieo/ConvenienceLib", authors = {"Harieo"})
public class ConvenienceLibVelocity {

    private static ConvenienceLibVelocity INSTANCE;

    private final Logger logger;
    private final Path dataDirectory;

    @Getter
    private DatabaseConfiguration databaseConfiguration;

    @Inject
    public ConvenienceLibVelocity(@NotNull ProxyServer server, @NotNull Logger logger, @DataDirectory Path dataDirectory) {
        INSTANCE = this;

        this.logger = logger;
        this.dataDirectory = dataDirectory;
    }

    @Subscribe
    public void onProxyInitialization(@NotNull ProxyInitializeEvent event) {
        YamlRedisConfiguration yamlRedisConfiguration = new YamlRedisConfiguration(dataDirectory);
        YamlSQLConfiguration yamlSQLConfiguration = new YamlSQLConfiguration(dataDirectory);
        this.databaseConfiguration = new DatabaseConfiguration(yamlSQLConfiguration, yamlRedisConfiguration);
        logger.info("Loaded database configuration successfully.");
    }

    public static ConvenienceLibVelocity getInstance() {
        return INSTANCE;
    }

}
