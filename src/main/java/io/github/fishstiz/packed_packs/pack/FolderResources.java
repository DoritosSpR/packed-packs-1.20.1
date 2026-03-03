package io.github.fishstiz.packed_packs.pack;

import net.minecraft.server.packs.AbstractPackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.Set;

public class FolderResources extends AbstractPackResources {
    private final Path root;

    public FolderResources(String id, boolean isBuiltin, Path root) {
        super(id, isBuiltin);
        this.root = root;
    }

    @Override
    @Nullable
    public IoSupplier<InputStream> getRootResource(String... paths) {
        Path path = this.root;
        for (String s : paths) {
            path = path.resolve(s);
        }
        return java.nio.file.Files.exists(path) ? IoSupplier.create(path) : null;
    }

    @Override
    @Nullable
    public IoSupplier<InputStream> getResource(PackType type, ResourceLocation location) {
        // Lógica básica para obtener recursos (texturas, json, etc)
        return null; 
    }

    @Override
    public void listResources(PackType type, String namespace, String path, ResourceOutput output) {
        // Implementación requerida por AbstractPackResources
    }

    @Override
    public Set<String> getNamespaces(PackType type) {
        return Set.of();
    }

    @Override
    public void close() {
    }
}
