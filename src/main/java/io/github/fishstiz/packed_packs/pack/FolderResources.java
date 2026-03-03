package io.github.fishstiz.packed_packs.pack;

import net.minecraft.server.packs.PathPackResources;
import java.nio.file.Path;

public class FolderResources extends PathPackResources {
    public static final String FOLDER_CONFIG_FILENAME = ".folder_config.json";

    public FolderResources(String id, Path path) {
        super(id, path, false);
    }
}
