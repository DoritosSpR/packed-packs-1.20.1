package io.github.fishstiz.packed_packs.pack;

import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackCompatibility;
import net.minecraft.server.packs.repository.PackSource;
import java.util.List;

public abstract class FolderPack extends Pack {
    
    public FolderPack(String id, boolean required, ResourcesSupplier resources, Component title, Info info, PackCompatibility compatibility, Position position, boolean fixed, PackSource source) {
        super(id, required, resources, title, info, compatibility, position, fixed, source);
    }

    public abstract List<Pack> flatten();
}
