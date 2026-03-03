package io.github.fishstiz.fidgetz.gui.components;

import io.github.fishstiz.fidgetz.gui.util.GuiRectangle;
import net.minecraft.client.gui.navigation.ScreenRectangle;

public interface Fidgetz {
    int getX();
    int getY();
    int getWidth();
    int getHeight();

    default ScreenRectangle getScreenRectangle(GuiRectangle rect) {
        return new ScreenRectangle(rect.x(), rect.y(), rect.width(), rect.height());
    }
}
