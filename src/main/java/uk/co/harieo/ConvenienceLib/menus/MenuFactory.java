package uk.co.harieo.ConvenienceLib.menus;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

/**
 * A factory which stores information for and creates instances of {@link org.bukkit.inventory.Inventory} for the
 * purposes of handling interaction which is totally controlled by the system. This means that unless the system allows
 * it, a player can't edit this menu.
 *
 * @author Harieo
 */
public abstract class MenuFactory {

	private String name;
	private int slots;

	private Map<Integer, MenuItem> items;
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
		this.items = new HashMap<>(slots);
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
	public Map<Integer, MenuItem> getItems() {
		return items;
	}

	/**
	 * Returns the {@link MenuItem} set to correspond to that slot, if any
	 *
	 * @param slot to get the item for
	 * @return the item or null of no item is found
	 */
	public MenuItem getItem(int slot) {
		return items.get(slot);
	}

	/**
	 * Sets the {@link MenuItem} for a specified slot. This will overwrite any previous item in that slot.
	 *
	 * @param slot for the menu item to take up
	 * @param item the menu item
	 */
	public void setItem(int slot, MenuItem item) {
		if (slot >= items.size()) {
			if (items.containsKey(slot)) {
				items.replace(slot, item);
			} else {
				items.put(slot, item);
			}
			updateSlot(slot); // Updates any open menus
		} else {
			throw new IllegalArgumentException(
					"Invalid menu slot. Size: " + items.size() + ", Index: " + slot);
		}
	}

	/**
	 * Retrieves the already created instance of {@link MenuImpl} or creates a new one for the specified player
	 *
	 * @param player to get the menu implementation for
	 * @return the menu implementation, either new or cached
	 */
	public MenuImpl getOrCreateMenu(Player player) {
		if (implementations.containsKey(player.getUniqueId())) {
			return implementations.get(player.getUniqueId());
		} else {
			MenuImpl impl = new MenuImpl(this, player);
			implementations.put(player.getUniqueId(), impl);
			return impl;
		}
	}

	/**
	 * Calls {@link MenuImpl#updateSlot(int)} for all instances inside this factory
	 *
	 * @param slot to be updated
	 */
	public void updateSlot(int slot) {
		implementations.values().forEach(impl -> impl.updateSlot(slot));
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
