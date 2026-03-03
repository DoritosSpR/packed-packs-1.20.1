package io.github.fishstiz.fidgetz.gui.components;

import io.github.fishstiz.fidgetz.gui.shapes.GuiRectangle;

public abstract class AbstractLayoutElement implements Fidgetz {
    protected int x, y, width, height;

    @Override public int getX() { return x; }
    @Override public int getY() { return y; }
    @Override public int getWidth() { return width; }
    @Override public int getHeight() { return height; }
    @Override public void setX(int x) { this.x = x; }
    @Override public void setY(int y) { this.y = y; }

    @Override
    public GuiRectangle getViewRectangle() {
        return new GuiRectangle(x, y, width, height);
    }
}
