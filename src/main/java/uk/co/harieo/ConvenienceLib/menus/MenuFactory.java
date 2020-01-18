package uk.co.harieo.ConvenienceLib.menus;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import java.util.*;

/**
 * A factory which stores information for and creates instances of {@link org.bukkit.inventory.Inventory} for the
 * purposes of handling interaction which is totally controlled by the system. This means that unless the system allows
 * it, a player can't edit this menu.
 *
 * All items in this factory are player-specific and should be set via {@link #setPlayerItems(Player)} in extensions
 * of this class.
 *
 * @author Harieo
 */
public abstract class MenuFactory {

	private String name;
	private int slots;

	private Table<UUID, Integer, MenuItem> items;
	private Map<UUID, MenuImpl> implementations = new HashMap<>();
	private MenuInteractionListener interactionListener;

	/**
	 * A new factory for creating menus
	 *
	 * @param inventoryName to be displayed with the {@link org.bukkit.inventory.Inventory}
	 * @param rows amount of rows (slots / 9) this menu will be
	 */
	public MenuFactory(String inventoryName, int rows) {
		this.name = inventoryName;
		this.slots = rows * 9;
		this.items = HashBasedTable.create();
		this.interactionListener = new MenuInteractionListener(this);
	}

	/**
	 * @return the name for this inventory, displayed to the player
	 */
	public String getInventoryName() {
		return name;
	}

	/**
	 * @return the amount of slots (row * 9) this inventory uses
	 */
	public int getSlotSize() {
		return slots;
	}

	/**
	 * @return a map of slots and what items go in that slot, if any
	 */
	public Map<Integer, MenuItem> getItems(Player player) {
		return items.row(player.getUniqueId());
	}

	/**
	 * Returns the {@link MenuItem} set to correspond to that slot, if any
	 *
	 * @param slot to get the item for
	 * @return the item or null of no item is found
	 */
	public MenuItem getItem(Player player, int slot) {
		return items.get(player.getUniqueId(), slot);
	}

	/**
	 * Sets the {@link MenuItem} for a specified slot. This will overwrite any previous item in that slot.
	 *
	 * @param slot for the menu item to take up
	 * @param item the menu item
	 */
	public void setItem(Player player, int slot, MenuItem item) {
		if (slot < slots) {
			if (items.contains(player.getUniqueId(), slot)) {
				items.remove(player.getUniqueId(), slot);
			}

			items.put(player.getUniqueId(), slot, item);
			updateSlot(player, slot); // Updates any open menus
		} else {
			throw new IllegalArgumentException(
					"Invalid menu slot. Size: " + slots + ", Index: " + slot);
		}
	}

	/**
	 * Clears all items for the player
	 *
	 * @param player to clear items for
	 */
	public void clearItems(Player player) {
		items.row(player.getUniqueId()).clear();
		if (implementations.containsKey(player.getUniqueId())) {
			implementations.get(player.getUniqueId()).clear();
		}
	}

	/**
	 * A method called before a player-specific implementation is made to set all the necessary GUI items
	 *
	 * @param player to set the items for
	 */
	public abstract void setPlayerItems(Player player);

	/**
	 * Retrieves the already created instance of {@link MenuImpl} or creates a new one for the specified player
	 *
	 * @param player to get the menu implementation for
	 * @return the menu implementation, either new or cached
	 */
	public MenuImpl getOrCreateMenu(Player player) {
		MenuImpl impl;
		setPlayerItems(player);
		if (implementations.containsKey(player.getUniqueId())) {
			impl = implementations.get(player.getUniqueId());
			impl.updateAll(); // Refresh in-case things changed since last access
			impl.setPlayer(player); // Refresh in-case the player went offline before this
		} else {
			impl = new MenuImpl(this, player);
			implementations.put(player.getUniqueId(), impl);
		}
		return impl;
	}

	/**
	 * Calls {@link MenuImpl#updateSlot(int)} for all instances inside this factory
	 *
	 * @param slot to be updated
	 */
	public void updateSlot(Player player, int slot) {
		implementations.forEach((uuid, impl) -> {
			if (uuid.equals(player.getUniqueId())) {
				impl.updateSlot(slot);
			}
		});
	}

	/**
	 * Registers an instance of {@link MenuInteractionListener} with Bukkit with the provided plugin executor
	 *
	 * @param plugin to register the listener under
	 */
	public void registerDefaultInteractionListener(JavaPlugin plugin) {
		Bukkit.getPluginManager().registerEvents(interactionListener, plugin);
	}

}
