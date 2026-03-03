package io.github.fishstiz.packed_packs.config;

import net.minecraft.server.packs.repository.Pack;
import java.nio.file.Path;
import java.io.Serializable;
import java.util.*;

public class Profile implements Serializable {
    public String id;
    public String name;
    public Path saveFolder;
    public boolean temp = false;
    private final Set<String> packIds = new HashSet<>();
    private final Set<String> hiddenPacks = new HashSet<>();
    private final Set<String> fixedPacks = new HashSet<>();
    private final Map<String, Pack.Position> positions = new HashMap<>();

    public Profile() {}

    public Profile(String name, Path saveFolder) {
        this.name = name;
        this.saveFolder = saveFolder;
    }

    public String getId() { return id != null ? id : ""; }
    public String getName() { return name != null ? name : id; }
    
    public boolean isHidden(Pack pack) { return hiddenPacks.contains(pack.getId()); }
    public boolean isFixed(Pack pack) { return fixedPacks.contains(pack.getId()); }
    public boolean isLocked() { return false; }
    
    public Pack.Position getPosition(Pack pack) { return positions.getOrDefault(pack.getId(), pack.getDefaultPosition()); }
    public boolean overridesPosition(Pack pack) { return positions.containsKey(pack.getId()) || fixedPacks.contains(pack.getId()); }

    public void setRequired(Boolean required, Pack pack) {
        if (required == null) fixedPacks.remove(pack.getId());
        else if (required) fixedPacks.add(pack.getId());
    }

    public boolean hasOverride(Pack pack) { return packIds.contains(pack.getId()); }
}
