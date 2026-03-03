package io.github.fishstiz.packed_packs.gui.components.pack;

import net.minecraft.client.gui.layouts.LayoutElement;
import java.util.function.Consumer;
import net.minecraft.client.gui.components.AbstractWidget;

public class FolderPackList extends PackList implements LayoutElement {
    // Agregamos el constructor que falta
    public FolderPackList(Object options, Object assets, Object fileOps, Object screen) {
        super(200, 200, 0, 200, 36); // Valores por defecto
    }

    @Override public void setX(int x) { this.left = x; }
    @Override public void setY(int y) { this.top = y; }
    @Override public int getX() { return this.left; }
    @Override public int getY() { return this.top; }
    @Override public int getWidth() { return this.width; }
    @Override public int getHeight() { return this.height; }
    @Override public void visitWidgets(Consumer<AbstractWidget> consumer) {}
}
