package io.github.fishstiz.packed_packs.gui.screens;

import io.github.fishstiz.fidgetz.gui.components.ToggleableDialogContainer;
import io.github.fishstiz.packed_packs.gui.components.events.PackListEvent;
import io.github.fishstiz.packed_packs.gui.components.events.PackListEventListener;
import io.github.fishstiz.packed_packs.gui.components.pack.*;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public abstract class PackListScreen<S extends Screen & ToggleableDialogContainer & PackListEventListener> extends Screen {
    protected final S parent;
    
    // Componentes de la interfaz
    protected PackList availablePackList;
    protected PackList selectedPackList;
    protected EditBox searchBox;
    protected FolderDialog folderDialog;
    protected FileRenameModal fileRenameModal;
    protected PackListDevMenu contextMenu;
    protected PackProfileSidebar profiles;
    protected PackAliasModal aliasModal; // Puede ser nulo si no se usa

    protected PackListScreen(S parent) {
        super(Component.translatable("packed_packs.screen.title"));
        this.parent = parent;
    }

    @Override
    protected void init() {
        // Aquí inicializarías tus listas y componentes. 
        // Por ahora definimos los stubs para que PackListEventHandler compile.
        this.setupComponents();
    }

    private void setupComponents() {
        // Inicialización básica de objetos para evitar NullPointerException
        // En tu código real, aquí instanciarías cada componente con sus coordenadas
    }

    public void onEvent(PackListEvent event) {
        // Lógica base de eventos (si la hay)
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    // Getters para que el EventHandler pueda acceder a los componentes
    public PackList getAvailablePackList() { return availablePackList; }
    public PackList getSelectedPackList() { return selectedPackList; }
    public FolderDialog getFolderDialog() { return folderDialog; }
    public FileRenameModal getFileRenameModal() { return fileRenameModal; }
    public PackProfileSidebar getSidebar() { return profiles; }

    @Override
    public void onClose() {
        this.minecraft.setScreen(this.parent);
    }
}
