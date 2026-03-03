package io.github.fishstiz.fidgetz.gui.components;

import io.github.fishstiz.fidgetz.gui.shapes.Padding;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.layouts.Layout;
import net.minecraft.client.gui.screens.Screen;

public class Modal<T extends Layout> extends ToggleableDialog<LayoutWrapper<T>> {
    private static final int MIN_SIZE = 50;

    protected Modal(Builder<T> builder) {
        super(builder);
        this.root().setPadding(builder.padding);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (!isOpen()) return;
        // Renderizar fondo y luego el layout
        this.root().visitWidgets(widget -> widget.render(guiGraphics, mouseX, mouseY, partialTick));
    }

    public void repositionElements() {
        this.root().arrangeElements();
        this.root().setX(this.screen.width / 2 - this.root().getWidth() / 2);
        this.root().setY(this.screen.height / 2 - this.root().getHeight() / 2);
    }

    public void closeModal() { this.setOpen(false); }

    public static <S extends Screen & ToggleableDialogContainer, T extends Layout> Builder<T> builder(S screen, T layout) {
        return new Builder<>(screen, new LayoutWrapper<>(layout, MIN_SIZE, MIN_SIZE));
    }

    public static class Builder<T extends Layout> extends ToggleableDialog.Builder<LayoutWrapper<T>, Builder<T>> {
        protected Padding padding = Padding.empty();

        public Builder(Screen screen, LayoutWrapper<T> root) {
            super(screen, root);
        }

        public Builder<T> padding(Padding padding) {
            this.padding = padding;
            return this;
        }

        @Override
        public Modal<T> build() {
            return new Modal<>(this);
        }
    }
}
