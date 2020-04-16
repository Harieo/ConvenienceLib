package uk.co.harieo.ConvenienceLib.menus.extra;

import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.material.Dye;
import org.bukkit.material.MaterialData;
import uk.co.harieo.ConvenienceLib.menus.MenuFactory;
import uk.co.harieo.ConvenienceLib.menus.MenuItem;

public abstract class BorderedMenuFactory extends MenuFactory {

	private MenuItem border;

	public BorderedMenuFactory(MaterialData border, String inventoryName, int rows) {
		super(inventoryName, rows);

		this.border = new MenuItem(border.getItemType(), 1, border.getData());
	}

	public BorderedMenuFactory(String inventoryName, int rows) {
		this(new Dye(Material.STAINED_GLASS_PANE, DyeColor.BLACK.getWoolData()), inventoryName, rows);
	}

	@Override
	public final void setPlayerItems(Player player, int page) {
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
	 * The same thing as {@link MenuFactory#setPlayerItems(Player, int)}, this method exists
	 * because {@link BorderedMenuFactory} has its own implementation of
	 * the {@link MenuFactory#setPlayerItems(Player, int)} method.
	 *
	 * @param player to set the items for
	 * @param page the page should this factory be using pages (can be ignored)
	 */
	public abstract void setPlayerItemsImpl(Player player, int page);
}
