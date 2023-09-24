package net.harieo.ConvenienceLib.spigot.scoreboards.elements;

import org.bukkit.entity.Player;

public class ConstantElement implements RenderableElement {

	private final String text;

	/**
	 * An implementation of {@link RenderableElement} that displays text which does not change on update
	 *
	 * @param text to be displayed
	 */
	public ConstantElement(String text) {
		this.text = text;
	}

	/**
	 * @param player that this element is being rendered for
	 * @return the text that is to be displayed by this element
	 */
	@Override
	public String getText(Player player) {
		return text;
	}

}
