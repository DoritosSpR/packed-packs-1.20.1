package io.github.fishstiz.packed_packs.pack.folder;

import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import java.nio.file.Path;

public class FolderPack {
    // Ejemplo de la línea que fallaba en el constructor/supplier:
    public static Pack create(String id, Path path) {
        return Pack.readMetaAndCreate(
            id, 
            net.minecraft.network.chat.Component.literal(id), 
            false, 
            (name) -> new net.minecraft.server.packs.PathPackResources(name, path, false), 
            PackType.CLIENT_RESOURCES, 
            Pack.Position.TOP, 
            net.minecraft.server.packs.repository.PackSource.DEFAULT
        );
    }
}
