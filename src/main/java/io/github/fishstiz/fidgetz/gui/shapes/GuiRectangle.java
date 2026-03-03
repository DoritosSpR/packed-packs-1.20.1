package io.github.fishstiz.fidgetz.gui.shapes;

import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.util.Mth;

public record GuiRectangle(int x, int y, int width, int height) {
    
    public static GuiRectangle viewOf(int x, int y, int width, int height) {
        return new GuiRectangle(x, y, width, height);
    }

    // Si usas el sistema de Layout de Minecraft 1.20.1
    public static GuiRectangle viewOf(net.minecraft.client.gui.layouts.LayoutElement element) {
        return new GuiRectangle(element.getX(), element.getY(), element.getWidth(), element.getHeight());
    }

    public boolean containsPoint(double mouseX, double mouseY) {
        return mouseX >= (double)this.x && mouseX < (double)(this.x + this.width) 
            && mouseY >= (double)this.y && mouseY < (double)(this.y + this.height);
    }

    public ScreenRectangle getScreenRectangle() {
        return new ScreenRectangle(x, y, width, height);
    }

    public int getRight() { return x + width; }
    public int getBottom() { return y + height; }
}
