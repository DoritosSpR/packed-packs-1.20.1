package io.github.fishstiz.packed_packs.pack;

import net.minecraft.server.packs.PathPackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.resources.ResourceLocation;
import java.nio.file.Path;
import java.util.Set;

public class FolderResources extends PathPackResources {
    public static final String FOLDER_CONFIG_FILENAME = ".folder_config.json";

    public FolderResources(String id, Path path) {
        super(id, path, false);
    }

    @Override
    public Set<String> getNamespaces(PackType type) {
        return super.getNamespaces(type);
    }
}
