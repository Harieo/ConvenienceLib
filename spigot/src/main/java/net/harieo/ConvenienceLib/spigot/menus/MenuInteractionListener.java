package net.harieo.ConvenienceLib.spigot.menus;

import net.harieo.ConvenienceLib.spigot.menus.click.ClickAction;
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
        if (event.getWhoClicked() instanceof Player player) {
            int slotClicked = event.getSlot();

            Inventory clickedInventory = event.getClickedInventory();
            if (clickedInventory == null)
                return;
            MenuImpl factoryMenuImpl = factory.getMenu(player);
            if (factoryMenuImpl == null) // They may be clicking a menu from another factory
                return;
            Inventory factoryInventory = factoryMenuImpl.getInventory();

            if (clickedInventory.equals(factoryInventory)) {
                event.setCancelled(true);
                MenuItem item = factory.getItem(player, slotClicked);
                if (item != null) {
                    ClickAction clickAction = new ClickAction(event);
                    item.onClick(clickAction);
                }
            } else if (event.isShiftClick() && event.getView().getTopInventory().equals(factoryInventory)) {
                event.setCancelled(true); // Stop items merging with the factory inventory
            }
        }
    }

}
