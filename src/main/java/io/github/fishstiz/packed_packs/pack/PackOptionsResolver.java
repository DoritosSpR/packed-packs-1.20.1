package io.github.fishstiz.packed_packs.pack;

import io.github.fishstiz.fidgetz.util.lang.FunctionsUtil;
import io.github.fishstiz.packed_packs.config.DevConfig;
import io.github.fishstiz.packed_packs.config.PackOptions;
import io.github.fishstiz.packed_packs.config.Profile;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.*;

public record PackOptionsResolver(
        Supplier<@Nullable Profile> profileSupplier,
        DevConfig.Packs config
) implements PackOptions {
    
    public static final PackOptionsResolver DATA_PACKS = new PackOptionsResolver(DevConfig.get().get(PackType.SERVER_DATA));
    public static final PackOptionsResolver RESOURCE_PACKS = new PackOptionsResolver(DevConfig.get().get(PackType.CLIENT_RESOURCES));

    public PackOptionsResolver(DevConfig.Packs config) {
        this(FunctionsUtil.nullSupplier(), config);
    }

    @Override
    public boolean isHidden(Pack pack) {
        return Boolean.TRUE.equals(this.inDefaultOrSelected(pack, Profile::isHidden, Profile::isHidden));
    }

    @Override
    public boolean isRequired(Pack pack) {
        return Boolean.TRUE.equals(this.inDefaultOrSelected(pack, Profile::overridesRequired, Profile::isRequired));
    }

    @Override
    public boolean isFixed(Pack pack) {
        return Boolean.TRUE.equals(this.inDefaultOrSelected(pack, Profile::overridesPosition, Profile::isFixed));
    }

    @Override
    public @Nullable Pack.Position getPosition(Pack pack) {
        return this.inDefaultOrSelected(pack, Profile::overridesPosition, Profile::getPosition);
    }

    // Eliminados los métodos de PackSelectionConfig

    public boolean isRequiredOrDefault(Pack pack) {
        return this.inDefaultOrSelected(pack, Profile::overridesRequired, Profile::isRequired, Pack::isRequired);
    }

    public boolean isFixedOrDefault(Pack pack) {
        return this.inDefaultOrSelected(pack, Profile::overridesPosition, Profile::isFixed, Pack::isFixedPosition);
    }

    public @NotNull Pack.Position getPositionOrDefault(Pack pack) {
        return Objects.requireNonNull(this.inDefaultOrSelected(pack, Profile::overridesPosition, Profile::getPosition, Pack::getDefaultPosition));
    }

    public boolean overridesRequired(Pack pack) {
        return this.hasOverride(pack, Profile::overridesRequired);
    }

    public boolean overridesPosition(Pack pack) {
        return this.hasOverride(pack, Profile::overridesPosition);
    }

    private boolean hasOverride(Pack pack, BiPredicate<Profile, Pack> option) {
        Profile defaultProfile = this.config.getDefaultProfile();
        Profile selected = this.profileSupplier.get();

        if (defaultProfile != null && option.test(defaultProfile, pack)) {
            return true;
        }
        return selected != null && option.test(selected, pack);
    }

    private <T> T inDefaultOrSelected(Pack pack, BiPredicate<Profile, Pack> shouldApply, BiFunction<Profile, Pack, T> option, Function<Pack, T> defaultValue) {
        Profile defaultProfile = this.config.getDefaultProfile();
        if (defaultProfile != null && shouldApply.test(defaultProfile, pack)) {
            return option.apply(defaultProfile, pack);
        }
        Profile selected = this.profileSupplier.get();
        if (selected != null && shouldApply.test(selected, pack)) {
            return option.apply(selected, pack);
        }
        return defaultValue.apply(pack);
    }

    private <T> @Nullable T inDefaultOrSelected(Pack pack, BiPredicate<Profile, Pack> shouldApply, BiFunction<Profile, Pack, T> option) {
        return this.inDefaultOrSelected(pack, shouldApply, option, p -> null);
    }
}
