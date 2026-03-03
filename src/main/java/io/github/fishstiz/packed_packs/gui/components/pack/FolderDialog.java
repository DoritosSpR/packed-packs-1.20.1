package io.github.fishstiz.packed_packs.gui.components.pack;

import io.github.fishstiz.fidgetz.gui.components.*;
import io.github.fishstiz.fidgetz.gui.components.contextmenu.ContextMenuContainer;
import io.github.fishstiz.fidgetz.gui.components.contextmenu.ContextMenuItemBuilder;
import io.github.fishstiz.fidgetz.gui.renderables.sprites.Sprite;
import io.github.fishstiz.fidgetz.util.DrawUtil;
import io.github.fishstiz.packed_packs.gui.components.events.*;
import io.github.fishstiz.packed_packs.pack.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Nullable;

// Nota: Quitamos el genérico si causa problemas de bounds con la clase base
public class FolderDialog extends ToggleableDialog implements ContextMenuContainer {
    private final FolderPackList content;
    private final PackListEventListener listener;
    private FolderPack folderPack;
    private Sprite folderSprite = PackAssetManager.DEFAULT_FOLDER_ICON;
    
    private int x, y, width, height;
    private boolean visible = false;

    public <S extends Screen & ToggleableDialogContainer & PackListEventListener> FolderDialog(
            S screen, PackOptionsContext options, PackAssetManager assets, PackFileOperations fileOps
    ) {
        super(); // Constructor sin argumentos según el error de Gradle
        this.listener = screen;
        this.content = new FolderPackList(options, assets, fileOps, screen);
    }

    // Getters y Setters manuales para compensar lo que el compilador no encuentra en la clase base
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public void setWidth(int width) { this.width = width; }
    public void setHeight(int height) { this.height = height; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getWidth() { return width; }
    public int getHeight() { return height; }

    public boolean isVisible() { return this.visible; }
    public void setOpen(boolean open) { this.visible = open; }

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
        
        // Render fondo (Usa el DrawUtil de Fidgetz si está disponible)
        guiGraphics.fill(x, y, x + width, y + height, 0xCC000000); 
        
        this.content.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void buildItems(ContextMenuItemBuilder builder, int mouseX, int mouseY) {
        // Aquí iría la lógica del menú contextual (Rename, Delete, etc.)
    }

    @Override
    public void setFocused(@Nullable GuiEventListener listener) { }

    @Override
    public @Nullable GuiEventListener getFocused() {
        return this.content;
    }
}
