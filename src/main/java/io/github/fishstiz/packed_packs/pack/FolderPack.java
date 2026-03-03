package io.github.fishstiz.packed_packs.pack;

import io.github.fishstiz.packed_packs.util.PackUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.PackType;
import java.util.List;

public class FolderPack extends Pack {
    // En 1.20.1 no podemos extender Pack fácilmente si el constructor es privado.
    // Lo ideal es usar un wrapper o asegurar que el constructor coincida.
    
    public FolderPack(String id, boolean required, ResourcesSupplier resources, Component title, Info info, Position position, boolean hidden) {
        super(id, required, resources, title, info, PackCompatibility.forFormat(info.format(), PackType.CLIENT_RESOURCES), position, hidden, PackUtil.PACK_SOURCE);
    }

    public Component getTitle() {
        return this.getTitle(); // O el campo correspondiente
    }
}
