package io.github.fishstiz.packed_packs.config;

import net.minecraft.server.packs.PackType;
import java.util.HashMap;
import java.util.Map;

public class Config {
    private static final Config INSTANCE = new Config();
    private boolean devMode = false;
    private final Map<PackType, Packs> packConfigs = new HashMap<>();

    public Config() {
        // Inicializar configuraciones para Cliente y Servidor
        packConfigs.put(PackType.CLIENT_RESOURCES, new Packs());
        packConfigs.put(PackType.SERVER_DATA, new Packs());
    }

    public static Config get() {
        return INSTANCE;
    }

    public Packs get(PackType type) {
        return packConfigs.getOrDefault(type, packConfigs.get(PackType.CLIENT_RESOURCES));
    }

    public boolean isDevMode() {
        return devMode;
    }

    // Clase interna que el compilador está buscando en PackOptionsContext, OptionsLayout, etc.
    public static class Packs {
        private String selectedProfile = "default";
        private final Map<String, Profile> profiles = new HashMap<>();

        public Packs() {
            profiles.put("default", new Profile("default"));
        }

        public Map<String, Profile> getProfiles() {
            return profiles;
        }

        public String getSelectedProfile() {
            return selectedProfile;
        }

        public void setSelectedProfile(String name) {
            this.selectedProfile = name;
        }
    }
}
