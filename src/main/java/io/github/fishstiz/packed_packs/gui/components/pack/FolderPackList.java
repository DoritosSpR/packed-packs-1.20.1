package io.github.fishstiz.packed_packs.gui.components.pack;

import io.github.fishstiz.fidgetz.gui.components.ToggleableDialog;
import io.github.fishstiz.packed_packs.pack.PackAssetManager;
import io.github.fishstiz.packed_packs.pack.PackFileOperations;
import io.github.fishstiz.packed_packs.pack.PackOptionsContext;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.screens.Screen;
import java.util.function.Consumer;

/**
 * Debe extender de ToggleableDialog o ser compatible con el genérico T de ToggleableDialog<T>.
 * En este caso, lo hacemos heredar de PackList y aseguramos que cumpla la jerarquía.
 */
public class FolderPackList extends PackList implements LayoutElement {
    private int x;
    private int y;
    public boolean visible = true;

    public FolderPackList(PackOptionsContext options, PackAssetManager assets, PackFileOperations fileOps, Screen screen) {
        // Valores por defecto para la lista: width, height, top, bottom, itemHeight
        super(screen.width, screen.height, 40, screen.height - 40, 36);
    }

    @Override
    public void setX(int x) {
        this.x = x;
        this.setLeft(x);
    }

    @Override
    public void setY(int y) {
        this.y = y;
        // En 1.20.1 las listas manejan su posición vertical internamente, 
        // pero para LayoutElement guardamos el valor.
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

    @Override
    public void visitWidgets(Consumer<AbstractWidget> consumer) {
        // Si la lista contiene widgets individuales (botones internos), se pasan aquí
    }
}
