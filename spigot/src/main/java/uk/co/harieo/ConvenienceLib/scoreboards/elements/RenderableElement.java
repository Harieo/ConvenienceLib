package uk.co.harieo.ConvenienceLib.scoreboards.elements;

import org.bukkit.entity.Player;

@Deprecated
public interface RenderableElement {

	/**
	 * @param player that this element is being rendered for
	 * @return the text this element will display to the player
	 */
	String getText(Player player);

}
