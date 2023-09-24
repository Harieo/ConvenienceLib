package net.harieo.ConvenienceLib.common.database.specific;

import lombok.Data;

@Data
public class RedisConfiguration {

	private final String host;
	private final int port;
	private final int timeout;
	private final String password;
	private final int database;

}
