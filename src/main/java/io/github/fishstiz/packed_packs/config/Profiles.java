package io.github.fishstiz.packed_packs.config;

import net.minecraft.server.packs.PackType;
import java.util.HashMap;
import java.util.Map;

public class Profiles {
    private static final Map<PackType, Map<String, Profile>> CACHE = new HashMap<>();

    public static Profile get(PackType type, String id) {
        if (id == null) return null;
        return CACHE.computeIfAbsent(type, k -> new HashMap<>()).get(id);
    }

    public static void save(PackType type, Profile profile) {
        CACHE.computeIfAbsent(type, k -> new HashMap<>()).put(profile.getName(), profile);
        // Aquí iría la lógica de guardado a disco real
    }
}
