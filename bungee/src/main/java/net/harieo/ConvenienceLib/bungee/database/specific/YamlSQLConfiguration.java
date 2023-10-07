package net.harieo.ConvenienceLib.bungee.database.specific;

import lombok.Getter;
import net.harieo.ConvenienceLib.common.database.specific.SQLConfiguration;
import net.md_5.bungee.config.Configuration;
import org.jetbrains.annotations.NotNull;

/**
 * An {@link SQLConfiguration} which parses value from a YAML configuration format.
 */
@Getter
public class YamlSQLConfiguration extends SQLConfiguration {

	/**
	 * Takes a Bungee {@link net.md_5.bungee.config.Configuration} and parses fields with the prefix 'mysql.' (e.g., 'mysql.host').
	 *
	 * @param configuration the YAML configuration which holds the SQL database configuration values
	 */
	public YamlSQLConfiguration(@NotNull Configuration configuration) {
		super(
				configuration.getString("mysql.host"),
				configuration.getInt("mysql.port"),
				configuration.getString("mysql.database"),
				configuration.getString("mysql.username"),
				configuration.getString("mysql.password"),
				configuration.getString("mysql.append")
		);
	}

}
