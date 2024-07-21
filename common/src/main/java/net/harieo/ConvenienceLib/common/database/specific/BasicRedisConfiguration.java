package net.harieo.ConvenienceLib.common.database.specific;

import lombok.Data;
import net.harieo.ConvenienceLib.common.database.api.RedisConfiguration;

@Data
public class BasicRedisConfiguration implements RedisConfiguration {

	private final String host;
	private final int port;
	private final int timeout;
	private final String password;
	private final int database;

}
