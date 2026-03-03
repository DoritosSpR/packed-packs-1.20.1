package io.github.fishstiz.packed_packs.config;

import net.minecraft.server.packs.repository.Pack;
import java.io.Serializable;
import java.util.*;

public class Profile implements Serializable {
    private final String name;
    private List<String> selectedPacks = new ArrayList<>();
    private Set<String> requiredPacks = new HashSet<>();
    private Set<String> hiddenPacks = new HashSet<>();
    private Map<String, Pack.Position> packPositions = new HashMap<>();

    public Profile(String name) { this.name = name; }

    public String getName() { return name; }
    public String getId() { return name; }
    
    // --- Métodos que faltaban para que compile DevConfig y PackOptionsResolver ---

    public boolean remapPackId(String oldId, String newId) {
        boolean changed = false;
        if (selectedPacks.remove(oldId)) { selectedPacks.add(newId); changed = true; }
        if (requiredPacks.remove(oldId)) { requiredPacks.add(newId); changed = true; }
        if (hiddenPacks.remove(oldId)) { hiddenPacks.add(newId); changed = true; }
        if (packPositions.containsKey(oldId)) {
            packPositions.put(newId, packPositions.remove(oldId));
            changed = true;
        }
        return changed;
    }

    public boolean overridesRequired(Pack pack) {
        return requiredPacks.contains(pack.getId());
    }

    public boolean overridesPosition(Pack pack) {
        return packPositions.containsKey(pack.getId());
    }

    public boolean hasOverride(Pack pack) {
        return overridesRequired(pack) || overridesPosition(pack) || isHidden(pack);
    }

    public boolean isLocked() {
        return false; // O implementar lógica de bloqueo si el mod la requiere
    }

    public boolean isFixed(Pack pack) {
        return overridesPosition(pack);
    }

    // --- Métodos de gestión ---

    public boolean includes(Pack pack) {
        return selectedPacks.contains(pack.getId()) || requiredPacks.contains(pack.getId());
    }

    public void setHidden(boolean hidden, List<Pack> packs) {
        for (Pack p : packs) {
            if (hidden) hiddenPacks.add(p.getId());
            else hiddenPacks.remove(p.getId());
        }
    }

    public void setRequired(boolean required, Pack pack) {
        if (required) requiredPacks.add(pack.getId());
        else requiredPacks.remove(pack.getId());
    }

    public void setPosition(Pack.Position pos, List<Pack> packs) {
        for (Pack p : packs) {
            packPositions.put(p.getId(), pos);
        }
    }

    public boolean isHidden(Pack pack) { return hiddenPacks.contains(pack.getId()); }
    public boolean isRequired(Pack pack) { return requiredPacks.contains(pack.getId()); }
    public Pack.Position getPosition(Pack pack) { return packPositions.getOrDefault(pack.getId(), Pack.Position.TOP); }
}
