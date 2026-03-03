package io.github.fishstiz.fidgetz.gui.components;

import io.github.fishstiz.fidgetz.gui.Metadata;
import io.github.fishstiz.fidgetz.gui.renderables.RenderableRect;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.CommonComponents;

public class RenderableRectWidget<E> extends AbstractWidget implements Metadata<E>, Fidgetz {
    protected RenderableRect renderableRect;
    protected E metadata;

    protected RenderableRectWidget(Builder<E> builder) {
        super(builder.x, builder.y, builder.width, builder.height, CommonComponents.EMPTY);
        this.renderableRect = builder.renderableRect;
        this.metadata = builder.metadata;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (this.renderableRect != null) {
            this.renderableRect.render(guiGraphics, this.getX(), this.getY(), this.getWidth(), this.getHeight(), partialTick);
        }
    }

    public void setRenderableRect(RenderableRect renderableRect) {
        this.renderableRect = renderableRect;
    }

    @Override public int getX() { return super.getX(); }
    @Override public int getY() { return super.getY(); }
    @Override public int getWidth() { return this.width; }
    @Override public int getHeight() { return this.height; }

    @Override public E getMetadata() { return this.metadata; }
    @Override public void setMetadata(E metadata) { this.metadata = metadata; }

    @Override protected void updateWidgetNarration(NarrationElementOutput narration) {}

    public static <E> Builder<E> builder(RenderableRect renderableRect) {
        return new Builder<>(renderableRect);
    }

    public static class Builder<E> extends AbstractWidgetBuilder<Builder<E>> {
        protected final RenderableRect renderableRect;
        protected E metadata;

        public Builder(RenderableRect renderableRect) {
            this.renderableRect = renderableRect;
            this.width = 16;
            this.height = 16;
        }

        public Builder<E> makeSquare() {
            int size = Math.max(this.width, this.height);
            this.width = size;
            this.height = size;
            return self();
        }

        public Builder<E> setMetadata(E metadata) {
            this.metadata = metadata;
            return self();
        }

        public RenderableRectWidget<E> build() {
            return new RenderableRectWidget<>(this);
        }
    }
}
