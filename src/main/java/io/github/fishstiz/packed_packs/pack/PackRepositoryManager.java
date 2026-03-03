package io.github.fishstiz.packed_packs.pack;

import com.google.common.collect.Lists;
import net.minecraft.Util;
import net.minecraft.server.packs.repository.Pack;
import java.nio.file.Path;
import java.util.List;

public class PackRepositoryManager {
    private final Path packDir;

    public PackRepositoryManager(Path packDir) {
        this.packDir = packDir;
    }

    public void handlePacks(List<Pack> selected) {
        // CORRECCIÓN: Lists.reverse en lugar de .reversed()
        for (Pack pack : Lists.reverse(selected)) {
            // Lógica de procesamiento
        }
    }

    public void openPackFolder() {
        // CORRECCIÓN: En 1.20.1 es openFile o openUri para carpetas
        Util.getPlatform().openFile(this.packDir.toFile());
    }
}
