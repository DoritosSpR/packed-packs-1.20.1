package io.github.fishstiz.fidgetz.gui.shapes;

public record GuiRectangle(int x, int y, int width, int height) {
    public boolean contains(double mouseX, double mouseY) {
        return mouseX >= (double)this.x && mouseX < (double)(this.x + this.width) && mouseY >= (double)this.y && mouseY < (double)(this.y + this.height);
    }

    public int right() { return x + width; }
    public int bottom() { return y + height; }
}
