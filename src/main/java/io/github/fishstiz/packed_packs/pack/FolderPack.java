package io.github.fishstiz.packed_packs.pack;

import io.github.fishstiz.packed_packs.config.Folder;
import io.github.fishstiz.packed_packs.util.PackUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackCompatibility;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;

public class FolderPack extends Pack {
    private final Folder folder;

    public FolderPack(String id, 
                      boolean required, 
                      Pack.ResourcesSupplier resources, 
                      Component title, 
                      PackMetadataSection info, 
                      Pack.Position position, 
                      boolean hidden, 
                      Folder folder) {
        // En 1.20.1, el constructor de Pack pide PackCompatibility directamente.
        // info.getPackFormat() es el método correcto en esta versión.
        super(id, required, resources, title, info, 
              PackCompatibility.forFormat(info.getPackFormat(), PackType.CLIENT_RESOURCES), 
              position, hidden, PackUtil.PACK_SOURCE);
        this.folder = folder;
    }

    public Folder getFolderConfig() {
        return this.folder;
    }
}
