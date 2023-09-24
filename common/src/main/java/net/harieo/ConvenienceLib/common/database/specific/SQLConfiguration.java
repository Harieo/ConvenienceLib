package net.harieo.ConvenienceLib.common.database.specific;

import lombok.Data;

@Data
public class SQLConfiguration {

	private final String host;
	private final int port;
	private final String database;
	private final String username;
	private final String password;
	private final String append;

	/**
	 * @return the address of the MySQL database consisting of host and port
	 */
	public String getAddress() {
		return getHost() + ":" + getPort();
	}

}
