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
    public String getId() { return name; } // Añadido para DevConfig
    
    public boolean includes(Pack pack) {
        return selectedPacks.contains(pack.getId()) || requiredPacks.contains(pack.getId());
    }

    public void setHidden(boolean hidden, List<Pack> packs) {
        for (Pack p : packs) {
            if (hidden) hiddenPacks.add(p.getId());
            else hiddenPacks.remove(p.getId());
        }
    }

    public void setRequired(boolean required, List<Pack> packs) {
        for (Pack p : packs) {
            if (required) requiredPacks.add(p.getId());
            else requiredPacks.remove(p.getId());
        }
    }

    public void setPosition(Pack.Position pos, List<Pack> packs) {
        for (Pack p : packs) {
            packPositions.put(p.getId(), pos);
        }
    }

    // Métodos de consulta existentes actualizados
    public boolean isHidden(Pack pack) { return hiddenPacks.contains(pack.getId()); }
    public boolean isRequired(Pack pack) { return requiredPacks.contains(pack.getId()); }
    public Pack.Position getPosition(Pack pack) { return packPositions.getOrDefault(pack.getId(), Pack.Position.TOP); }
}
