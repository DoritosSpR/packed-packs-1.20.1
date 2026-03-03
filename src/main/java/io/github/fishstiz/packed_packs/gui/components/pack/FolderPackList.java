package io.github.fishstiz.packed_packs.gui.components.pack;

import net.minecraft.client.gui.layouts.LayoutElement;
import java.util.function.Consumer;
import net.minecraft.client.gui.components.AbstractWidget;

// Añadimos LayoutElement para que sea compatible con ToggleableDialog
public class FolderPackList extends PackList implements LayoutElement {
    public boolean visible = true;

    // Implementación manual de LayoutElement para 1.20.1
    @Override public void setX(int x) { this.left = x; }
    @Override public void setY(int y) { this.top = y; }
    @Override public int getX() { return this.left; }
    @Override public int getY() { return this.top; }
    @Override public int getWidth() { return this.width; }
    @Override public int getHeight() { return this.height; }
    @Override public void visitWidgets(Consumer<AbstractWidget> consumer) {
        // Si la lista tiene widgets internos (botones), pásalos aquí
    }
}
