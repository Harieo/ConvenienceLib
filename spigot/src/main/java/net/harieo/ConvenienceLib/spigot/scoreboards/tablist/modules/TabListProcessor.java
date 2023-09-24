package net.harieo.ConvenienceLib.spigot.scoreboards.tablist.modules;

import org.bukkit.entity.Player;

import java.util.*;

/**
 * A generic processor which handles the processing of prefixes and suffixes for players in tab list.
 *
 * @author Harieo
 */
public abstract class TabListProcessor {

	private Map<String, Affix> affixes;

	// Abstract Methods //

	/**
	 * A list of initial prefixes and suffixes which will be pre-loaded before rendering of a scoreboard, which are slightly safer to
	 * use than appended versions.
	 *
	 * @return an initial list of {@link Affix} which represent all initially possible prefixes and suffixes.
	 */
	protected abstract List<Affix> getInitialAffixes();

	/**
	 * A method which will determine what initial prefix should be used for the specified {@link Player}
	 *
	 * @param player to get the prefix for
	 * @return an {@link Optional} which represents the prefix to be used or none if no prefix should be used
	 */
	public abstract Optional<Affix> getAffixForPlayer(Player player);

	// Methods Available on Post-Instantiation //

	/**
	 * This method will take from {@link #getInitialAffixes()} on first call
	 *
	 * @return a map of unique ids and their affixes which represent a validated list of prefixes which can be used
	 */
	public Map<String, Affix> getAffixes() {
		if (affixes == null) { // Do this here as thread issues occur if done on instantiation (super doesn't work)
			List<Affix> initialAffixes = validateAffixList(getInitialAffixes()); // Make sure the content is valid
			affixes = mapByUniqueId(initialAffixes);
		}
		return affixes;
	}

	// Utility Methods without Dependency on Instantiation //

	/**
	 * Converts a list of {@link Affix} into a Map of {@link Affix#getUniqueId()} to {@link Affix} so they can be more
	 * easily identified by their string id.
	 *
	 * @param affixes to be split into a Map
	 * @return the split Map
	 */
	private Map<String, Affix> mapByUniqueId(List<Affix> affixes) {
		Map<String, Affix> map = new HashMap<>();
		for (Affix affix : affixes) {
			map.put(affix.getUniqueId(), affix);
		}
		return map;
	}

	/**
	 * Validates the ids and bodies of a provided list of {@link Affix} to ensure there will be no violations to integrity
	 * when they are used.
	 *
	 * @param affixes to be validated
	 */
	private static List<Affix> validateAffixList(List<Affix> affixes) {
		List<Affix> validatedList = new ArrayList<>(affixes);
		for (Affix affix : affixes) {
			boolean remove = false;
			if (affix.hasPrefix()) {
				if (affix.getPrefix().length() > 16) {
					remove = true;
				}
			}

			if (affix.hasSuffix()) {
				if (affix.getSuffix().length() > 16) {
					remove = true;
				}
			}

			if (affix.getUniqueId() == null || affix.getUniqueId().isEmpty()) {
				remove = true;
			}

			if (remove) {
				validatedList.remove(affix);
			}
		}

		return validatedList;
	}

}
