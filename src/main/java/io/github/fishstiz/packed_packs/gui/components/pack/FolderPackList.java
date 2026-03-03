package io.github.fishstiz.packed_packs.gui.components.pack;

import io.github.fishstiz.packed_packs.pack.PackAssetManager;
import io.github.fishstiz.packed_packs.pack.PackFileOperations;
import io.github.fishstiz.packed_packs.pack.PackOptionsContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.Screen;
import java.util.function.Consumer;

public class FolderPackList extends PackList implements LayoutElement {
    private int x;
    private int y;
    public boolean visible = true;

    public FolderPackList(PackOptionsContext options, PackAssetManager assets, PackFileOperations fileOps, Screen screen) {
        // En 1.20.1, EntryListWidget usa: minecraft, width, height, top, bottom, itemHeight
        super(Minecraft.getInstance(), screen.width, screen.height, 40, screen.height - 40, 36);
        
        // Si PackList requiere parámetros extra (como context/assets), 
        // asegúrate de que el constructor de PackList los reciba.
    }

    @Override
    public void setX(int x) {
        this.x = x;
        // En Minecraft 1.20.1, para EntryListWidget, el x se define 
        // a través de updateSize o métodos de posicionamiento de la lista.
        this.setLeftPos(x); 
    }

    @Override
    public void setY(int y) {
        this.y = y;
        // Ajustamos los límites de renderizado vertical de la lista
        this.updateSize(this.width, this.height, y, y + this.height);
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getY() {
        return this.y;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    /**
     * Requerido por LayoutElement en 1.20.1
     */
    @Override
    public ScreenRectangle getRectangle() {
        return new ScreenRectangle(this.x, this.y, this.width, this.height);
    }

    /**
     * Fidgetz suele usar este método para registrar el contenido en el sistema de eventos
     */
    public void visitWidgets(Consumer<AbstractWidget> consumer) {
        // Las listas no suelen ser AbstractWidgets, pero si tienen botones 
        // flotantes o barras de búsqueda, se registrarían aquí.
    }
    
    // Método de utilidad para encontrar entradas bajo el ratón (usado en FolderDialog)
    public PackList.Entry getEntryAt(double mouseX, double mouseY) {
        return this.getEntryAtPosition(mouseX, mouseY);
    }
}
