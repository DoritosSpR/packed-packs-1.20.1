package io.github.fishstiz.packed_packs.gui.components.contextmenu;

import net.minecraft.Util;
import java.nio.file.Path;
import java.io.File;

public class DirectoryMenuItem {
    private final File directory;

    public DirectoryMenuItem(Path path) {
        this.directory = path.toFile();
    }

    public void onClick() {
        // Util.getPlatform().openFile es el método correcto para 1.20.1
        Util.getPlatform().openFile(this.directory);
    }
}
