package io.github.fishstiz.packed_packs.gui.components;

import java.util.List;
import java.util.Objects;

public record SelectionContext<T>(List<T> selection, T item) {
    public boolean isSelectedLast() {
        if (this.selection == null || this.selection.isEmpty()) return false;
        return Objects.equals(this.selection.get(this.selection.size() - 1), this.item);
    }

    public boolean isSelected() {
        return this.selection != null && this.selection.contains(this.item);
    }
}
