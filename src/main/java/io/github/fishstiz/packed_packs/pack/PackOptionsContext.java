package io.github.fishstiz.packed_packs.pack;

import io.github.fishstiz.packed_packs.config.Config;
import io.github.fishstiz.packed_packs.config.Profile;
import io.github.fishstiz.packed_packs.config.ProfileScope;
import net.minecraft.server.packs.repository.Pack;
import java.util.Optional;
import java.util.function.BiPredicate;

public class PackOptionsContext {
    private final Config config;
    private final String profileName;

    public PackOptionsContext(Config config, String profileName) {
        this.config = config;
        this.profileName = profileName;
    }

    public Config getConfig() {
        return this.config;
    }

    public Optional<Profile> getProfile() {
        return Optional.ofNullable(this.config.getProfiles().get(this.profileName));
    }

    public boolean isDefaultProfile() {
        return "default".equals(this.profileName);
    }

    public boolean isLocked() {
        return false; // Implementar lógica de bloqueo si es necesario
    }

    public ProfileScope hasOverride(Pack pack, BiPredicate<Profile, Pack> condition) {
        return condition.test(this.config.getProfiles().get("default"), pack) ? ProfileScope.GLOBAL : ProfileScope.NONE;
    }

    public boolean hasOverride(Pack pack) {
        return false;
    }

    public boolean isFixed(Pack pack) {
        return pack.isFixedPosition();
    }

    public boolean isRequired(Pack pack) {
        return pack.isRequired();
    }

    public Pack.Position getPosition(Pack pack) {
        return pack.getDefaultPosition();
    }

    public void setRequired(Pack pack, boolean required) {
        this.getProfile().ifPresent(profile -> {
            // Corregido: ya no pasa null, usa el booleano
            profile.setRequired(required, pack);
        });
    }
}
