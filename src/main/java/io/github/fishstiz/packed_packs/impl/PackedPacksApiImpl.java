package io.github.fishstiz.packed_packs.impl;

import io.github.fishstiz.packed_packs.PackedPacks;
import io.github.fishstiz.packed_packs.api.PackedPacksApi;

public final class PackedPacksApiImpl implements PackedPacksApi {
    private final EventBusImpl eventBus = new EventBusImpl();
    private final PreferenceRegistryImpl preferenceRegistry = new PreferenceRegistryImpl();

    private PackedPacksApiImpl() {
    }

    public static PackedPacksApiImpl getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public PreferenceRegistryImpl preferences() {
        return this.preferenceRegistry;
    }

    @Override
    public EventBusImpl eventBus() {
        return this.eventBus;
    }

    private static final class Holder {
        private static final PackedPacksApiImpl INSTANCE;

        static {
            PackedPacksApiImpl api = new PackedPacksApiImpl();

            // En Forge 1.20.1, si no tienes otros mods que dependan de este, 
            // podemos omitir la búsqueda de extensiones por ahora para que compile.
            
            api.eventBus.freeze();
            api.preferenceRegistry.freeze();

            INSTANCE = api;
        }
    }
}
