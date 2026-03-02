package io.github.fishstiz.fidgetz.gui.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.util.Mth; // Importante para clamp

public abstract class AbstractFixedListWidget<E extends ContainerObjectSelectionList.Entry<E>> extends ContainerObjectSelectionList<E> {
    
    public AbstractFixedListWidget(int width, int height, int top, int bottom, int itemHeight) {
        // CORRECCIÓN 1.20.1: El constructor requiere 6 parámetros
        // Minecraft, width, height, top, bottom, itemHeight
        super(Minecraft.getInstance(), width, height, top, bottom, itemHeight);
    }

    // Reemplaza Math.clamp por Mth.clamp en tus métodos de scroll
    protected void clampScrollAmount() {
        this.setScrollAmount(Mth.clamp(this.getScrollAmount(), 0.0D, (double)this.getMaxScroll()));
    }

    // En 1.20.1 usa getY() y getX() si están disponibles, 
    // si no, usa las variables 'left', 'top', 'width', 'height' de la clase padre.
}
