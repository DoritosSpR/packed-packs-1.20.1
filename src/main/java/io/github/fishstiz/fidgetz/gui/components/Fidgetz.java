package io.github.fishstiz.fidgetz.gui.components;

import net.minecraft.client.gui.navigation.ScreenRectangle;
import io.github.fishstiz.fidgetz.gui.util.GuiRectangle;

public class Fidgetz {
    
    /**
     * Convierte un GuiRectangle de Fidgetz a un ScreenRectangle de Minecraft.
     */
    public static ScreenRectangle getScreenRectangle(GuiRectangle rect) {
        // En 1.20.1 los métodos de GuiRectangle suelen ser left(), top(), width() y height()
        // o x(), y(), width(), height() dependiendo de tu implementación de Fidgetz.
        return new ScreenRectangle(rect.x(), rect.y(), rect.width(), rect.height());
    }
}
