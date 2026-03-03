package io.github.fishstiz.packed_packs.gui.components;

import java.util.List;
import java.util.Objects;

public record SelectionContext<T>(List<T> selection, T item) {
    public boolean isSelectedLast() {
        if (this.selection.isEmpty()) return false;
        // CORRECCIÓN: Java 17 no tiene getLast()
        return Objects.equals(this.selection.get(this.selection.size() - 1), this.item);
    }
}
