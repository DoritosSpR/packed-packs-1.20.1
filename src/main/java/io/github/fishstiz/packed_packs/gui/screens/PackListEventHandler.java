package io.github.fishstiz.packed_packs.gui.screens;

import io.github.fishstiz.fidgetz.gui.components.ToggleableDialogContainer;
import io.github.fishstiz.packed_packs.gui.components.events.PackListEvent;
import io.github.fishstiz.packed_packs.gui.components.events.PackListEventListener;
import io.github.fishstiz.packed_packs.gui.components.pack.PackList;
import io.github.fishstiz.packed_packs.gui.screens.history.PackStateHistory;
import net.minecraft.client.gui.screens.Screen;

// Importaciones estáticas corregidas para PackListEvent
import static io.github.fishstiz.packed_packs.gui.components.events.PackListEvent.*;

public abstract class PackListEventHandler<S extends Screen & ToggleableDialogContainer & PackListEventListener> extends PackListScreen<S> implements PackListEventListener {
    protected final PackStateHistory history = new PackStateHistory();

    public PackListEventHandler(S screen) {
        super(screen);
    }

    @Override
    public void onEvent(PackListEvent event) {
        // Lógica de reseteo de UI
        this.profiles.getSidebar().setOpen(false);
        this.contextMenu.setOpen(false);
        this.fileRenameModal.setOpen(false);
        if (this.aliasModal != null) this.aliasModal.closeModal();

        boolean notFolderDialogEvent = event.target() != this.folderDialog.root();
        if (notFolderDialogEvent) this.folderDialog.setOpen(false);

        // JAVA 17: Pattern Matching simplificado
        if (event instanceof FileDeleteEvent) {
            this.revalidatePacks();
        } else if (event instanceof FileRenameOpenEvent e) {
            this.fileRenameModal.open(e.target(), e.trigger());
        } else if (event instanceof FileRenameEvent e) {
            this.onFileRename(e);
        } else if (event instanceof FileRenameCloseEvent e) {
            this.focusList(e.target());
        } else if (event instanceof FolderOpenEvent e) {
            this.onFolderOpen(e);
        } else if (event instanceof FolderCloseEvent e) {
            this.onFolderClose(e);
        } else if (event instanceof PackAliasOpenEvent e) {
            this.onOpenAliases(e);
        }

        if (this.isUnlocked() && event.pushToHistory() && notFolderDialogEvent) {
            this.history.push(this.captureState());
        }
    }

    @Override
    public void onSelection(SelectionEvent event) {
        if (event != null) {
            this.unfocusOtherLists(event.target());
        }
    }

    protected void onFileRename(FileRenameEvent event) {
        this.revalidatePacks();
    }

    protected void onFolderOpen(FolderOpenEvent event) {
        this.folderDialog.open(event.target(), event.trigger());
    }

    protected void onFolderClose(FolderCloseEvent event) {
        this.focusList(event.target());
    }

    protected void onOpenAliases(PackAliasOpenEvent event) {
        if (this.aliasModal != null) {
            this.aliasModal.open(event.target(), event.trigger());
        }
    }

    protected abstract void revalidatePacks();
    protected abstract void focusList(PackList packList);
    protected abstract void unfocusOtherLists(PackList packList);
    protected abstract PackStateHistory.State captureState();
    protected abstract boolean isUnlocked();
}
