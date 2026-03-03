package io.github.fishstiz.fidgetz.gui.components;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class FidgetzButton<E> extends Button implements Fidgetz {
    protected ResourceLocation sprite;

    protected FidgetzButton(Builder<E, ?> builder) {
        super(builder.x, builder.y, builder.width, builder.height, builder.message, builder.onPress, DEFAULT_NARRATION);
        this.sprite = builder.sprite;
    }

    @Override
    public int getX() { return super.getX(); }

    @Override
    public int getY() { return super.getY(); }

    @Override
    public int getWidth() { return this.width; }

    @Override
    public int getHeight() { return this.height; }

    public static <E> Builder<E, Builder<E, ?>> builder() {
        return new Builder<>();
    }

    // T representa el tipo del Builder para permitir el encadenamiento (method chaining)
    public static class Builder<E, T extends Builder<E, T>> extends AbstractWidgetBuilder<T> {
        protected OnPress onPress = (btn) -> {};
        protected ResourceLocation sprite;

        public Builder() {
            this.width = 150;
            this.height = 20;
        }

        public T onPress(OnPress onPress) {
            this.onPress = onPress;
            return self();
        }

        public T message(Component message) {
            this.message = message;
            return self();
        }

        public T setSprite(ResourceLocation sprite) {
            this.sprite = sprite;
            return self();
        }

        public T makeSquare() {
            this.width = this.height;
            return self();
        }

        @SuppressWarnings("unchecked")
        public FidgetzButton<E> build() {
            return new FidgetzButton<>((Builder<E, ?>) this);
        }
    }
}
