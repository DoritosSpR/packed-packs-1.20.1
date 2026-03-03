package io.github.fishstiz.packed_packs.gui.components.pack;

import io.github.fishstiz.fidgetz.gui.components.AbstractFixedListWidget;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import java.util.List;
import java.util.Collections;

public abstract class PackList extends AbstractFixedListWidget<PackList.Entry> {
    protected final int itemHeight;

    public PackList(int width, int height, int top, int bottom, int itemHeight) {
        super(width, height, top, bottom, itemHeight);
        this.itemHeight = itemHeight;
    }

    // Dentro de tu clase PackList, asegúrate de que Entry tenga esto:
public abstract class Entry extends ContainerObjectSelectionList.Entry<Entry> {
    // ... (constructor y métodos anteriores)

    public abstract net.minecraft.server.packs.repository.Pack pack();

    public boolean canOperateFile() {
        return true; // O tu lógica de permisos
    }

    // Para 1.20.1, el sistema de widgets es diferente. 
    // Si usas MixinExtras o una librería personalizada, asegúrate de que este método exista:
    public <T extends net.minecraft.client.gui.components.events.GuiEventListener & net.minecraft.client.gui.components.Renderable> T addWidget(T widget) {
        // Lógica para añadir el botón de hamburguesa/carpeta al renderizado de la línea
        return widget;
    }
}

        @Override
        public abstract void render(GuiGraphics g, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean isHovered, float partialTick);

        @Override
        public List<? extends GuiEventListener> children() {
            return Collections.emptyList();
        }

        // Métodos de compatibilidad para que otros archivos encuentren las coordenadas
        public int getX() { return PackList.this.getRowLeft(); }
        public int getY() { return PackList.this.getRowTop(this.index); }
        public int getWidth() { return PackList.this.getRowWidth(); }
        public int getHeight() { return PackList.this.itemHeight; }
        public int getBottom() { return this.getY() + this.getHeight(); }
    }
}
