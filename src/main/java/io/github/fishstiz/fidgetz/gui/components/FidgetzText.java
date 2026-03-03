package io.github.fishstiz.fidgetz.gui.components;

import io.github.fishstiz.fidgetz.gui.Metadata;
import io.github.fishstiz.fidgetz.gui.shapes.GuiRectangle;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.network.chat.Component;

public class FidgetzText<E> extends StringWidget implements Fidgetz, Metadata<E> {
    private E metadata;

    public FidgetzText(int x, int y, int width, int height, Component message, net.minecraft.client.gui.Font font) {
        super(x, y, width, height, message, font);
    }

    @Override
    public GuiRectangle getViewRectangle() {
        return new GuiRectangle(this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override public E getMetadata() { return metadata; }
    @Override public void setMetadata(E metadata) { this.metadata = metadata; }
}
