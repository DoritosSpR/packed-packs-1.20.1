package io.github.fishstiz.packed_packs.pack;

import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.FilePackResources;
import net.minecraft.server.packs.repository.Pack;
import java.nio.file.Path;

public record FolderResourcesSupplier(String id, Path path) implements Pack.ResourcesSupplier {
    @Override
    public PackResources open(String id) {
        // En 1.20.1 usamos la implementación nativa para carpetas/archivos
        return new FilePackResources(this.id, this.path.toFile(), false);
    }

    @Override
    public PackResources openFull(String id, Pack.Info info) {
        return this.open(id);
    }
}
