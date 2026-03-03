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
    private int width;
    private int height;
    public boolean visible = true;

    public FolderPackList(PackOptionsContext options, PackAssetManager assets, PackFileOperations fileOps, Screen screen) {
        // AJUSTE CLAVE: Usamos los enteros que PackList requiere (heredados de EntryListWidget)
        // minecraft, width, height, top, bottom, itemHeight
        super(Minecraft.getInstance(), screen.width, screen.height, 40, screen.height - 40, 36);
        
        // Guardamos las dimensiones iniciales
        this.width = screen.width;
        this.height = screen.height;
    }

    @Override public void setX(int x) { this.x = x; }
    @Override public void setY(int y) { this.y = y; }
    @Override public int getX() { return this.x; }
    @Override public int getY() { return this.y; }
    @Override public int getWidth() { return this.width; }
    @Override public int getHeight() { return this.height; }
    
    // Métodos adicionales que FolderDialog intenta llamar
    public void setWidth(int width) { this.width = width; }
    public void setHeight(int height) { this.height = height; }

    @Override
    public ScreenRectangle getRectangle() {
        return new ScreenRectangle(this.x, this.y, this.width, this.height);
    }

    public void visitWidgets(Consumer<AbstractWidget> consumer) { }
}
