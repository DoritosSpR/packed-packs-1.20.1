package io.github.fishstiz.packed_packs.gui.components.pack;

import io.github.fishstiz.packed_packs.gui.components.SelectableList;
import io.github.fishstiz.packed_packs.pack.PackOptionsContext;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.util.StringUtil; // Usamos el de Minecraft 1.20.1
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class PackListModel extends SelectableList<Pack> {
    private final PackOptionsContext options;
    private Query query = new Query("", Query.SortOption.NONE, false);

    public PackListModel(PackOptionsContext options) {
        super();
        this.options = options;
    }

    @Override
    protected boolean filter(Pack pack) {
        if (this.query.hideIncompatible() && !pack.getCompatibility().isCompatible()) {
            return false;
        }

        String searchTerm = this.query.search().toLowerCase(Locale.ROOT);
        if (!searchTerm.isEmpty()) {
            // En 1.20.1 usamos getFriendlyName() y getDescription()
            String title = pack.getFriendlyName().getString().toLowerCase(Locale.ROOT);
            String description = pack.getDescription().getString().toLowerCase(Locale.ROOT);
            String id = pack.getId().toLowerCase(Locale.ROOT);
            
            return title.contains(searchTerm) || description.contains(searchTerm) || id.contains(searchTerm);
        }
        return true;
    }

    public void applySort() {
        Comparator<Pack> comparator = switch (this.query.sort()) {
            case NAME -> Comparator.comparing(p -> p.getFriendlyName().getString().toLowerCase(Locale.ROOT));
            case ID -> Comparator.comparing(Pack::getId);
            case COMPATIBILITY -> Comparator.comparing(Pack::getCompatibility);
            case NONE -> null;
        };

        if (comparator != null) {
            this.items.sort(comparator);
        }
    }

    // --- Los records Snapshot y Query se mantienen igual pero dentro de la clase ---
    public record Snapshot(List<Pack> items, List<Pack> selected) {
        public void restore(PackListModel model) {
            model.items.clear();
            model.items.addAll(this.items);
            model.selectedItems.clear();
            model.selectedItems.addAll(this.selected);
            model.refresh();
        }
    }

    public record Query(String search, SortOption sort, boolean hideIncompatible) {
        public Query withSearch(String search) { return new Query(search, this.sort, this.hideIncompatible); }
        public Query withSort(SortOption sort) { return new Query(this.search, sort, this.hideIncompatible); }
        public Query withHideIncompatible(boolean hide) { return new Query(this.search, this.sort, hide); }

        public enum SortOption { NONE, NAME, ID, COMPATIBILITY }
    }

    // Métodos de utilidad para actualizar el estado
    public boolean sort(Query.SortOption sortOption) {
        if (this.query.sort() == sortOption) return false;
        this.query = this.query.withSort(sortOption);
        this.applySort();
        return true;
    }

    public boolean search(@NotNull String term) {
        if (this.query.search().equals(term)) return false;
        this.query = this.query.withSearch(term);
        return true;
    }
}
