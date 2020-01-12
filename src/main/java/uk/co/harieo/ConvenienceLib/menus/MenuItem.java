package uk.co.harieo.ConvenienceLib.menus;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

/**
 * An item which holds data to be converted into {@link ItemStack} for interacting with an {@link org.bukkit.inventory.Inventory}
 *
 * @author Harieo
 */
public class MenuItem {

	private ItemStack item;
	private Consumer<Player> onClick;

	/**
	 * Creates an {@link ItemStack} with {@link Material}, amount and byte damage
	 *
	 * @param material to set for the item
	 * @param amount of the item
	 * @param damage to set for the item
	 */
	public MenuItem(Material material, int amount, byte damage) {
		this(new ItemStack(material, amount, damage));
	}

	/**
	 * Creates an {@link ItemStack} with the {@link Material} and amount
	 *
	 * @param material to set for the item
	 * @param amount of the item
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
	 * Sets a Consumer to be called when this item is clicked by a Player
	 *
	 * @param onClick for when the item is clicked
	 */
	public void setOnClick(Consumer<Player> onClick) {
		this.onClick = onClick;
	}

	/**
	 * Calls the onClick Consumer for this item, if one has been set
	 *
	 * @param player which has clicked the item
	 */
	public void onClick(Player player) {
		if (onClick != null) {
			onClick.accept(player);
		}
	}

	/**
	 * @return the Bukkit item which represents this instance
	 */
	public ItemStack getItem() {
		return item;
	}

}
