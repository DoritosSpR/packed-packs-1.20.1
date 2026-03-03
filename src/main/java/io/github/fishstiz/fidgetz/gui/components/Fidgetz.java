package io.github.fishstiz.fidgetz.gui.components;

import net.minecraft.client.gui.navigation.ScreenRectangle;
import io.github.fishstiz.fidgetz.gui.util.GuiRectangle;

public interface Fidgetz {
    default ScreenRectangle getScreenRectangle(GuiRectangle rect) {
        return new ScreenRectangle(rect.x(), rect.y(), rect.width(), rect.height());
    }

    int getX();
    int getY();
    int getWidth();
    int getHeight();
}
