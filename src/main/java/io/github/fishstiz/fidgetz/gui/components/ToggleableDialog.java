package io.github.fishstiz.fidgetz.gui.components;

import io.github.fishstiz.fidgetz.gui.shapes.GuiRectangle;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.screens.Screen;
import java.util.ArrayList;
import java.util.List;

public abstract class ToggleableDialog<T extends LayoutElement> implements ContainerEventHandler {
    protected final Screen screen;
    protected final T root;
    protected boolean open = false;
    protected GuiRectangle boundingBox;
    private GuiEventListener focused;

    protected ToggleableDialog(Screen screen, T root) {
        this.screen = screen;
        this.root = root;
        this.boundingBox = GuiRectangle.viewOf(root);
    }

    public boolean isOpen() { return open; }
    public void setOpen(boolean open) { this.open = open; }
    public T root() { return root; }

    @Override public List<? extends GuiEventListener> children() { return new ArrayList<>(); }
    @Override public boolean isDragging() { return false; }
    @Override public void setDragging(boolean d) {}
    @Override public GuiEventListener getFocused() { return focused; }
    @Override public void setFocused(GuiEventListener l) { this.focused = l; }

    public void onClose() { 
        this.setOpen(false);
        if (this.screen != null) this.screen.setFocused(null); 
    }

    // Clase Builder estática para compatibilidad con Modal y ContextMenu
    public static class Builder<T extends LayoutElement, B extends Builder<T, B>> {
        protected final Screen screen;
        protected final T root;
        public Builder(Screen s, T r) { this.screen = s; this.root = r; }
        @SuppressWarnings("unchecked")
        protected B self() { return (B) this; }
    }
}
