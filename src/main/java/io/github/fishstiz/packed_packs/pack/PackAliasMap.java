package io.github.fishstiz.packed_packs.pack;

import io.github.fishstiz.packed_packs.config.Config;
import io.github.fishstiz.packed_packs.config.Profile;
import java.util.Collection;
import java.util.Map;

public class PackAliasMap {
    // ... resto del código ...

    public String resolveId(String packId) {
        // Obtenemos el mapa de perfiles
        Map<String, Profile> profilesMap = Config.get().get(this.config.packType()).getProfiles();
        
        // Convertimos los valores del mapa a una Colección para que coincida con la firma del método
        Collection<Profile> profiles = profilesMap.values();
        
        return this.config.getAndSaveCanonicalId(profiles, packId);
    }
}
