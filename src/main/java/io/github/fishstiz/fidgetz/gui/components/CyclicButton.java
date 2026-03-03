package io.github.fishstiz.fidgetz.gui.components;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import io.github.fishstiz.fidgetz.gui.renderables.sprites.Sprite;

import java.util.List;

public class CyclicButton<T> extends Button {
    private final List<T> values;
    private int index;

    public CyclicButton(int x, int y, int width, int height, Component message, OnPress onPress, List<T> values) {
        super(x, y, width, height, message, onPress, DEFAULT_NARRATION);
        this.values = values;
        this.index = 0;
    }

    public T getValue() {
        return values.get(index);
    }

    public void next() {
        index = (index + 1) % values.size();
    }

    private boolean hasCustomSprite() {
        return this.getValue() instanceof Sprite;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderWidget(guiGraphics, mouseX, mouseY, partialTick);
        
        if (hasCustomSprite()) {
            Sprite sprite = (Sprite) getValue();
            sprite.render(guiGraphics, getX() + 2, getY() + 2, width - 4, height - 4);
        }
    }
}
