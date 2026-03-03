package io.github.fishstiz.fidgetz.gui.components;

import io.github.fishstiz.fidgetz.gui.shapes.GuiRectangle;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.navigation.ScreenRectangle;

public interface Fidgetz extends GuiEventListener, LayoutElement {
    // Esto resuelve el conflicto devolviendo el tipo que Minecraft espera,
    // pero usando los datos de nuestro GuiRectangle interno.
    @Override
    default ScreenRectangle getRectangle() {
        GuiRectangle rect = getViewRectangle();
        return new ScreenRectangle(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
    }

    // Este es nuestro método propio que las clases deben implementar
    GuiRectangle getViewRectangle();

    @Override
    default boolean isMouseOver(double mouseX, double mouseY) {
        return getViewRectangle().containsPoint(mouseX, mouseY);
    }
}
