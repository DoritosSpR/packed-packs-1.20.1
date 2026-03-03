package io.github.fishstiz.packed_packs.gui.components.pack;

import io.github.fishstiz.packed_packs.pack.PackAssetManager;
import io.github.fishstiz.packed_packs.pack.PackFileOperations;
import io.github.fishstiz.packed_packs.pack.PackOptionsContext;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.navigation.ScreenRectangle;
import net.minecraft.client.gui.screens.Screen;
import java.util.function.Consumer;

// Implementamos LayoutElement para que ScreenEvent lo acepte
public class FolderPackList extends PackList implements LayoutElement {
    private int x;
    private int y;
    public boolean visible = true;

    public FolderPackList(PackOptionsContext options, PackAssetManager assets, PackFileOperations fileOps, Screen screen) {
        // AJUSTE: El error indica que el super(...) anterior falló. 
        // Usamos el constructor que PackList realmente tiene.
        super(options, assets, fileOps, screen);
    }

    // Métodos para satisfacer a FolderDialog y LayoutElement
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public int getX() { return this.x; }
    public int getY() { return this.y; }
    public int getWidth() { return this.width; }
    public int getHeight() { return this.height; }
    
    // Estos métodos evitan errores de "cannot find symbol" en FolderDialog
    public void setWidth(int width) { this.width = width; }
    public void setHeight(int height) { this.height = height; }

    @Override
    public ScreenRectangle getRectangle() {
        return new ScreenRectangle(x, y, getWidth(), getHeight());
    }

    public void visitWidgets(Consumer<AbstractWidget> consumer) {
        // Implementación vacía para cumplir el contrato si es necesario
    }
    
    public PackList.Entry getEntryAt(double mouseX, double mouseY) {
        // Si PackList no tiene getEntryAtPosition, esto fallará. 
        // Verifica si el método se llama de otra forma en tu versión.
        return super.getEntryAtPosition(mouseX, mouseY);
    }
}
