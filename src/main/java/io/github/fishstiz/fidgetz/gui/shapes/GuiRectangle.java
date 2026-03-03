package io.github.fishstiz.fidgetz.gui.shapes;

import net.minecraft.util.Mth;

public class GuiRectangle {
    private int x, y, width, height;

    public int getRelativeX(float fraction) {
        // CORRECCIÓN: Mth.clamp es la versión de Minecraft para Java 17
        return this.x + Math.round(this.width * Mth.clamp(fraction, 0f, 1f));
    }

    public int getRelativeY(float fraction) {
        return this.y + Math.round(this.height * Mth.clamp(fraction, 0f, 1f));
    }
    
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }
}
