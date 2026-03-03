package io.github.fishstiz.fidgetz.gui.components;

import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.Layout;

public abstract class ToggleableDialog<T extends ToggleableDialog<T>> {
    private float zIndex = 0;
    private boolean captureClick = true;
    private boolean captureFocus = true;

    public float getZ() { return zIndex; }
    public void setZ(float z) { this.zIndex = z; }

    public boolean isCaptureClick() { return captureClick; }
    public boolean isCaptureFocus() { return captureFocus; }

    public boolean encloses(GuiEventListener child) {
        return false;
    }

    // AÑADIR ESTA CLASE INTERNA
    public static abstract class Builder<L extends Layout, B extends Builder<L, B>> {
        protected float zIndex = 0;

        @SuppressWarnings("unchecked")
        public B setZ(float z) {
            this.zIndex = z;
            return (B) this;
        }

        // Aquí irían los métodos comunes para construir diálogos
    }
}
