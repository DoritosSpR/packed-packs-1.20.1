package io.github.fishstiz.packed_packs.pack;

import io.github.fishstiz.packed_packs.util.PackUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.PackType;
import java.util.List;

public abstract class FolderPack extends Pack {
    
    public FolderPack(String id, boolean required, ResourcesSupplier resources, Component title, Info info, Position position, boolean fixed, boolean hidden) {
        // Corregido: info.compatibility ahora requiere el PackType (usualmente CLIENT_RESOURCES o SERVER_DATA)
        super(id, fixed, resources, title, info, info.compatibility(PackType.CLIENT_RESOURCES), position, hidden, PackUtil.PACK_SOURCE);
    }

    public abstract List<Pack> flatten();
}
