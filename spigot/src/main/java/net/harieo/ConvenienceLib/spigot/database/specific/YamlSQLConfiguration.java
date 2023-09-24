package net.harieo.ConvenienceLib.spigot.database.specific;

import lombok.Getter;
import net.harieo.ConvenienceLib.common.database.specific.SQLConfiguration;
import org.bukkit.configuration.file.FileConfiguration;

/**
 * An {@link SQLConfiguration} which parses value from a YAML configuration format.
 */
@Getter
public class YamlSQLConfiguration extends SQLConfiguration {

	/**
	 * Takes a {@link FileConfiguration} and parses fields with the prefix 'mysql.' (e.g., 'mysql.host').
	 *
	 * @param configuration the YAML configuration which holds the SQL database configuration values
	 */
	public YamlSQLConfiguration(FileConfiguration configuration) {
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
