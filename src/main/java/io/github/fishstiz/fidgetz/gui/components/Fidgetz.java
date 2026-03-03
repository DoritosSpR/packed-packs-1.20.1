package io.github.fishstiz.fidgetz.gui.components;

import io.github.fishstiz.fidgetz.gui.shapes.GuiRectangle;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.navigation.ScreenRectangle;

public interface Fidgetz extends GuiEventListener, LayoutElement {
    
    @Override
    default ScreenRectangle getRectangle() {
        GuiRectangle rect = getViewRectangle();
        return new ScreenRectangle(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
    }

    GuiRectangle getViewRectangle();

    @Override
    default boolean isMouseOver(double mouseX, double mouseY) {
        return getViewRectangle().containsPoint(mouseX, mouseY);
    }

    @Override int getX();
    @Override int getY();
    @Override int getWidth();
    @Override int getHeight();
}
