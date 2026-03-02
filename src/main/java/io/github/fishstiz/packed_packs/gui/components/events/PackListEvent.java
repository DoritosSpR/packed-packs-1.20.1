package io.github.fishstiz.packed_packs.gui.components.events;

import io.github.fishstiz.packed_packs.gui.components.pack.PackList;
import net.minecraft.server.packs.repository.Pack;

public sealed interface PackListEvent permits
        BasicEvent,
        DragEvent,
        DropEvent,
        FileEvent,
        MoveEvent,
        RequestTransferEvent,
        SelectionEvent,
        PackAliasOpenEvent {
    
    PackList target();

    boolean pushToHistory();

    default String name() {
        return this.getClass().getSimpleName();
    }

    // --- Implementaciones Requeridas ---

    record BasicEvent(PackList target, boolean pushToHistory) implements PackListEvent {}

    record SelectionEvent(PackList target) implements PackListEvent {
        @Override public boolean pushToHistory() { return false; }
    }

    record DragEvent(PackList target) implements PackListEvent {
        @Override public boolean pushToHistory() { return false; }
    }

    record DropEvent(PackList target) implements PackListEvent {
        @Override public boolean pushToHistory() { return true; }
    }

    record MoveEvent(PackList target) implements PackListEvent {
        @Override public boolean pushToHistory() { return true; }
    }

    record RequestTransferEvent(PackList target) implements PackListEvent {
        @Override public boolean pushToHistory() { return false; }
    }

    record PackAliasOpenEvent(PackList target, Object trigger) implements PackListEvent {
        @Override public boolean pushToHistory() { return false; }
    }

    // --- Jerarquía de Archivos (FileEvent) ---
    
    sealed interface FileEvent extends PackListEvent permits 
        FileDeleteEvent, FileRenameOpenEvent, FileRenameEvent, FileRenameCloseEvent,
        FolderOpenEvent, FolderCloseEvent {
    }

    record FileDeleteEvent(PackList target) implements FileEvent {
        @Override public boolean pushToHistory() { return true; }
    }

    record FileRenameOpenEvent(PackList target, Object trigger) implements FileEvent {
        @Override public boolean pushToHistory() { return false; }
    }

    record FileRenameEvent(PackList target, String oldName, String newName) implements FileEvent {
        @Override public boolean pushToHistory() { return true; }
    }

    record FileRenameCloseEvent(PackList target) implements FileEvent {
        @Override public boolean pushToHistory() { return false; }
    }

    record FolderOpenEvent(PackList target, Object trigger) implements FileEvent {
        @Override public boolean pushToHistory() { return false; }
    }

    record FolderCloseEvent(PackList target) implements FileEvent {
        @Override public boolean pushToHistory() { return false; }
    }
}
