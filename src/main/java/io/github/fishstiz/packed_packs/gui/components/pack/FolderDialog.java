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
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public class FolderDialog extends ToggleableDialog<FolderDialog> implements ContextMenuContainer {
    private static final int SPACING = 4;
    private static final int HEADER_HEIGHT = 16;
    
    private final FolderPackList content;
    private final PackListEventListener listener;
    private int x, y, width, height;
    private boolean visible;
    private FolderPack folderPack;

    public <S extends Screen & ToggleableDialogContainer & PackListEventListener> FolderDialog(
            S screen, PackOptionsContext options, PackAssetManager assets, PackFileOperations fileOps
    ) {
        // El error dice que super() no debe llevar argumentos
        super();
        this.listener = screen;
        this.content = new FolderPackList(options, assets, fileOps, screen);
    }

    // Implementamos los getters/setters que faltan en la jerarquía
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public void setWidth(int width) { this.width = width; }
    public void setHeight(int height) { this.height = height; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }

    // Reemplazamos isVisible() / isOpen() según lo que Fidgetz espera
    public boolean isVisible() { return this.visible; }
    public void setVisible(boolean visible) { this.visible = visible; }

    public void updateFolder(PackList parent, FolderPack folderPack, PackAssetManager assets) {
        this.folderPack = folderPack;
        this.setX(parent.getX());
        this.setY(parent.getY());
        this.setWidth(parent.getWidth());
        this.setHeight(parent.getHeight());
        
        // Posicionar contenido interno
        this.content.setX(this.x + SPACING);
        this.content.setY(this.y + HEADER_HEIGHT + (SPACING * 2));
        this.content.setWidth(this.width - (SPACING * 2));
        this.content.setHeight(this.height - HEADER_HEIGHT - (SPACING * 3));
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (!this.isVisible()) return;
        
        // Renderizar el contenido
        this.content.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void buildItems(ContextMenuItemBuilder builder, int mouseX, int mouseY) {
        // Implementación del menú contextual
    }

    @Override
    public void setFocused(@Nullable GuiEventListener listener) {
        // Requerido por la interfaz de Minecraft
    }

    @Override
    public @Nullable GuiEventListener getFocused() {
        return this.content;
    }
}
