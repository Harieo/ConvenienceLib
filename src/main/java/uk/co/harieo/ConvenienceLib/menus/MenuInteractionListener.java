package uk.co.harieo.ConvenienceLib.menus;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
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
		if (event.getWhoClicked() instanceof Player) {
			Player player = (Player) event.getWhoClicked();
			int slotClicked = event.getSlot();

			Inventory clickedInventory = event.getClickedInventory();
			Inventory factoryInventory = factory.getMenu(player).getInventory();

			if (clickedInventory != null && clickedInventory.equals(factoryInventory)) {
				event.setCancelled(true);
				MenuItem item = factory.getItem(player, slotClicked);
				if (item != null) {
					item.onClick(player);
				}
			} else if (event.isShiftClick() && event.getView().getTopInventory().equals(factoryInventory)) {
				event.setCancelled(true); // Stop items merging with the factory inventory
			}
		}
	}

}
