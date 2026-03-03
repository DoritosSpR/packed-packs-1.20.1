package io.github.fishstiz.packed_packs.gui.components.pack;

import io.github.fishstiz.packed_packs.gui.components.events.PackListEventListener;
import io.github.fishstiz.packed_packs.pack.PackAssetManager;
import io.github.fishstiz.packed_packs.pack.PackFileOperations;
import io.github.fishstiz.packed_packs.pack.PackOptionsContext;
import net.minecraft.server.packs.repository.Pack;
import java.util.Collections;
import java.util.List;

public class CurrentPackList extends PackList {
    private boolean scrolling;

    public CurrentPackList(PackOptionsContext options, PackAssetManager assets, PackFileOperations fileOps, PackListEventListener listener) {
        super(options, assets, fileOps, listener);
    }

    // Corregido: método de selección en 1.20.1
    public Pack getSelectedPack() {
        Entry entry = this.getSelected();
        return entry != null ? entry.pack() : null;
    }

    public void setScroll(double scrollAmount) {
        this.setScrollAmount(scrollAmount);
    }

    protected int getRowIndex(double mouseY) {
        return (int)((mouseY - (double)this.getY() + this.getScrollAmount() - (double)this.headerHeight) / (double)this.itemHeight);
    }

    private int toPackIndex(int dropIndex) {
        return dropIndex; // Lógica de mapeo simplificada para 1.20.1
    }

    private int getDropIndex(double mouseY) {
        int index = this.getRowIndex(mouseY);
        return index >= 0 ? index : this.children().size();
    }

    public boolean canInteract(PackList source) {
        return !this.options.isLocked();
    }

    public boolean isTransferable(Pack pack) {
        return !this.options.isRequired(pack);
    }

    public List<Pack> handleDrop(PackList source, List<Pack> payload, double mouseX, double mouseY) {
        if (this.scrolling || this.options.isLocked() || payload.isEmpty()) {
            return Collections.emptyList();
        }

        int dropIndex = this.getDropIndex(mouseY);
        
        for (Pack selected : payload) {
            if (!this.isTransferable(selected)) continue;
            // Aquí iría la lógica de movimiento en el repositorio
            this.listener.onAccept(selected); 
        }

        return payload;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.scrolling = button == 0 && mouseX >= (double)this.getScrollbarPosition();
        return super.mouseClicked(mouseX, mouseY, button);
    }
}
