package io.github.fishstiz.fidgetz.gui.components;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class FidgetzButton<E> extends Button implements Fidgetz {
    protected ResourceLocation sprite;

    protected FidgetzButton(Builder<E> builder) {
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

    // El método estático para obtener el builder
    public static <E> Builder<E> builder() {
        return new Builder<>();
    }

    public static class Builder<E> extends AbstractWidgetBuilder<Builder<E>> {
        protected OnPress onPress = (btn) -> {};
        protected ResourceLocation sprite;

        public Builder() {
            this.width = 150; // Ancho por defecto de un botón de Minecraft
            this.height = 20;
        }

        public Builder<E> onPress(OnPress onPress) {
            this.onPress = onPress;
            return self();
        }

        public Builder<E> message(Component message) {
            this.message = message;
            return self();
        }

        public Builder<E> setSprite(ResourceLocation sprite) {
            this.sprite = sprite;
            return self();
        }

        /**
         * Ajusta el tamaño para que sea un cuadrado basado en la altura.
         * Útil para botones de iconos (como la 'X' de cerrar).
         */
        public Builder<E> makeSquare() {
            this.width = this.height;
            return self();
        }

        public FidgetzButton<E> build() {
            return new FidgetzButton<>(this);
        }
    }
}
