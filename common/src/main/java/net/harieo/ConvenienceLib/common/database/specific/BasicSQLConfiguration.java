package net.harieo.ConvenienceLib.common.database.specific;

import lombok.Data;
import net.harieo.ConvenienceLib.common.database.api.SQLConfiguration;

@Data
public class BasicSQLConfiguration implements SQLConfiguration {

	private final String host;
	private final int port;
	private final String database;
	private final String username;
	private final String password;
	private final String append;

}
