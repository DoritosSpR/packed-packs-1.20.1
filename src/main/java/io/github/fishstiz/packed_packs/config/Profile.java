package io.github.fishstiz.packed_packs.config;

import net.minecraft.server.packs.repository.Pack;
import java.io.Serializable;
import java.util.*;

public class Profile implements Serializable {
    private String name;
    private List<String> selectedPacks = new ArrayList<>();
    private Map<String, String> remappedIds = new HashMap<>();
    private Set<String> requiredPacks = new HashSet<>();

    public Profile(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public List<String> getSelectedPacks() {
        return selectedPacks;
    }

    public boolean isRequired(Pack pack) {
        return requiredPacks.contains(pack.getId());
    }

    public boolean overridesRequired(Pack pack) {
        // Lógica para determinar si este perfil fuerza el estado de un pack
        return requiredPacks.contains(pack.getId()) || selectedPacks.contains(pack.getId());
    }

    public boolean remapPackId(String oldId, String newId) {
        if (oldId.equals(newId)) return false;
        remappedIds.put(oldId, newId);
        
        // Actualizar en la lista de seleccionados si existe
        if (selectedPacks.contains(oldId)) {
            Collections.replaceAll(selectedPacks, oldId, newId);
        }
        return true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Profile profile = (Profile) o;
        return Objects.equals(name, profile.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
