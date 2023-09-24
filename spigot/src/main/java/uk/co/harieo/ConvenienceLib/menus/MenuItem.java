package uk.co.harieo.ConvenienceLib.menus;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import uk.co.harieo.ConvenienceLib.menus.click.ClickAction;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * An item which holds data to be converted into {@link ItemStack} for interacting with an {@link org.bukkit.inventory.Inventory}
 *
 * @author Harieo
 */
@Deprecated
public class MenuItem {

    private final ItemStack item;
    private Consumer<ClickAction> onClick;

    /**
     * Creates an {@link ItemStack} with {@link Material}, amount and byte damage
     *
     * @param material to set for the item
     * @param amount   of the item
     * @param damage   to set for the item
     * @deprecated because use of byte data is deprecated in this version of Spigot
     */
    @Deprecated
    public MenuItem(Material material, int amount, byte damage) {
        this(new ItemStack(material, amount, damage));
    }

    /**
     * Creates an {@link ItemStack} with the {@link Material} and amount
     *
     * @param material to set for the item
     * @param amount   of the item
     */
    public MenuItem(Material material, int amount) {
        this(new ItemStack(material, amount));
    }

    /**
     * Creates an {@link ItemStack} with just the {@link Material}
     *
     * @param material to set for the item
     */
    public MenuItem(Material material) {
        this(new ItemStack(material));
    }

    /**
     * Sets the {@link ItemStack} directly, assuming values have already been inserted into it
     *
     * @param item to set
     */
    public MenuItem(ItemStack item) {
        this.item = item;
    }

    /**
     * Sets the item name
     *
     * @param name to set
     */
    public MenuItem setName(String name) {
        metaUpdate(meta -> meta.setDisplayName(name));
        return this;
    }

    /**
     * @return the name of this item
     */
    public String getName() {
        return Objects.requireNonNull(item.getItemMeta()).getDisplayName();
    }

    /**
     * Sets the lore
     *
     * @param lore to set
     */
    public MenuItem setLore(List<String> lore) {
        metaUpdate(meta -> meta.setLore(lore));
        return this;
    }

    /**
     * @return the lore of this item
     */
    public List<String> getLore() {
        return Objects.requireNonNull(item.getItemMeta()).getLore();
    }

    /**
     * Updates and sets the Item Meta for the item
     *
     * @param toEdit edits to the meta before re-set
     */
    private void metaUpdate(Consumer<ItemMeta> toEdit) {
        ItemMeta meta = item.getItemMeta();
        toEdit.accept(meta);
        item.setItemMeta(meta);
    }

    /**
     * Sets a Consumer to be called when this item is clicked by a Player
     *
     * @param onClick for when the item is clicked
     * @deprecated replaced with {@link #setClickAction(Consumer)} which uses the data-rich {@link ClickAction} object
     */
    @Deprecated
    public MenuItem setOnClick(Consumer<Player> onClick) {
        setClickAction(action -> onClick.accept(action.getClickingPlayer()));
        return this;
    }

    /**
     * Sets a Consumer which takes action when a player commits a {@link ClickAction} on this menu item
     *
     * @param onClick the procedure to run on click
     */
    public MenuItem setClickAction(Consumer<ClickAction> onClick) {
        this.onClick = onClick;
        return this;
    }

    /**
     * Calls the onClick Consumer for this item, if one has been set. Note: This should only be used with {@link MenuInteractionListener}
     * or a custom implementation which detects interactions with {@link MenuItem}.
     *
     * @param clickAction the click action committed against this item
     */
    public void onClick(ClickAction clickAction) {
        if (onClick != null) {
            onClick.accept(clickAction);
        }
    }

    /**
     * @return the Bukkit item which represents this instance
     */
    public ItemStack getItem() {
        return item;
    }

}
