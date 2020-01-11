package uk.co.harieo.ConvenienceLib.scoreboards;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.*;

import java.util.*;
import java.util.function.Consumer;
import uk.co.harieo.ConvenienceLib.scoreboards.elements.RenderableElement;

/**
 * This class is the live implementation of {@link GameBoard} which takes the relevant configuration and renders it to
 * the provided player. In most cases, you will not need to use this class as it is done for you, unless you are using a
 * more advanced implementation of the Spigot {@link Scoreboard} API.
 */
public class GameBoardImpl {

	private static List<GameBoardImpl> cache = new ArrayList<>(); // A list of active implementations

	private GameBoard gameBoard;
	private Player player;
	private Map<Integer, RenderableElement> elements;
	private int updateTime;
	private Plugin plugin;

	private Scoreboard scoreboard;
	private Objective objective;
	private Map<Integer, Team> teams = new HashMap<>(15);

	private BukkitRunnable runnable;
	private Consumer<Scoreboard> beforeRender;

	/**
	 * An implementation of {@link GameBoard} to safely communicate with the player
	 *
	 * @param gameBoard the {@link GameBoard} which is handling this implementation's configuration
	 * @param plugin instance which is handling this system
	 * @param player to display this implementation to when called
	 * @param elements to display to the player
	 * @param displayName of the scoreboard
	 * @param slot the type of {@link DisplaySlot} the scoreboard should be in
	 * @param updateTime the tick interval to refresh elements in
	 */
	GameBoardImpl(GameBoard gameBoard, Plugin plugin, Player player, Map<Integer, RenderableElement> elements,
			String displayName, DisplaySlot slot, int updateTime) {
		this.gameBoard = gameBoard;
		this.player = player;
		this.elements = elements;
		this.updateTime = updateTime;
		this.plugin = plugin;

		ScoreboardManager manager = Bukkit.getScoreboardManager();
		Scoreboard scoreboard = manager.getNewScoreboard();
		Objective objective = scoreboard.registerNewObjective("main", "dummy");
		objective.setDisplayName(displayName);
		objective.setDisplaySlot(slot);
		this.scoreboard = scoreboard;
		this.objective = objective;
	}

	/**
	 * Begin rendering this implementation to the player
	 */
	void start() {
		for (int slot : elements.keySet()) {
			Team team = scoreboard.registerNewTeam("line" + slot);
			String lineColor = ChatColor.values()[slot].toString();
			team.addEntry(lineColor); // Same as the line it will be on
			teams.put(slot, team);
			objective.getScore(lineColor).setScore(slot);
		}

		if (beforeRender != null) {
			beforeRender.accept(scoreboard);
		}
		player.setScoreboard(scoreboard);
		cache.add(this); // Add to cache just before starting

		BukkitRunnable runnable = new BukkitRunnable() {
			@Override
			public void run() {
				if (!player.isOnline()) {
					cancel();
					return;
				}

				for (int slot : teams.keySet()) {
					try {
						compressText(teams.get(slot), elements.get(slot).getText(player));
					} catch (IllegalArgumentException e) { // Thrown if formatting failed
						e.printStackTrace();
						cancel();
					}
				}
			}
		};
		runnable.runTaskTimer(plugin, 0, updateTime);
		this.runnable = runnable;
	}

	/**
	 * Stop rendering this to the player and remove it from the cache
	 */
	void stop() {
		runnable.cancel();
		cache.remove(this);
		gameBoard.getImplementations()
				.remove(player.getUniqueId()); // Declares that this implementation is no longer viable
	}

	/**
	 * Compresses a String of up to 32 characters into a team by splitting them evenly into prefix and suffix
	 *
	 * @param team to put the compressed text into
	 * @param text to be compressed
	 * @throws IllegalArgumentException if the string was more than 32 characters, including after compression
	 */
	private void compressText(Team team, String text) throws IllegalArgumentException {
		// Teams only allow up to 16 chars so we'll split them in half and feed the second half into suffix
		String prefix = text;
		String suffix = null; // Anything under 16 doesn't need editing
		if (prefix.length() > 32) { // Using scoreboard content isn't dynamic enough so max at 32
			throw new IllegalArgumentException("An element had more than 32 characters: " + prefix);
		} else if (prefix.length() > 16) { // Splitting is required now
			suffix = prefix.substring(15);
			prefix = prefix.substring(0, prefix.length() - suffix.length());
			suffix = ChatColor.getLastColors(prefix) + suffix; // Fix any color leakage due to the split
		}

		team.setPrefix(prefix);
		if (suffix != null) {
			team.setSuffix(suffix);
		}
	}

	/**
	 * Set an active process which will occur <b>before</b> this {@link Scoreboard} is displayed to the player but after
	 * the pre-processes of this implementation have been completed (e.g setting up the elements ready to display)
	 *
	 * @param consumer to be accepted before rendering to the player in {@link #start()}
	 */
	public void consumeBeforeRender(Consumer<Scoreboard> consumer) {
		this.beforeRender = consumer;
	}

	/**
	 * @return the scoreboard generated by this implementation
	 */
	public Scoreboard getScoreboard() {
		return scoreboard;
	}

	/**
	 * @return a live list of all instances of {@link GameBoardImpl} which are currently running
	 */
	public static List<GameBoardImpl> getActiveImplementations() {
		return cache;
	}

}
