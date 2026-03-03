package io.github.fishstiz.fidgetz.gui.components;

import io.github.fishstiz.fidgetz.gui.shapes.GuiRectangle;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public abstract class FidgetzButton<E> extends Button implements Fidgetz {
    
    protected FidgetzButton(int x, int y, int width, int height, Component message, OnPress onPress, CreateNarration createNarration) {
        super(x, y, width, height, message, onPress, createNarration);
    }

    @Override
    public int getX() { return this.getX(); }

    @Override
    public int getY() { return this.getY(); }

    @Override
    public int getWidth() { return this.width; }

    @Override
    public int getHeight() { return this.height; }

    public GuiRectangle getViewRectangle() {
        return new GuiRectangle(this.getX(), this.getY(), this.getWidth(), this.getHeight());
    }
}
