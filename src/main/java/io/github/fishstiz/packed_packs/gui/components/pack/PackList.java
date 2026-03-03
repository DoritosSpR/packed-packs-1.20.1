package io.github.fishstiz.packed_packs.gui.components.pack;

import io.github.fishstiz.fidgetz.gui.components.Fidgetz;
import io.github.fishstiz.packed_packs.gui.components.events.PackListEventListener;
import io.github.fishstiz.packed_packs.pack.PackAssetManager;
import io.github.fishstiz.packed_packs.pack.PackFileOperations;
import io.github.fishstiz.packed_packs.pack.PackOptionsContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.Pack;
import java.util.List;

public abstract class PackList extends ObjectSelectionList<PackList.Entry> implements Fidgetz {
    protected final PackOptionsContext options;
    protected final PackAssetManager assets;
    protected final PackFileOperations fileOps;
    protected final PackListEventListener listener;
    protected int headerHeight = 0;
    public int scrollbarOffset = 6;

    public PackList(Minecraft minecraft, int width, int height, int top, int bottom, int itemHeight, 
                    PackOptionsContext options, PackAssetManager assets, PackFileOperations fileOps, PackListEventListener listener) {
        // En 1.20.1: minecraft, width, height, y0 (top), y1 (bottom), itemHeight
        super(minecraft, width, height, top, bottom, itemHeight);
        this.options = options;
        this.assets = assets;
        this.fileOps = fileOps;
        this.listener = listener;
    }

    // Métodos de utilidad para las clases hijas (corrige error en AvailablePackList)
    public boolean scrollbarVisible() {
        return this.getMaxScroll() > 0;
    }

    @Override public int getX() { return this.x0; }
    @Override public int getY() { return this.y0; }
    @Override public int getWidth() { return this.width; }
    @Override public int getHeight() { return this.height; }

    public abstract boolean isTransferable(Pack pack);
    public abstract void removeAll(List<Pack> packs);
    public abstract void addAll(List<Pack> packs);
    public abstract void select(Pack pack);
    public abstract void selectAll(List<Pack> packs);
    public abstract void clearSelection();
    public abstract Entry getEntry(Pack pack);
    public abstract boolean canInteract(PackList source);

    public abstract static class Entry extends ObjectSelectionList.Entry<Entry> implements Fidgetz {
        public abstract Pack pack();
        public abstract boolean isSelectedLast();
        public abstract boolean isTransferable();
        public abstract void transfer();
        public abstract void onRename(Component newName);
        protected abstract void sendPacks(Pack trigger, List<Pack> required);

        @Override public int getX() { return 0; } 
        @Override public int getY() { return 0; } 
        @Override public int getWidth() { return 0; }
        @Override public int getHeight() { return 36; }
    }
}
