package io.github.fishstiz.fidgetz.gui.components;

import io.github.fishstiz.fidgetz.gui.shapes.GuiRectangle;
import net.minecraft.client.gui.layouts.LayoutElement;

public abstract class AbstractLayoutElement implements LayoutElement {
    protected int x, y, width, height;

    @Override public int getX() { return x; }
    @Override public int getY() { return y; }
    @Override public int getWidth() { return width; }
    @Override public int getHeight() { return height; }
    @Override public void setX(int x) { this.x = x; }
    @Override public void setY(int y) { this.y = y; }

    public GuiRectangle getRectangle() {
        return new GuiRectangle(x, y, width, height);
    }
}
