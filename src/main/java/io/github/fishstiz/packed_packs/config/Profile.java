package io.github.fishstiz.packed_packs.config;

import io.github.fishstiz.packed_packs.util.PackUtil;
import net.minecraft.server.packs.repository.Pack;
import java.util.*;

public class Profile {
    private final Set<String> packIds = new HashSet<>();

    public boolean remapPackId(String oldId, String newId) {
        if (packIds.contains(oldId)) {
            packIds.remove(oldId);
            packIds.add(newId);
            return true;
        }
        return false;
    }

    public boolean overridesRequired(Pack pack) {
        // Tu lógica para determinar si este perfil sobreescribe la obligatoriedad
        return false; 
    }

    public boolean isRequired(Pack pack) {
        return PackUtil.isEssential(pack);
    }

    public boolean hasOverride(Pack pack) {
        return packIds.contains(pack.getId());
    }
}
