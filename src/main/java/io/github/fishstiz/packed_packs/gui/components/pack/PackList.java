package io.github.fishstiz.packed_packs.gui.components.pack;

import io.github.fishstiz.fidgetz.gui.components.AbstractFixedListWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.server.packs.repository.Pack;
import java.util.List;
import java.util.Collections;

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
        return (int)((mouseY - (double)this.top + this.getScrollAmount() - (double)this.headerHeight) / (double)this.itemHeight);
    }

    public abstract class Entry extends ContainerObjectSelectionList.Entry<Entry> {
        protected final int index;

        public Entry(int index) {
            this.index = index;
        }

        public abstract Pack pack();

        public boolean canOperateFile() {
            return true;
        }

        @Override
        public abstract void render(GuiGraphics g, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean isHovered, float partialTick);

        @Override
        public List<? extends GuiEventListener> children() {
            return Collections.emptyList();
        }

        public int getX() { return PackList.this.getRowLeft(); }
        public int getY() { return PackList.this.getRowTop(this.index); }
        public int getWidth() { return PackList.this.getRowWidth(); }
        public int getHeight() { return PackList.this.itemHeight; }
        public int getBottom() { return this.getY() + this.getHeight(); }
    }
}
