package io.github.fishstiz.packed_packs.transform.interfaces;

import io.github.fishstiz.packed_packs.pack.PackOptionsResolver;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.Pack;

public interface ConfiguredPack {
    default void packed_packs$setConfigurationResolver(PackOptionsResolver resolver) {
    }

    default boolean packed_packs$isHidden() {
        return false;
    }

    default boolean packed_packs$isConfigured() {
        return false;
    }

    // Reemplazamos el objeto inexistente por sus componentes individuales
    boolean packed_packs$isRequired();
    boolean packed_packs$isFixed();
    Pack.Position packed_packs$getDefaultPosition();

    // En 1.20.1, si necesitas la descripción, es mejor devolver el Component
    Component packed_packs$getDescription();
}
