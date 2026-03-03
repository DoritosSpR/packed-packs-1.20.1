package io.github.fishstiz.packed_packs.gui.components.pack;

import io.github.fishstiz.fidgetz.gui.components.AbstractFixedListWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.server.packs.repository.Pack;

public abstract class PackList extends AbstractFixedListWidget<PackList.Entry> {
    protected final int itemHeight;

    public PackList(int width, int height, int top, int bottom, int itemHeight) {
        super(width, height, top, bottom, itemHeight);
        this.itemHeight = itemHeight;
    }

    public Entry getEntry(Pack pack) {
        for (Entry entry : this.children()) {
            if (entry.pack().equals(pack)) return entry;
        }
        return null;
    }

    public int getRowIndex(double mouseY) {
        // Usamos getY() en lugar de this.top
        return (int)((mouseY - (double)this.getY() + this.getScrollAmount() - (double)this.headerHeight) / (double)this.itemHeight);
    }

    public abstract class Entry extends ContainerObjectSelectionList.Entry<Entry> {
        protected final int index;
        public Entry(int index) { this.index = index; }
        public abstract Pack pack();
        @Override public abstract void render(GuiGraphics g, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean isHovered, float partialTick);
    }
}
