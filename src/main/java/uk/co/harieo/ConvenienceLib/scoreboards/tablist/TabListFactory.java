package uk.co.harieo.ConvenienceLib.scoreboards.tablist;

import uk.co.harieo.ConvenienceLib.scoreboards.GameBoard;
import uk.co.harieo.ConvenienceLib.scoreboards.GameBoardImpl;
import uk.co.harieo.ConvenienceLib.scoreboards.tablist.modules.TabListProcessor;

/**
 * This is the primary class for the tab list API which is inertly attached to {@link GameBoard} on creation. The
 * purpose of this factory is to remain inactive until a {@link TabListProcessor} is injected, at which point a new
 * {@link TeamHandler} is created to edit the tab list for a Player.
 *
 * In summary, this class edits the tab list for you when you implement {@link GameBoard} and allows for a custom
 * processor of your design to handle what prefixes and suffixes are attached to players, if any.
 *
 * @author Harieo
 */
public class TabListFactory {

	private GameBoard gameBoard;
	private TabListProcessor injectedProcessor;
	private TeamHandler teamHandler;

	/**
	 * A new instance of this factory on initial creation of {@link GameBoard}
	 *
	 * @param gameBoard which this factory is attached to
	 */
	public TabListFactory(GameBoard gameBoard) {
		this.gameBoard = gameBoard;
	}

	/**
	 * Activates this factory by loading information from the provided processor and injecting it into the {@link
	 * GameBoard} which this factory is located in.
	 *
	 * @param processor to be injected
	 */
	public void injectProcessor(TabListProcessor processor) {
		if (this.injectedProcessor != null) {
			throw new IllegalStateException("Cannot inject more than 1 processor into a TabListFactory");
		}

		this.injectedProcessor = processor;
		this.teamHandler = new TeamHandler(processor); // Created only when there is a processor, as it is required
		updateAll(); // The new TeamHandler will need to run its internal processes
	}

	/**
	 * Injects all the necessary teams into a new {@link GameBoardImpl} via {@link TeamHandler} assuming that this
	 * factory has already been activated.
	 *
	 * @param impl to inject teams into
	 */
	public void handleNewImpl(GameBoardImpl impl) {
		if (isActivated()) {
			teamHandler.injectTeams(impl);
		}
	}

	public void handleDeleteImpl(GameBoardImpl impl) {
		if (isActivated()) {
			teamHandler.removeImpl(impl);
		}
	}

	/**
	 * @return whether a {@link TabListProcessor} has been injected into this class (and by extension, is activated)
	 */
	public boolean isActivated() {
		return injectedProcessor != null;
	}

	/**
	 * Updates all {@link GameBoardImpl} instances with the {@link TeamHandler} then calls {@link
	 * TeamHandler#injectOnlinePlayers()}
	 */
	public void updateAll() {
		if (isActivated()) {
			// Begin injecting all available affixes into all available implemented scoreboards
			for (GameBoardImpl impl : gameBoard.getImplementations().values()) {
				teamHandler.injectTeams(impl);
			}

			teamHandler.injectOnlinePlayers();
		}
	}

}
