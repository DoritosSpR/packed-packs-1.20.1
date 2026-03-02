package io.github.fishstiz.packed_packs.gui.screens;

import io.github.fishstiz.fidgetz.gui.components.ToggleableDialogContainer;
import io.github.fishstiz.packed_packs.gui.components.events.PackListEventListener;
import io.github.fishstiz.packed_packs.gui.components.pack.*;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.client.gui.components.events.GuiEventListener;

public abstract class PackListScreen<S extends Screen & ToggleableDialogContainer & PackListEventListener> extends Screen {
    protected final S parent;
    
    protected PackList availablePackList;
    protected PackList selectedPackList;
    
    // Cambiamos las clases específicas que faltan por GuiEventListener temporalmente
    // o las comentamos si no se están usando todavía.
    protected GuiEventListener profiles; 
    protected GuiEventListener aliasModal;
    protected PackListDevMenu contextMenu;
    protected FolderDialog folderDialog;
    protected FileRenameModal fileRenameModal;

    protected PackListScreen(S parent) {
        super(Component.literal("Packed Packs"));
        this.parent = parent;
    }

    public GuiEventListener getSidebar() { return profiles; }

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.parent);
    }
}
