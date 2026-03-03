package io.github.fishstiz.packed_packs.pack;

import net.minecraft.server.packs.repository.Pack;
import net.minecraft.network.chat.Component;
import java.util.List;

// Importante: Debe heredar de Pack para ser compatible
public abstract class FolderPack extends Pack {
    
    public FolderPack(String id, boolean required, Pack.ResourcesSupplier resources, Component title, Pack.Info info, Pack.Position position, boolean fixed, boolean hidden) {
        super(id, required, resources, title, info, position, fixed, hidden);
    }

    public abstract List<Pack> flatten();
    public abstract Component getTitle();
}
