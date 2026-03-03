package io.github.fishstiz.fidgetz.gui.components;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import java.util.List;

public class CyclicButton<T, E> extends FidgetzButton<E> {
    
    public interface SpriteOption {
        Component getMessage();
        ResourceLocation getSprite();
    }

    protected CyclicButton(Builder<T, E> builder) {
        super(builder);
    }

    // El error principal era que el código buscaba esta interfaz dentro de CyclicButton
    public interface CyclicValue {
        Component getMessage();
    }

    public static class Builder<T, E> extends FidgetzButton.Builder<E> {
        // Implementación mínima para que AvailablePacksLayout compile
        public CyclicButton<T, E> build() {
            return new CyclicButton<>(this);
        }
    }
}
