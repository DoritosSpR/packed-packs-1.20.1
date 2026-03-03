package io.github.fishstiz.fidgetz.gui.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

public class FidgetzText<E> extends AbstractWidget implements Fidgetz {
    private final boolean shadow;
    private final int offsetY;

    private FidgetzText(Builder<E> builder) {
        super(builder.x, builder.y, builder.width, builder.height, builder.message);
        this.shadow = builder.shadow;
        this.offsetY = builder.offsetY;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        Font font = Minecraft.getInstance().font;
        int color = this.active ? 0xFFFFFF : 0xA0A0A0;
        guiGraphics.drawString(font, this.getMessage(), this.getX(), this.getY() + offsetY, color, shadow);
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narration) {}

    public static <E> Builder<E> builder() {
        return new Builder<>();
    }

    public static class Builder<E> extends AbstractWidgetBuilder<Builder<E>> {
        protected boolean shadow = true;
        protected int offsetY = 0;

        public Builder<E> setShadow(boolean shadow) { this.shadow = shadow; return self(); }
        public Builder<E> setOffsetY(int offsetY) { this.offsetY = offsetY; return self(); }

        public FidgetzText<E> build() {
            return new FidgetzText<>(this);
        }
    }
}
