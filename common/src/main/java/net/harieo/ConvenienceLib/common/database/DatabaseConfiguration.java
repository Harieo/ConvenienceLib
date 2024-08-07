package net.harieo.ConvenienceLib.common.database;

import lombok.Data;
import net.harieo.ConvenienceLib.common.database.api.RedisConfiguration;
import net.harieo.ConvenienceLib.common.database.api.SQLConfiguration;
import org.jetbrains.annotations.NotNull;

/**
 * A handler which stores both {@link SQLConfiguration} and {@link RedisConfiguration}
 * for {@link DatabaseManager}.
 */
@Data
public class DatabaseConfiguration {

	private final @NotNull SQLConfiguration sqlConfiguration;
	private final @NotNull RedisConfiguration redisConfiguration;

}
