package io.github.fishstiz.fidgetz.gui.components;

import io.github.fishstiz.fidgetz.gui.shapes.GuiRectangle;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.LayoutElement;

public interface Fidgetz extends GuiEventListener, LayoutElement {
    // Cambiamos el nombre a getViewRectangle para evitar el choque con Minecraft 1.20.1
    GuiRectangle getViewRectangle();

    default boolean isMouseOver(double mouseX, double mouseY) {
        return getViewRectangle().containsPoint(mouseX, mouseY);
    }
}
