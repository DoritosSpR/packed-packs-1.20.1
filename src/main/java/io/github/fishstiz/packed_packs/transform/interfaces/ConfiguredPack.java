package io.github.fishstiz.packed_packs.transform.interfaces;

import io.github.fishstiz.packed_packs.pack.PackOptionsResolver;
import io.github.fishstiz.packed_packs.config.PackOverride; // Asumiendo que esta es la clase de configuración
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

    // Métodos requeridos por PackOverride.java
    boolean packed_packs$isRequired();
    boolean packed_packs$isFixed();
    Pack.Position packed_packs$getDefaultPosition();
    
    /**
     * Devuelve la configuración original del pack. 
     * Requerido por PackOverride para determinar posiciones por defecto.
     */
    PackOverride packed_packs$originalConfig();

    /**
     * Devuelve los metadatos extendidos del pack (compatibilidad, etc).
     * Requerido por PackListDevMenu.
     */
    Pack packed_packs$getMetadata();

    // En 1.20.1, si necesitas la descripción, es mejor devolver el Component
    Component packed_packs$getDescription();
}
