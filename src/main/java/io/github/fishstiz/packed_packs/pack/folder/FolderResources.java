package io.github.fishstiz.packed_packs.pack.folder;

import io.github.fishstiz.packed_packs.util.PackUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.server.packs.resources.IoSupplier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Set;

public class FolderResources implements PackResources {
    public static final String FOLDER_CONFIG_FILENAME = "packed_packs.folderpack.json";
    private final String name;
    private final Path path;

    public FolderResources(String name, Path path) {
        this.name = name;
        this.path = path;
    }

    @Override
    public @Nullable IoSupplier<InputStream> getRootResource(String... elements) {
        if (elements.length > 0) {
            String fileName = elements[0];
            if (fileName.equals(PackUtil.ICON_FILENAME) || fileName.equals(FOLDER_CONFIG_FILENAME)) {
                Path filePath = this.path.resolve(fileName);
                if (Files.exists(filePath)) {
                    return () -> Files.newInputStream(filePath);
                }
            }
        }
        return null;
    }

    @Override
    public @Nullable IoSupplier<InputStream> getResource(PackType packType, ResourceLocation location) {
        // FolderPack suele ser solo un contenedor, no tiene recursos propios de MC
        return null;
    }

    @Override
    public void listResources(PackType packType, String namespace, String path, ResourceOutput resourceOutput) {
        // En 1.20.1 este método es ligeramente diferente. 
        // Si usas Architectury o multiloader, asegúrate de que ResourceOutput coincida.
    }

    @Override
    public @NotNull Set<String> getNamespaces(PackType type) {
        return Collections.emptySet();
    }

    @Override
    public @Nullable <T> T getMetadataSection(MetadataSectionSerializer<T> deserializer) {
        // En 1.20.1, si no tiene pack.mcmeta, devolvemos null o un metadato simulado
        return null;
    }

    @Override
    public @NotNull String packId() {
        return this.name;
    }

    @Override
    public void close() {
    }
}
