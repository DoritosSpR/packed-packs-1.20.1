package io.github.fishstiz.packed_packs.gui.components.pack;

import io.github.fishstiz.packed_packs.gui.components.events.PackListEventListener;
import io.github.fishstiz.packed_packs.pack.PackAssetManager;
import io.github.fishstiz.packed_packs.pack.PackFileOperations;
import io.github.fishstiz.packed_packs.pack.PackOptionsContext;
import net.minecraft.client.Minecraft;
import net.minecraft.server.packs.repository.Pack;
import java.util.List;

public class FolderPackList extends PackList {

    public FolderPackList(Minecraft minecraft, int width, int height, int top, int bottom, 
                          PackOptionsContext options, PackAssetManager assets, 
                          PackFileOperations fileOps, PackListEventListener listener) {
        super(minecraft, width, height, top, bottom, 36, options, assets, fileOps, listener);
    }

    @Override
    public boolean isTransferable(Pack pack) { return false; }
    @Override
    public void removeAll(List<Pack> packs) {}
    @Override
    public void addAll(List<Pack> packs) {}
    @Override
    public void select(Pack pack) {}
    @Override
    public void selectAll(List<Pack> packs) {}
    @Override
    public void clearSelection() {}
    @Override
    public Entry getEntry(Pack pack) { return null; }
    @Override
    public boolean canInteract(PackList source) { return false; }
}
