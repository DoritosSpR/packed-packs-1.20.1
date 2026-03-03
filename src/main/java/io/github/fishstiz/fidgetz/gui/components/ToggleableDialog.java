package io.github.fishstiz.fidgetz.gui.components;

import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.Layout;
import net.minecraft.client.gui.screens.Screen;

public abstract class ToggleableDialog<L extends Layout> implements Renderable, GuiEventListener {
    protected final Screen screen;
    protected final L root;
    private boolean open = false;
    protected int background = 0;

    protected ToggleableDialog(Builder<L, ?> builder) {
        this.screen = builder.screen;
        this.root = builder.root;
        this.background = builder.background;
    }

    public L root() { return root; }
    public boolean isOpen() { return open; }
    public void setOpen(boolean open) { this.open = open; }

    public static abstract class Builder<L extends Layout, B extends Builder<L, B>> {
        protected final Screen screen;
        protected final L root;
        protected int background = 0;

        public Builder(Screen screen, L root) {
            this.screen = screen;
            this.root = root;
        }

        @SuppressWarnings("unchecked")
        public B background(int color) {
            this.background = color;
            return (B) this;
        }

        public abstract ToggleableDialog<L> build();
    }
}
