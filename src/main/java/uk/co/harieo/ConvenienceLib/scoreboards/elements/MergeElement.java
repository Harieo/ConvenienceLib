package uk.co.harieo.ConvenienceLib.scoreboards.elements;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * A {@link RenderableElement} which merges other static and variable elements together as one final string. It is otherwise
 * difficult to merge these two types of elements together as variable elements need a player provided every time whereas
 * static elements do not.
 */
public class MergeElement implements RenderableElement {

    private final List<RenderableElement> elements = new ArrayList<>();

    public MergeElement append(RenderableElement element) {
        elements.add(element);
        return this;
    }

    public MergeElement append(String element) {
        return append(new ConstantElement(element));
    }

    public MergeElement space() {
        return append(new ConstantElement(" "));
    }

    @Override
    public String getText(Player player) {
        StringBuilder builder = new StringBuilder();
        for (RenderableElement element : elements) {
            builder.append(element.getText(player));
        }
        return builder.toString();
    }

}
