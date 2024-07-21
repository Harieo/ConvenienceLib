package uk.co.harieo.ConvenienceLib.menus.extra;

import org.bukkit.Material;
import org.bukkit.entity.Player;

import uk.co.harieo.ConvenienceLib.menus.MenuFactory;
import uk.co.harieo.ConvenienceLib.menus.MenuItem;

/**
 * Contribution by deanveloper
 *
 * @author deanveloper
 */
@Deprecated
public abstract class BorderedMenuFactory extends MenuFactory {

	private final MenuItem border;

	/**
	 * A menu factory which adds a border around the edge of the menu using the provided display item, containing all
	 * added items inside it.
	 *
	 * @param material to create a display item for the border out of
	 * @param inventoryName of the menu
	 * @param rows in the menu
	 */
	public BorderedMenuFactory(Material material, String inventoryName, int rows) {
		super(inventoryName, rows);

		this.border = new MenuItem(material);
	}

	/**
	 * An overload of {@link #BorderedMenuFactory(Material, String, int)} where the material is {@link
	 * Material#GRAY_STAINED_GLASS_PANE}
	 *
	 * @param inventoryName of the menu
	 * @param rows in the menu
	 */
	public BorderedMenuFactory(String inventoryName, int rows) {
		this(Material.GRAY_STAINED_GLASS_PANE, inventoryName, rows);
	}

	@Override
	public final void setPlayerItems(Player player, int page) {
		clearItems(player);
		for (int x = 0; x < 9; x++) {
			setItem(player, x, border);
			setItem(player, x + (getSlotSize() - 9), border);
		}

		for (int y = 9; y < getSlotSize() - 9; y += 9) {
			setItem(player, y, border);
			setItem(player, y + 8, border);
		}

		setPlayerItemsImpl(player, page);
	}

	/**
	 * A method called before a player-specific implementation is made to set all the necessary GUI items.
	 *
	 * The same thing as {@link MenuFactory#setPlayerItems(Player, int)}, this method exists because {@link
	 * BorderedMenuFactory} has its own implementation of the {@link MenuFactory#setPlayerItems(Player, int)} method.
	 *
	 * @param player to set the items for
	 * @param page the page should this factory be using pages (can be ignored)
	 */
	public abstract void setPlayerItemsImpl(Player player, int page);
}
