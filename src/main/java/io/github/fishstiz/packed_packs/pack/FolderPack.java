package io.github.fishstiz.packed_packs.pack;

import io.github.fishstiz.packed_packs.util.PackUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.Pack;
import java.util.List;

public abstract class FolderPack extends Pack {
    
    public FolderPack(String id, boolean required, ResourcesSupplier resources, Component title, Info info, Position position, boolean fixed, boolean hidden) {
        // En 1.20.1 el orden es: id, fixed, resources, title, info, compatibility, position, hidden, source
        super(id, fixed, resources, title, info, info.compatibility(), position, hidden, PackUtil.PACK_SOURCE);
    }

    public abstract List<Pack> flatten();
}
