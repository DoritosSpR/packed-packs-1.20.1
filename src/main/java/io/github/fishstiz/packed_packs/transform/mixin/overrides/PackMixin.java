package io.github.fishstiz.packed_packs.transform.mixin.overrides;

import net.minecraft.server.packs.repository.Pack;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

@Mixin(Pack.class)
public abstract class PackMixin {
    @Shadow private final Component title;
    @Shadow private final Component description;

    // En 1.20.1 NO EXISTE PackSelectionConfig ni Pack.Metadata.
    // Si necesitas guardar datos extra, usa campos @Unique simples.
    
    @Unique
    public Component packed_packs$getTitle() {
        return this.title;
    }
}
