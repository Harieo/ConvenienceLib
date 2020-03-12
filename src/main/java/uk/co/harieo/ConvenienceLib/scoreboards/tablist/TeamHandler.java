package uk.co.harieo.ConvenienceLib.scoreboards.tablist;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import java.util.Optional;
import uk.co.harieo.ConvenienceLib.scoreboards.GameBoardImpl;
import uk.co.harieo.ConvenienceLib.scoreboards.tablist.modules.Affix;
import uk.co.harieo.ConvenienceLib.scoreboards.tablist.modules.TabListProcessor;

/**
 * This class handles instances of {@link Team} in all implementations of {@link Scoreboard} which are attached.
 *
 * @author Harieo
 */
public class TeamHandler {

	private TabListProcessor processor;
	private Table<GameBoardImpl, Affix, Team> teams = HashBasedTable.create();

	/**
	 * A new instance of this handler with a valid {@link TabListProcessor} to handle core processes
	 *
	 * @param processor from the factory to be used
	 */
	TeamHandler(TabListProcessor processor) {
		this.processor = processor;
	}

	/**
	 * Creates a new instance of {@link Team} for every instance of {@link Affix} provided by the processor. This is for
	 * the purpose of setting the prefixes/suffixes as the processor requires.
	 *
	 * @param gameBoardImpl to inject the teams into
	 */
	public void injectTeams(GameBoardImpl gameBoardImpl) {
		Scoreboard scoreboard = gameBoardImpl.getScoreboard();
		for (Affix affix : processor.getAffixes().values()) {
			teams.remove(gameBoardImpl, affix); // New injection, all other values will be overwritten or deleted

			Team team = scoreboard.getTeam(affix.getUniqueId());
			if (team == null) {
				team = newTeam(scoreboard, affix); // As this is a new team, it needs assigning
			} else {
				editTeam(team, affix); // Just editing, no new variables
			}

			teams.put(gameBoardImpl, affix, team); // Add to the cache
		}
	}

	/**
	 * Calls {@link #editTeam(Team, Affix)} with a new instance of {@link Team}
	 *
	 * @param scoreboard to create the team under
	 * @param affix for the {@link #editTeam(Team, Affix)} method
	 * @return the newly created and edited team
	 */
	private Team newTeam(Scoreboard scoreboard, Affix affix) {
		return editTeam(scoreboard.registerNewTeam(affix.getUniqueId()), affix);
	}

	/**
	 * Edits an already created {@link Team} by setting the prefixes and suffixes from {@link Affix} if applicable
	 *
	 * @param team to edit
	 * @param affix to get the prefixes/affixes from
	 * @return the edited team
	 */
	private Team editTeam(Team team, Affix affix) {
		// Add prefix and suffix, if applicable
		setAffixes(team, affix);
		// Add to cache of available teams
		return team;
	}

	/**
	 * Sets the prefix and suffix of a team, if available, from the provided {@link Affix}
	 *
	 * @param team to edit the prefix and suffix of
	 * @param affix to get the prefix and suffix from
	 */
	private void setAffixes(Team team, Affix affix) {
		if (affix.hasPrefix()) {
			team.setPrefix(affix.getPrefix());
		}

		if (affix.hasSuffix()) {
			team.setSuffix(affix.getSuffix());
		}
	}

	/**
	 * Calls {@link #injectPlayer(Player)} on all online players
	 */
	public void injectOnlinePlayers() {
		Bukkit.getOnlinePlayers().forEach(this::injectPlayer);
	}

	/**
	 * Adds the provided Player to any Team which the {@link TabListProcessor} requests
	 *
	 * @param player to add to teams, where required
	 */
	public void injectPlayer(Player player) {
		Optional<Affix> optionalAffix = this.processor.getAffixForPlayer(player);
		for (GameBoardImpl impl : teams.rowKeySet()) {
			if (optionalAffix
					.isPresent()) { // If this is not present, assume the player shouldn't be handled in this case
				Affix affix = optionalAffix.get();
				if (teams.contains(impl, affix)) { // If the row hasn't been cleared (e.g is still active)
					Team team = teams.get(impl, affix);
					if (team.getEntries().contains(player.getName())) { // If the player is already added
						team.removeEntry(player.getName()); // Remove to replace
					}

					team.addEntry(player.getName());
				}
			}
		}
	}

	/**
	 * Clears all cached data from a {@link GameBoardImpl} so that it is no longer updated
	 *
	 * @param impl to have the data cleared from
	 */
	public void removeImpl(GameBoardImpl impl) {
		teams.row(impl).clear(); // This will stop unnecessary team editing as much as it can
	}

}
