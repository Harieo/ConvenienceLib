package uk.co.harieo.ConvenienceLib.scoreboards;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

import java.util.*;
import java.util.function.Consumer;
import uk.co.harieo.ConvenienceLib.scoreboards.elements.ConstantElement;
import uk.co.harieo.ConvenienceLib.scoreboards.elements.RenderableElement;
import uk.co.harieo.ConvenienceLib.scoreboards.tablist.TabListFactory;
import uk.co.harieo.ConvenienceLib.scoreboards.tablist.modules.TabListProcessor;

/**
 * This is the primary interface for the Scoreboard API which handles the creation and implementation of {@link
 * GameBoardImpl}, which handles all back-end parts of rendering a scoreboard.
 *
 * Important to note: While your scoreboard only needs 1 of this class at any time, there will be a new implementation
 * for each player. This class is unique to a scoreboard, {@link GameBoardImpl} is not.
 *
 * @author Harieo
 */
public class GameBoard {

	private String displayName;
	private DisplaySlot slot;
	private Map<UUID, GameBoardImpl> impls = new HashMap<>();
	private Map<Integer, RenderableElement> elements = new HashMap<>();
	private Consumer<Scoreboard> beforeRender;
	private TabListFactory tabListFactory = new TabListFactory(this);

	public GameBoard(String displayName, DisplaySlot displaySlot) {
		this.displayName = displayName;
		this.slot = displaySlot;
	}

	/**
	 * Sets a line on the scoreboard using {@link RenderableElement}
	 *
	 * Lines start from the top of the scoreboard, line 1 would be score 15 when referencing {@link Score}
	 *
	 * @param element to be set
	 */
	public void addLine(RenderableElement element) {
		int line = 15; // Start at the top and go down
		while (elements.containsKey(line)) {
			line--; // Decrease line until the line is not taken
		}

		if (line < 1) { // This is the limit for Scoreboard in Bukkit
			throw new IllegalStateException("Lines cannot exceed 15 in GameBoard");
		}

		elements.put(line, element);
	}

	/**
	 * Adds a blank instance of {@link ConstantElement} to act as white space
	 *
	 * Lines start from the top of the scoreboard, line 1 would be score 15 when referencing {@link Score}
	 */
	public void addBlankLine() {
		int line = 15; // Start at the top and go down
		while (elements.containsKey(line)) {
			line--; // Decrease line until the line is not taken
		}

		if (line < 1) { // This is the limit for Scoreboard in Bukkit
			throw new IllegalStateException("Lines cannot exceed 15 in GameBoard");
		}

		// Each blank line needs to be different so pick a unique colour using line number
		RenderableElement blankElement = new ConstantElement(ChatColor.values()[line].toString());
		elements.put(line, blankElement);
	}

	/**
	 * Clears all elements from this scoreboard
	 */
	public void clearLines() {
		elements.clear();
	}

	/**
	 * @return the amount of elements/lines which have been added
	 */
	public int getLineCount() {
		return elements.size();
	}

	/**
	 * Note: Use of this factory is optional and it will only be activated if you call {@link
	 * TabListFactory#injectProcessor(TabListProcessor)} on it.
	 *
	 * @return the instantiated instance of {@link TabListFactory} for this scoreboard
	 */
	public TabListFactory getTabListFactory() {
		return tabListFactory;
	}

	/**
	 * This class will provide a consumer to the {@link GameBoardImpl#consumeBeforeRender(Consumer)} method every time a
	 * new implementation is created.
	 *
	 * This is <b>not</b> a consumer which will happen before {@link #render(JavaPlugin, Player, int)} is called, as you
	 * can easily implement this yourself (the method is not automatic).
	 *
	 * @param scoreboardConsumer to be provided
	 */
	public void beforeRender(Consumer<Scoreboard> scoreboardConsumer) {
		this.beforeRender = scoreboardConsumer;
	}

	/**
	 * Sets all values into the Scoreboard and assigns it to the Player
	 *
	 * @param plugin that is using this Scoreboard
	 * @param player that this scoreboard is being used for
	 * @param time in ticks for every update of the scoreboard
	 */
	public void render(JavaPlugin plugin, Player player, int time) {
		GameBoardImpl impl = new GameBoardImpl(this, plugin, player, elements, displayName, slot, time);
		if (beforeRender != null) { // Provides the Consumer before it will actually be needed
			impl.consumeBeforeRender(beforeRender);
		}
		impls.put(player.getUniqueId(), impl);
		impl.start();

		if (getTabListFactory().isActivated()) {
			getTabListFactory().handleNewImpl(impl);
		}
	}

	void invalidateImplementation(UUID uuid, GameBoardImpl impl) {
		if (getTabListFactory().isActivated()) {
			getTabListFactory().handleDeleteImpl(impl);
		}
		impls.remove(uuid);
	}

	/**
	 * Stops updating the Scoreboard for the stated Player and removes it from the cache. Note: This does not reset
	 * their scoreboard.
	 *
	 * @param player to cancel for
	 */
	public void cancelScoreboard(Player player) {
		if (impls.containsKey(player.getUniqueId())) {
			impls.get(player.getUniqueId()).stop();
		}
	}

	/**
	 * @return a list of all active implementations
	 */
	public Map<UUID, GameBoardImpl> getImplementations() {
		return impls;
	}

}
