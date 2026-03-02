package io.github.fishstiz.packed_packs.gui.components.pack;

import io.github.fishstiz.packed_packs.gui.components.SelectableList;
import io.github.fishstiz.packed_packs.pack.PackOptionsContext;
import net.minecraft.server.packs.repository.Pack;
import org.jetbrains.annotations.NotNull;
import java.util.*;

public class PackListModel extends SelectableList<Pack> {
    private final PackOptionsContext options;
    private Query query = new Query("", Query.SortOption.NONE, false);

    public PackListModel(PackOptionsContext options) {
        super();
        this.options = options;
    }

    @Override
    protected boolean filter(Pack pack) {
        if (this.query.hideIncompatible() && !pack.getCompatibility().isCompatible()) return false;

        String searchTerm = this.query.search().toLowerCase(Locale.ROOT);
        if (!searchTerm.isEmpty()) {
            // CORRECCIÓN 1.20.1: Usar getTitle()
            String title = pack.getTitle().getString().toLowerCase(Locale.ROOT);
            String description = pack.getDescription().getString().toLowerCase(Locale.ROOT);
            return title.contains(searchTerm) || description.contains(searchTerm) || pack.getId().contains(searchTerm);
        }
        return true;
    }

    // --- Métodos de Movimiento requeridos por PackList.java ---
    public boolean canMoveUp(Pack pack) {
        int i = this.items.indexOf(pack);
        return i > 0;
    }

    public boolean canMoveDown(Pack pack) {
        int i = this.items.indexOf(pack);
        return i >= 0 && i < this.items.size() - 1;
    }

    public boolean moveUp(Pack pack) {
        if (!canMoveUp(pack)) return false;
        int i = this.items.indexOf(pack);
        Collections.swap(this.items, i, i - 1);
        return true;
    }

    public boolean moveDown(Pack pack) {
        if (!canMoveDown(pack)) return false;
        int i = this.items.indexOf(pack);
        Collections.swap(this.items, i, i + 1);
        return true;
    }

    public boolean isQueried() {
        return !this.query.search().isEmpty() || this.query.hideIncompatible();
    }

    public boolean hideIncompatible(boolean hide) {
        this.query = this.query.withHideIncompatible(hide);
        return true;
    }

    public Snapshot captureState() {
        return new Snapshot(new ArrayList<>(this.items), new ArrayList<>(this.selectedItems));
    }

    // Records internos
    public record Snapshot(List<Pack> items, List<Pack> selected) {}
    public record Query(String search, SortOption sort, boolean hideIncompatible) {
        public Query withSearch(String s) { return new Query(s, sort, hideIncompatible); }
        public Query withSort(SortOption s) { return new Query(search, s, hideIncompatible); }
        public Query withHideIncompatible(boolean h) { return new Query(search, sort, h); }
        public enum SortOption { NONE, NAME, ID, COMPATIBILITY }
    }
}
