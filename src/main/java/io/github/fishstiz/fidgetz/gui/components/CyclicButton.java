package io.github.fishstiz.fidgetz.gui.components;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import java.util.List;

public class CyclicButton<T> extends FidgetzButton<Void> {
    protected final List<T> options;
    protected int index;

    protected CyclicButton(Builder<T, ?> builder) {
        super(builder);
        this.options = builder.options;
        this.index = builder.index;
    }

    public static <T> Builder<T, ?> builder(List<T> options) {
        return new Builder<>(options);
    }

    public static class Builder<T, B extends Builder<T, B>> extends FidgetzButton.Builder<Void, B> {
        protected List<T> options;
        protected int index = 0;

        public Builder(List<T> options) {
            this.options = options;
        }

        public B index(int index) {
            this.index = index;
            return self();
        }

        @Override
        public CyclicButton<T> build() {
            return new CyclicButton<>(this);
        }
    }

    public interface SpriteOption {
        ResourceLocation getSprite();
    }
}
