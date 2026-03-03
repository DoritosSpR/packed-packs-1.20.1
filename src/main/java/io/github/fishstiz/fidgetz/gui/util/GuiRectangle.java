package io.github.fishstiz.fidgetz.gui.util;

public record GuiRectangle(int x, int y, int width, int height) {
    public int left() { return x; }
    public int top() { return y; }
    public int right() { return x + width; }
    public int bottom() { return y + height; }
}
