package io.github.fishstiz.packed_packs.gui.components.pack;

import io.github.fishstiz.packed_packs.gui.components.SelectableList;
import io.github.fishstiz.packed_packs.pack.PackOptionsContext;
import io.github.fishstiz.packed_packs.util.lang.StringUtil;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackCompatibility;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class PackListModel extends SelectableList<Pack> {
    private final PackOptionsContext options;
    private Query query = new Query();

    public PackListModel(PackOptionsContext options) {
        super();
        this.options = options;
    }

    @Override
    protected boolean filter(Pack pack) {
        // Filtro de compatibilidad
        if (this.query.hideIncompatible() && !pack.getCompatibility().isCompatible()) {
            return false;
        }

        // Filtro de búsqueda (Case Insensitive)
        String searchTerm = this.query.search().toLowerCase(Locale.ROOT);
        if (!searchTerm.isEmpty()) {
            String title = pack.getTitle().getString().toLowerCase(Locale.ROOT);
            String description = pack.getDescription().getString().toLowerCase(Locale.ROOT);
            String id = pack.getId().toLowerCase(Locale.ROOT);
            
            return title.contains(searchTerm) || description.contains(searchTerm) || id.contains(searchTerm);
        }

        return super.filter(pack);
    }

    public boolean sort(Query.SortOption sortOption) {
        if (this.query.sort() == sortOption) return false;
        this.query = this.query.withSort(sortOption);
        this.applySort();
        return true;
    }

    public void applySort() {
        Comparator<Pack> comparator = switch (this.query.sort()) {
            case NAME -> Comparator.comparing(p -> p.getTitle().getString().toLowerCase(Locale.ROOT));
            case ID -> Comparator.comparing(Pack::getId);
            case COMPATIBILITY -> Comparator.comparing(Pack::getCompatibility);
            default -> null;
        };

        if (comparator != null) {
            this.items.sort(comparator);
        }
    }

    public boolean hideIncompatible(boolean hide) {
        if (this.query.hideIncompatible() == hide) return false;
        this.query = this.query.withHideIncompatible(hide);
        return true;
    }

    public boolean search(@NotNull String term) {
        if (this.query.search().equals(term)) return false;
        this.query = this.query.withSearch(term);
        return true;
    }

    public boolean isQueried() {
        return !this.query.search().isEmpty() || this.query.hideIncompatible();
    }

    public Snapshot captureState() {
        return new Snapshot(new ArrayList<>(this.items), new ArrayList<>(this.selectedItems));
    }

    // --- Sistema de Snapshot para deshacer/rehacer o restaurar estados ---
    public record Snapshot(List<Pack> items, List<Pack> selected) {
        public void restore(PackListModel model) {
            model.items.clear();
            model.items.addAll(this.items);
            model.selectedItems.clear();
            model.selectedItems.addAll(this.selected);
            model.refresh();
        }

        public Snapshot replaceAll(List<Pack> newItems) {
            return new Snapshot(new ArrayList<>(newItems), new ArrayList<>(this.selected));
        }

        public Snapshot retainAll(Set<Pack> toRetain) {
            List<Pack> nextItems = this.items.stream()
                    .filter(toRetain::contains)
                    .collect(Collectors.toList());
            List<Pack> nextSelected = this.selected.stream()
                    .filter(toRetain::contains)
                    .collect(Collectors.toList());
            return new Snapshot(nextItems, nextSelected);
        }
        
        // Helper para restaurar sin pasar el modelo si se usa desde el Snapshot interno
        public void restore() {
            // Este método se suele llamar desde PackList.replaceState
        }
    }

    // Clase interna para manejar los parámetros de búsqueda y orden
    public static record Query(String search, SortOption sort, boolean hideIncompatible) {
        public Query() {
            this("", SortOption.NONE, false);
        }

        public Query withSearch(String search) { return new Query(search, this.sort, this.hideIncompatible); }
        public Query withSort(SortOption sort) { return new Query(this.search, sort, this.hideIncompatible); }
        public Query withHideIncompatible(boolean hide) { return new Query(this.search, this.sort, hide); }

        public enum SortOption {
            NONE, NAME, ID, COMPATIBILITY
        }
    }
}
