package io.github.fishstiz.packed_packs.gui.components.pack;

import net.minecraft.client.gui.layouts.LayoutElement;
import java.util.function.Consumer;
import net.minecraft.client.gui.components.AbstractWidget;

public class FolderPackList extends PackList implements LayoutElement {
    public boolean visible = true;

    // Ajustamos el constructor para recibir lo que FolderDialog intenta pasarle
    public FolderPackList(Object options, Object assets, Object fileOps, Object screen) {
        super(200, 200, 40, 200, 36); 
    }

    @Override public void setX(int x) { this.setLeft(x); }
    @Override public void setY(int y) { /* En 1.20.1 esto se maneja distinto en listas */ }
    @Override public int getX() { return this.getRowLeft(); }
    @Override public int getY() { return 0; } // Ajustar según necesidad
    @Override public int getWidth() { return this.width; }
    @Override public int getHeight() { return this.height; }
    @Override public void visitWidgets(Consumer<AbstractWidget> consumer) {}
}
