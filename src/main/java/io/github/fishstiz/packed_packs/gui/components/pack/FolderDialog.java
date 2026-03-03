package io.github.fishstiz.packed_packs.gui.components.pack;

import io.github.fishstiz.fidgetz.gui.components.*;
import io.github.fishstiz.fidgetz.gui.components.contextmenu.ContextMenuContainer;
import io.github.fishstiz.fidgetz.gui.components.contextmenu.ContextMenuItemBuilder;
import io.github.fishstiz.packed_packs.gui.components.events.*;
import io.github.fishstiz.packed_packs.pack.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Nullable;
import java.util.Collections;
import java.util.List;

public class FolderDialog extends ToggleableDialog<FolderDialog> implements ContextMenuContainer {
    private final FolderPackList content;
    private final PackListEventListener listener;
    private int x, y, width, height;

    public <S extends Screen & ToggleableDialogContainer & PackListEventListener> FolderDialog(
            S screen, PackOptionsContext options, PackAssetManager assets, PackFileOperations fileOps
    ) {
        super();
        this.listener = screen;
        this.content = new FolderPackList(options, assets, fileOps, screen);
    }

    // --- REQUERIDO PARA 1.20.1 ---
    @Override
    public List<? extends GuiEventListener> children() {
        return Collections.singletonList(this.content);
    }

    @Override
    public boolean isDragging() { return false; }

    @Override
    public void setDragging(boolean dragging) { }
    // -----------------------------

    public boolean isVisible() { 
        return this.isActive(); // Cambiado de isOpen() a isActive() según error
    }

    public void updateFolder(PackList parent, FolderPack folderPack, PackAssetManager assets) {
        // En 1.20.1 los widgets suelen tener x, y públicos o setters específicos
        this.x = parent.getX();
        this.y = parent.getY();
        this.width = parent.getWidth();
        this.height = parent.getHeight();
        
        this.content.setX(this.x + 4);
        this.content.setY(this.y + 20);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (!isVisible()) return;
        guiGraphics.fill(x, y, x + width, y + height, 0xCC000000);
        this.content.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void buildItems(ContextMenuItemBuilder builder, int mouseX, int mouseY) {}

    @Override public void setFocused(@Nullable GuiEventListener listener) {}
    @Override public @Nullable GuiEventListener getFocused() { return this.content; }
}
