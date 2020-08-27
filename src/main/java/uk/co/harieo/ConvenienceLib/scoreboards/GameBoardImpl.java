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

	private static final List<GameBoardImpl> cache = new ArrayList<>(); // A list of active implementations

	private final GameBoard gameBoard;
	private final Player player;
	private final Map<Integer, RenderableElement> elements;
	private final int updateTime;
	private final Plugin plugin;

	private final Scoreboard scoreboard;
	private final Objective objective;
	private final Map<Integer, Team> teams = new HashMap<>(15);

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
	 * @throws IllegalArgumentException if the string was more than the maximum allowed characters, including after
	 * compression
	 */
	private void compressText(Team team, String text) throws IllegalArgumentException {
		// Teams only allow up to 16 chars so we'll split them in half and feed the second half into suffix
		String prefix = text;
		String suffix = null;

		int maxSplit = 16; // The maximum amount of characters allowed in either the prefix or suffix
		if (prefix.length() > maxSplit * 2) { // Using scoreboard content isn't dynamic enough so max at 32
			throw new IllegalArgumentException("An element had more than " + (maxSplit * 2) + " characters: " + prefix);
		} else if (prefix.length() > maxSplit) { // Splitting is required now
			char[] textChars = text.toCharArray();
			List<String> textElements = new ArrayList<>();

			StringBuilder stringBuilder = new StringBuilder();
			for (int i = 0; i < text.length(); i++) { // Iterate through all the characters of text
				char character = textChars[i];
				if (character == ChatColor.COLOR_CHAR) { // If we've found a colour code
					if (stringBuilder.length() > 0) { // If there was standard text for this
						textElements.add(stringBuilder.toString()); // Add that text as an element
						stringBuilder = new StringBuilder(); // Reset for use below
					}

					// Checking if there are more colour codes after this one
					while (character == ChatColor.COLOR_CHAR) {
						stringBuilder.append(character); // This should just be the colour char
						if (i + 1 < textChars.length) { // Make sure the actual code can be found
							stringBuilder.append(textChars[i + 1]);
						} else {
							break; // If we don't have +1 index, the below will automatically fail when trying +2 index
						}

						i += 2; // Add 2 to get to the next possible code because this one has already been handled above

						if (i < textChars.length) { // If there is a character after these 2
							character = textChars[i];
							if (character != ChatColor.COLOR_CHAR) {
								// This while loop will quit but the for loop will add 1, resulting in a lost char
								i -= 1;
							}
						} else {
							break;
						}
					}

					textElements.add(stringBuilder.toString());
					stringBuilder = new StringBuilder();
				} else {
					stringBuilder.append(character);
				}
			}

			if (stringBuilder.length() > 0) { // If anything is left
				textElements.add(stringBuilder.toString()); // Add to elements
			}

			StringBuilder prefixBuffer = new StringBuilder();
			StringBuilder suffixBuffer = new StringBuilder();
			for (String element : textElements) { // Add elements to the prefix and suffix where possible
				int elementLength = element.length();
				int currentPrefixLength = prefixBuffer.length();
				int currentSuffixLength = suffixBuffer.length();

				boolean hasNoSuffix = currentSuffixLength == 0;

				if (!element.contains(String.valueOf(ChatColor.COLOR_CHAR))) { // If this string isn't a colour code
					// Attempt to split the string into two pieces for the prefix and suffix
					int prefixLengthDifference = maxSplit - currentPrefixLength;
					int suffixLengthDifference = maxSplit - currentSuffixLength;

					int subIndex = 0;
					if (prefixLengthDifference > 0 && hasNoSuffix) { // Don't add to prefix if using suffix
						subIndex = prefixLengthDifference;
						if (subIndex >= elementLength) {
							subIndex = elementLength;
						}

						prefixBuffer.append(element, 0, subIndex);
					}

					if (subIndex < elementLength && suffixLengthDifference > 0) {
						int suffixIndexEnd = subIndex + suffixLengthDifference;
						suffixBuffer.append(element, subIndex,
								Math.min(suffixIndexEnd, elementLength));
					}
				} else if (currentPrefixLength + elementLength <= maxSplit
						&& hasNoSuffix) { // If the entire string fits into the prefix
					prefixBuffer.append(element);
				} else if (currentSuffixLength + elementLength
						<= maxSplit) { // If the entire string fits into the suffix
					suffixBuffer.append(element);
				} else {
					throw new IllegalStateException("Elements don't fit a " + maxSplit + "-character split");
				}
			}

			prefix = prefixBuffer.toString();
			suffix = suffixBuffer.toString();
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
	 * @return the {@link GameBoard} which is handling this implementation
	 */
	public GameBoard getGameBoard() {
		return gameBoard;
	}

	/**
	 * @return the Player which this implementation is meant to render for
	 */
	public Player getPlayer() {
		return player;
	}

	/**
	 * @return a live list of all instances of {@link GameBoardImpl} which are currently running
	 */
	public static List<GameBoardImpl> getActiveImplementations() {
		return cache;
	}

}
