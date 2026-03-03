package io.github.fishstiz.packed_packs.gui.components.pack;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;

public class FolderDialog extends AbstractWidget {
    private final PackList parent;

    public FolderDialog(PackList parent, int x, int y, int width, int height, Component message) {
        super(x, y, width, height, message);
        this.parent = parent;
    }

    public boolean isOpen() {
        // En 1.20.1, 'visible' es el campo estándar de AbstractWidget
        return this.visible;
    }

    public void updatePosition() {
        // Usamos el método getY() que añadimos a PackList en el paso anterior
        this.setX(parent.getRowLeft()); 
        this.setY(parent.getY());
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (!this.visible) return;
        // Lógica de renderizado del diálogo
    }

    @Override
    protected void updateWidgetNarration(net.minecraft.client.gui.narration.NarrationElementOutput narrationElementOutput) {
        this.defaultButtonNarrationText(narrationElementOutput);
    }
}
