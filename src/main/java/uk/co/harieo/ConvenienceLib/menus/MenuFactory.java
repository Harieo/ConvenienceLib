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
 * All items in this factory are player-specific and should be set via {@link #setPlayerItems(Player, int)} in extensions
 * of this class.
 *
 * @author Harieo
 */
public abstract class MenuFactory {

	private final String name;
	private final int slots;

	private final Table<UUID, Integer, MenuItem> items;
	private final Map<UUID, MenuImpl> implementations = new HashMap<>();
	private final MenuInteractionListener interactionListener;

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
	 * Adds an {@link MenuItem} in the next available slot. Throws {@link IllegalStateException} if there
	 * are no available slots remaining.
	 *
	 * @param player the player to add the item to.
	 * @param item the menu item.
	 */
	public void addItem(Player player, MenuItem item) {
		int slot;
		for (slot = 0; slot < slots; slot++) {
			if (!items.contains(player.getUniqueId(), slot)) {
				break;
			}
		}

		if (slot >= slots) {
			throw new IllegalStateException("cannot add to a full inventory (slots: " + slot + ")");
		}
		items.put(player.getUniqueId(), slot, item);
		updateSlot(player, slot); // Updates any open menus
	}

	/**
	 * Sets the {@link MenuItem} for a specified slot. This will overwrite any previous item in that slot.
	 *
	 * @param slot for the menu item to take up.
	 * @param item the menu item.
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
	 * @param page the page should this factory be using pages (can be ignored)
	 */
	public abstract void setPlayerItems(Player player, int page);

	/**
	 * Retrieves the already created instance of {@link MenuImpl} or creates a new one for the specified player
	 *
	 * @param player to get the menu implementation for
	 * @param page the page the player is on
	 * @return the menu implementation, either new or cached
	 */
	public MenuImpl getOrCreateMenu(Player player, int page) {
		MenuImpl impl;
		setPlayerItems(player, page);
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
	 * Overloads {@link #getOrCreateMenu(Player, int)} with page as 1
	 *
	 * @param player to create the menu for
	 * @return the implementation at default page 1
	 */
	public MenuImpl getOrCreateMenu(Player player) {
		return getOrCreateMenu(player, 1);
	}

	/**
	 * Retrieves a cached {@link MenuImpl} if one exists
	 *
	 * @param player to get the cached menu for
	 * @return the cached instance or null if none found
	 */
	public MenuImpl getMenu(Player player) {
		return implementations.get(player.getUniqueId());
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

	/**
	 * Removes all cached inventories and items for a specified player
	 *
	 * @param player to remove all implementations for
	 */
	void cleanup(Player player) {
		// remove items for the player
		Map<Integer, MenuItem> itemsForPlayer = items.row(player.getUniqueId());
		if (itemsForPlayer != null) {
			for (Integer i : new ArrayList<>(itemsForPlayer.keySet())) {
				items.remove(player.getUniqueId(), i);
			}
		}

		// delete MenuImpl instance
		implementations.remove(player.getUniqueId());
	}
}
