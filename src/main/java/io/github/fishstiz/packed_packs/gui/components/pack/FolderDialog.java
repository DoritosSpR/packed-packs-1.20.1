package io.github.fishstiz.packed_packs.gui.components.pack;

import io.github.fishstiz.fidgetz.gui.components.*;
import io.github.fishstiz.fidgetz.gui.components.contextmenu.ContextMenuContainer;
import io.github.fishstiz.fidgetz.gui.components.contextmenu.ContextMenuItemBuilder;
import io.github.fishstiz.fidgetz.gui.renderables.sprites.Sprite;
import io.github.fishstiz.packed_packs.gui.components.events.*;
import io.github.fishstiz.packed_packs.pack.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Nullable;

public class FolderDialog extends ToggleableDialog<FolderDialog> implements ContextMenuContainer {
    private final FolderPackList content;
    private final PackListEventListener listener;
    private FolderPack folderPack;
    private Sprite folderSprite = PackAssetManager.DEFAULT_FOLDER_ICON;
    
    private int x, y, width, height;
    private boolean isDragging;

    public <S extends Screen & ToggleableDialogContainer & PackListEventListener> FolderDialog(
            S screen, PackOptionsContext options, PackAssetManager assets, PackFileOperations fileOps
    ) {
        super(); 
        this.listener = screen;
        this.content = new FolderPackList(options, assets, fileOps, screen);
    }

    // Cumplir contrato de ContainerEventHandler (Requerido en 1.20.1)
    @Override
    public boolean isDragging() { return this.isDragging; }

    @Override
    public void setDragging(boolean dragging) { this.isDragging = dragging; }

    // Getters y Setters de posición
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public void setWidth(int width) { this.width = width; }
    public void setHeight(int height) { this.height = height; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }

    // Compatibilidad con ToggleableDialog
    public boolean isVisible() { return this.isOpen(); }

    public void updateFolder(PackList parent, FolderPack folderPack, PackAssetManager assets) {
        this.folderPack = folderPack;
        this.setX(parent.getX());
        this.setY(parent.getY());
        this.setWidth(parent.getWidth());
        this.setHeight(parent.getHeight());
        
        assets.getOrLoadIcon(folderPack, icon -> this.folderSprite = icon);
        
        // Ajustar la lista interna
        this.content.setX(this.x + 4);
        this.content.setY(this.y + 20);
        this.content.setWidth(this.width - 8);
        this.content.setHeight(this.height - 24);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (!isVisible()) return;
        
        // Render fondo semi-transparente
        guiGraphics.fill(x, y, x + width, y + height, 0xCC000000); 
        
        this.content.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void buildItems(ContextMenuItemBuilder builder, int mouseX, int mouseY) {
        // Lógica de menú contextual
    }

    @Override
    public void setFocused(@Nullable GuiEventListener listener) {
        this.content.setFocused(listener);
    }

    @Override
    public @Nullable GuiEventListener getFocused() {
        return this.content.getFocused();
    }
}
