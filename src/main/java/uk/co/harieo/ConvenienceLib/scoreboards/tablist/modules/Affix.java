package uk.co.harieo.ConvenienceLib.scoreboards.tablist.modules;

/**
 * This interface represents a linked pair of 1 prefix and 1 suffix which can be assigned to a Player in the tab list.
 * Due to the way {@link org.bukkit.scoreboard.Team} works, these are linked together to emulate a Team so that the full
 * use of the API can be made (such as ordering a tab list).
 *
 * Note: the {@link #getUniqueId()} is what will be considered the team name and by the APIs design will be used to
 * order the tab list. Descending alphabetical order.
 *
 * @author Harieo
 */
public class Affix {

	private String id;
	private String prefix;
	private String suffix;

	/**
	 * A representation of either a prefix, suffix or both in a {@link org.bukkit.scoreboard.Team}
	 *
	 * @param id to be the Team name and to organise this in processing. Must be unique to prevent errors.
	 */
	public Affix(String id) {
		this.id = id;
	}

	/**
	 * @return the unique name of this affix
	 */
	public String getUniqueId() {
		return id;
	}

	/**
	 * @return the prefix, if set
	 */
	public String getPrefix() {
		return prefix;
	}

	/**
	 * Sets the prefix
	 *
	 * @param prefix to set the prefix to
	 * @return the updated instance
	 */
	public Affix setPrefix(String prefix) {
		this.prefix = prefix;
		return this;
	}

	/**
	 * @return the suffix, if available
	 */
	public String getSuffix() {
		return suffix;
	}

	/**
	 * Sets the suffix
	 *
	 * @param suffix to set the suffix to
	 * @return the updated instance
	 */
	public Affix setSuffix(String suffix) {
		this.suffix = suffix;
		return this;
	}

	/**
	 * @return whether this Affix has a set prefix
	 */
	public boolean hasPrefix() {
		return prefix != null;
	}

	/**
	 * @return whether this Affix has a set suffix
	 */
	public boolean hasSuffix() {
		return suffix != null;
	}

}
