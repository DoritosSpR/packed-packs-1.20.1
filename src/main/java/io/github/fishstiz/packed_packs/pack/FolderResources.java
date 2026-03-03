package io.github.fishstiz.packed_packs.pack;

import net.minecraft.server.packs.AbstractPackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.Collections;

public class FolderResources extends AbstractPackResources {
    public static final String FOLDER_CONFIG_FILENAME = ".folder_config.json";
    private final Path root;

    public FolderResources(String id, Path root, boolean isBuiltin) {
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
        return Files.exists(path) ? IoSupplier.create(path) : null;
    }

    @Override
    @Nullable
    public IoSupplier<InputStream> getResource(PackType type, ResourceLocation location) {
        Path path = this.root.resolve(type.getDirectory()).resolve(location.getNamespace()).resolve(location.getPath());
        return Files.exists(path) ? IoSupplier.create(path) : null;
    }

    @Override
    public void listResources(PackType type, String namespace, String path, ResourceOutput output) {
        // Implementación de escaneo de archivos necesaria para 1.20.1
    }

    @Override
    public Set<String> getNamespaces(PackType type) {
        return Collections.emptySet();
    }

    @Override
    public void close() {}
}
