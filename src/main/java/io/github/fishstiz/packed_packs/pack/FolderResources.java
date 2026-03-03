package io.github.fishstiz.packed_packs.pack.folder;

import io.github.fishstiz.packed_packs.util.PackUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.server.packs.resources.IoSupplier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
    @Nullable
    public IoSupplier<InputStream> getRootResource(String... elements) {
        // En 1.20.1, elements suele ser el nombre del archivo (ej: "pack.mcmeta")
        if (elements.length > 0) {
            String fileName = String.join("/", elements);
            if (fileName.equals(PackUtil.ICON_FILENAME) || fileName.equals(FOLDER_CONFIG_FILENAME)) {
                Path filePath = this.path.resolve(fileName);
                if (Files.exists(filePath)) {
                    return IoSupplier.create(filePath);
                }
            }
        }
        return null;
    }

    @Override
    @Nullable
    public IoSupplier<InputStream> getResource(PackType packType, ResourceLocation location) {
        // FolderPack en este mod funciona como contenedor, no suele tener texturas/modelos directos
        return null;
    }

    @Override
    public void listResources(PackType packType, String namespace, String path, PackResources.ResourceOutput resourceOutput) {
        // En 1.20.1, el parámetro es PackResources.ResourceOutput
        // Si no tiene recursos propios, se deja vacío para evitar errores de iteración
    }

    @Override
    @NotNull
    public Set<String> getNamespaces(PackType type) {
        return Collections.emptySet();
    }

    @Override
    @Nullable
    public <T> T getMetadataSection(MetadataSectionSerializer<T> deserializer) {
        // Si el FolderPack no tiene un pack.mcmeta real, devolvemos null
        return null;
    }

    @Override
    @NotNull
    public String packId() {
        return this.name;
    }

    @Override
    public void close() {
        // No hay flujos abiertos que cerrar aquí
    }
}
