package io.github.fishstiz.packed_packs.transform.mixin.overrides;

import io.github.fishstiz.packed_packs.transform.interfaces.ConfiguredPack;
import net.minecraft.server.packs.repository.Pack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import net.minecraft.network.chat.Component;

@Mixin(Pack.class)
public abstract class PackMixin implements ConfiguredPack {
    // 1.20.1 no usa PackSelectionConfig ni Pack.Metadata (usa Pack.Info o campos directos)
    @Shadow @org.spongepowered.asm.mixin.Final private Component description;

    // Eliminamos los métodos y campos que mencionan PackSelectionConfig
    
    @Override
    public Component packed_packs$getDescription() {
        return this.description;
    }

    // Si necesitas la "Metadata" en 1.20.1, usualmente se accede vía campos de Pack
    // Implementa aquí solo lo que ConfiguredPack requiera para esta versión.
}
