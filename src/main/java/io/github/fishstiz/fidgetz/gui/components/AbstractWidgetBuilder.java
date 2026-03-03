package io.github.fishstiz.fidgetz.gui.components;

import net.minecraft.network.chat.Component;

public abstract class AbstractWidgetBuilder<B extends AbstractWidgetBuilder<B>> {
    protected int x;
    protected int y;
    protected int width;
    protected int height;
    protected Component message = Component.empty();

    @SuppressWarnings("unchecked")
    protected B self() {
        return (B) this;
    }

    public B setX(int x) {
        this.x = x;
        return self();
    }

    public B setY(int y) {
        this.y = y;
        return self();
    }

    public B setPosition(int x, int y) {
        this.x = x;
        this.y = y;
        return self();
    }

    public B setWidth(int width) {
        this.width = width;
        return self();
    }

    public B setHeight(int height) {
        this.height = height;
        return self();
    }

    public B setSize(int width, int height) {
        this.width = width;
        this.height = height;
        return self();
    }

    public B setMessage(Component message) {
        this.message = message;
        return self();
    }
}
