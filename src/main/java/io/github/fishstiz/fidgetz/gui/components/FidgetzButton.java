package io.github.fishstiz.fidgetz.gui.components;

import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public abstract class FidgetzButton<E> extends Button implements Fidgetz {
    
    protected FidgetzButton(int x, int y, int width, int height, Component message, OnPress onPress, CreateNarration createNarration) {
        super(x, y, width, height, message, onPress, createNarration);
    }

    // Definición correcta del Builder genérico para que las subclases no fallen
    public static abstract class Builder<E, T extends Builder<E, T>> {
        protected Component message = Component.empty();
        protected OnPress onPress = (btn) -> {};

        @SuppressWarnings("unchecked")
        public T message(Component message) {
            this.message = message;
            return (T) this;
        }

        @SuppressWarnings("unchecked")
        public T onPress(OnPress onPress) {
            this.onPress = onPress;
            return (T) this;
        }

        public abstract FidgetzButton<E> build();
    }
}
