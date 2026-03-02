package io.github.fishstiz.packed_packs.transform.mixin.folders;

import io.github.fishstiz.packed_packs.pack.PackOptionsResolver;
import io.github.fishstiz.packed_packs.transform.interfaces.ConfiguredPack;
import io.github.fishstiz.packed_packs.transform.interfaces.FilePack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackCompatibility;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.nio.file.Path;

@Mixin(Pack.class)
public abstract class PackMixin implements FilePack, ConfiguredPack {
    
    // En 1.20.1 estos campos son directos en la clase Pack
    @Shadow @Final private Component description;
    @Shadow @Final private PackCompatibility compatibility;
    @Shadow @Final private boolean required;
    @Shadow @Final private boolean fixedPosition;
    @Shadow @Final private Pack.Position defaultPosition;

    @Unique private boolean packed_packs$nested = false;
    @Unique private Path packed_packs$path;
    @Unique private PackOptionsResolver packed_packs$resolver;

    @Override
    public boolean packed_packs$nestedPack() { return this.packed_packs$nested; }

    @Override
    public void packed_packs$setNestedPack(boolean nested) { this.packed_packs$nested = nested; }

    @Override
    public void packed_packs$setPath(Path path) { this.packed_packs$path = path; }

    @Override
    public @Nullable Path packed_packs$getPath() { return this.packed_packs$path; }

    @Override
    public void packed_packs$setConfigurationResolver(PackOptionsResolver resolver) {
        this.packed_packs$resolver = resolver;
    }

    @Override
    public boolean packed_packs$isHidden() {
        return this.packed_packs$resolver != null && this.packed_packs$resolver.isHidden((Pack) (Object) this);
    }

    @Override
    public boolean packed_packs$isConfigured() { return this.packed_packs$resolver != null; }

    @Override
    public boolean packed_packs$isRequired() { return this.required; }

    @Override
    public boolean packed_packs$isFixed() { return this.fixedPosition; }

    @Override
    public Pack.Position packed_packs$getDefaultPosition() { return this.defaultPosition; }

    @Override
    public Component packed_packs$getDescription() { return this.description; }
}
