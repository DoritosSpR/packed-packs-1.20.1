package io.github.fishstiz.fidgetz.gui.components;

import net.minecraft.client.gui.components.events.GuiEventListener;

public abstract class ToggleableDialog<T extends ToggleableDialog<T>> {
    private float zIndex = 0;
    private boolean captureClick = true;
    private boolean captureFocus = true;

    public float getZ() { return zIndex; }
    public void setZ(float z) { this.zIndex = z; }

    public boolean isCaptureClick() { return captureClick; }
    public boolean isCaptureFocus() { return captureFocus; }

    public boolean encloses(GuiEventListener child) {
        // Lógica para detectar si un elemento está dentro del diálogo
        return false;
    }
}
