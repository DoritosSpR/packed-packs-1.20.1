package io.github.fishstiz.packed_packs.pack.folder;

import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.PackResources;
import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

// En 1.20.1, Pack.ResourcesSupplier suele ser una interfaz funcional simple 
// que devuelve un PackResources a partir de un String (el ID)
public record FolderResourcesSupplier(Path path) implements Pack.ResourcesSupplier {

    @Override
    public @NotNull PackResources open(String id) {
        // En 1.20.1, el método se llama 'open' y recibe el ID como String
        return new FolderResources(id, this.path);
    }
}
