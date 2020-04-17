package uk.co.harieo.ConvenienceLib.menus;

import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;

public class MenuInteractionListener implements Listener {

	private final MenuFactory factory;

	/**
	 * A generic {@link Listener} which listens for the factory's inventory being clicked then passes the event onto
	 * any instance of {@link MenuItem} in the slot which was clicked
	 *
	 * @param factory to listen for
	 */
	public MenuInteractionListener(MenuFactory factory) {
		this.factory = factory;
	}

	@EventHandler
	public void onInventoryClick(InventoryClickEvent event) {
		HumanEntity entity = event.getWhoClicked();
		if (entity instanceof Player) {
			Player player = (Player) entity;
			Inventory inventory = event.getClickedInventory();
			if (inventory != null && inventory.getName().equals(factory.getInventoryName())) {
				event.setCancelled(true);
				MenuItem item = factory.getItem(player, event.getSlot());
				if (item != null) {
					item.onClick(player);
				}
			}
		}
	}

	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		HumanEntity entity = event.getPlayer();
		if (entity instanceof Player) {
			factory.cleanup((Player) entity);
		}
	}
}
