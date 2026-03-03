package io.github.fishstiz.fidgetz.gui.components;

import io.github.fishstiz.fidgetz.gui.shapes.GuiRectangle;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.LayoutElement;

public interface Fidgetz extends GuiEventListener, LayoutElement {
    // En lugar de heredar, definimos el método para obtener el rectángulo
    GuiRectangle getRectangle();

    default boolean isMouseOver(double mouseX, double mouseY) {
        return getRectangle().containsPoint(mouseX, mouseY);
    }
}
