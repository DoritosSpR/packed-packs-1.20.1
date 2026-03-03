package io.github.fishstiz.packed_packs.gui.components;

import java.util.List;
import java.util.Objects;

public record SelectionContext<T>(List<T> selection, T item) {
    public boolean isSelectedLast() {
        if (this.selection == null || this.selection.isEmpty()) {
            return false;
        }
        // CORRECCIÓN: Uso de size() - 1 para compatibilidad con Java 17
        T lastElement = this.selection.get(this.selection.size() - 1);
        return Objects.equals(lastElement, this.item);
    }
}
