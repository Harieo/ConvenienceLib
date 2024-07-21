package uk.co.harieo.ConvenienceLib.menus.click;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;

/**
 * Represents a click action taken by a {@link Player} through an {@link InventoryClickEvent}
 */
@Deprecated
public class ClickAction {

    private final Player clickingPlayer;
    private final ClickType clickType;
    private final InventoryAction inventoryAction;

    /**
     * Takes the {@link Player} who clicked and the {@link ClickType} used
     *
     * @param clickingPlayer  the player which clicked in the inventory
     * @param clickType       the type of click the player used
     * @param inventoryAction the action taken in the inventory
     */
    public ClickAction(Player clickingPlayer, ClickType clickType, InventoryAction inventoryAction) {
        this.clickingPlayer = clickingPlayer;
        this.clickType = clickType;
        this.inventoryAction = inventoryAction;
    }

    /**
     * Takes an {@link InventoryClickEvent} and retrieves the data required to construct this class,
     * calling {@link ClickAction#ClickAction(Player, ClickType, InventoryAction)}
     *
     * @param clickEvent the event to pull click action data from
     */
    public ClickAction(InventoryClickEvent clickEvent) {
        this((Player) clickEvent.getWhoClicked(), clickEvent.getClick(), clickEvent.getAction());
    }

    /**
     * @return the player who clicked in the inventory
     */
    public Player getClickingPlayer() {
        return clickingPlayer;
    }

    /**
     * @return the type of click that the player used
     */
    public ClickType getClickType() {
        return clickType;
    }

    /**
     * @return the inventory action which the player committed in the inventory
     */
    public InventoryAction getInventoryAction() {
        return inventoryAction;
    }

}
