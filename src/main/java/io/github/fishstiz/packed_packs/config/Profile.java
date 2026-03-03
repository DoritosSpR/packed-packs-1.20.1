package io.github.fishstiz.packed_packs.config;

import net.minecraft.server.packs.repository.Pack;
import java.io.Serializable;
import java.util.*;

public class Profile implements Serializable {
    private final String name;
    private List<String> selectedPacks = new ArrayList<>();
    private Map<String, String> remappedIds = new HashMap<>();
    private Set<String> requiredPacks = new HashSet<>();
    private boolean locked = false;
    private boolean hidden = false;
    private boolean fixed = false;
    private Pack.Position position = Pack.Position.TOP;

    public Profile(String name) {
        this.name = name;
    }

    public String getName() { return name; }
    public List<String> getSelectedPacks() { return selectedPacks; }
    public boolean isLocked() { return locked; }
    public boolean isHidden(Pack pack) { return hidden; }
    public boolean isHidden() { return hidden; }
    public boolean isFixed(Pack pack) { return fixed; }
    public boolean isFixed() { return fixed; }
    public Pack.Position getPosition() { return position; }
    public Pack.Position getPosition(Pack pack) { return position; }

    public boolean isRequired(Pack pack) {
        return requiredPacks.contains(pack.getId());
    }

    public boolean overridesRequired(Pack pack) {
        return requiredPacks.contains(pack.getId()) || selectedPacks.contains(pack.getId());
    }

    public boolean overridesPosition(Pack pack) {
        return fixed;
    }

    public boolean hasOverride(Pack pack) {
        return requiredPacks.contains(pack.getId()) || selectedPacks.contains(pack.getId()) || fixed || hidden;
    }

    public void setRequired(Object ignored, Pack pack) {
        this.requiredPacks.add(pack.getId());
    }

    public boolean remapPackId(String oldId, String newId) {
        if (oldId.equals(newId)) return false;
        remappedIds.put(oldId, newId);
        if (selectedPacks.contains(oldId)) {
            Collections.replaceAll(selectedPacks, oldId, newId);
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Profile profile)) return false;
        return Objects.equals(name, profile.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
