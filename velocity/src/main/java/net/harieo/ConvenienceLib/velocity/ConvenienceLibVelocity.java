package net.harieo.ConvenienceLib.velocity;

import com.google.inject.Inject;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;
import java.util.logging.Logger;

@Plugin(id = "ConvenienceLib", name = "ConvenienceLib", version = "1.2.1",
        url = "https://github.com/Harieo/ConvenienceLib", authors = {"Harieo"})
public class ConvenienceLibVelocity {

    @Inject
    public ConvenienceLibVelocity(@NotNull ProxyServer server, @NotNull Logger logger, @DataDirectory Path dataDirectory) {
        // Using this to show the order of loading plugins - Don't forget to add this plugin as a dependency!
        logger.info("ConvenienceLib has been loaded onto your server at this stage.");
    }

}
