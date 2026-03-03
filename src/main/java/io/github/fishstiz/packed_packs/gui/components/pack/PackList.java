package io.github.fishstiz.packed_packs.gui.components.pack;

import io.github.fishstiz.packed_packs.gui.components.events.PackListEventListener;
import io.github.fishstiz.packed_packs.pack.PackAssetManager;
import io.github.fishstiz.packed_packs.pack.PackFileOperations;
import io.github.fishstiz.packed_packs.pack.PackOptionsContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.server.packs.repository.Pack;

public abstract class PackList extends ObjectSelectionList<PackList.Entry> {
    protected final PackOptionsContext options;
    protected final PackAssetManager assets;
    protected final PackFileOperations fileOps;
    protected final PackListEventListener listener;
    protected int headerHeight = 0;

    public PackList(PackOptionsContext options, PackAssetManager assets, PackFileOperations fileOps, PackListEventListener listener) {
        // Minecraft 1.20.1 constructor: Minecraft, width, height, top, bottom, itemHeight
        super(Minecraft.getInstance(), 200, 200, 32, 200 - 32, 36);
        this.options = options;
        this.assets = assets;
        this.fileOps = fileOps;
        this.listener = listener;
    }

    public int getRowTop(int index) {
        // En 1.20.1 usamos getY() (si está disponible) o el campo 'top'
        return this.getY() + 4 - (int)this.getScrollAmount() + index * this.itemHeight + this.headerHeight;
    }

    protected int getIndexAt(double mouseX, double mouseY) {
        return (int)((mouseY - (double)this.getY() + this.getScrollAmount() - (double)this.headerHeight) / (double)this.itemHeight);
    }
    
    // Método auxiliar para compatibilidad si getY() falla en algunas configuraciones de mappings
    @Override
    public int getY() {
        return this.y0; // y0 es el campo protegido para la parte superior en 1.20.1
    }

    public abstract static class Entry extends ObjectSelectionList.Entry<Entry> {
        public abstract Pack pack();
        public abstract int getY();
        public abstract int getHeight();
    }
}
