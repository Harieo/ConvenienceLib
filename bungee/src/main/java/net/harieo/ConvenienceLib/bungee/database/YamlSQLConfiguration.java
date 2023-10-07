package net.harieo.ConvenienceLib.bungee.database;

import lombok.Getter;
import net.harieo.ConvenienceLib.bungee.config.BungeeConfigurationHandler;
import net.harieo.ConvenienceLib.common.database.api.SQLConfiguration;
import net.harieo.ConvenienceLib.common.database.specific.BasicSQLConfiguration;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;

/**
 * An {@link BasicSQLConfiguration} which parses value from a YAML configuration format.
 */
@Getter
public class YamlSQLConfiguration
		extends BungeeConfigurationHandler
		implements SQLConfiguration {

	public static final String DEFAULT_FILE_NAME = YamlRedisConfiguration.DEFAULT_FILE_NAME;

	private final String host;
	private final int port;
	private final String database;
	private final String username;
	private final String password;
	private final String append;

	/**
	 * Takes a {@link Configuration} and parses fields with the prefix 'mysql.' (e.g., 'mysql.host').
	 *
	 * @param plugin the plugin containing the database configuration file
	 * @param fileName the name of the database configuration file
	 */
	public YamlSQLConfiguration(@NotNull Plugin plugin, @NotNull String fileName) {
		super(plugin, fileName);
		try {
			Configuration configuration = getConfiguration();
			this.host = configuration.getString("mysql.host");
			this.port = configuration.getInt("mysql.port");
			this.database = configuration.getString("mysql.database");
			this.username = configuration.getString("mysql.username");
			this.password = configuration.getString("mysql.password");
			this.append = configuration.getString("mysql.append");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * An overload of {@link YamlSQLConfiguration} which uses {@link #DEFAULT_FILE_NAME} as the file
	 * name.
	 *
	 * @param plugin the plugin containing the data file.
	 */
	public YamlSQLConfiguration(@NotNull Plugin plugin) {
		this(plugin, DEFAULT_FILE_NAME);
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
	public String getDatabase() {
		return database;
	}

	@Override
	public String getUsername() {
		return username;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getAppend() {
		return append;
	}

}
