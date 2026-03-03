package io.github.fishstiz.fidgetz.gui.components;

import io.github.fishstiz.fidgetz.transform.mixin.EditBoxAccess;
import io.github.fishstiz.fidgetz.gui.Metadata;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class ToggleableEditBox<E> extends EditBox implements Fidgetz, Metadata<E> {
    private final List<Consumer<String>> listeners;
    private E metadata;
    private int focusedTextColor;
    private int hintColor;

    private ToggleableEditBox(Builder<E> builder) {
        super(builder.font, builder.x, builder.y, builder.width, builder.height, builder.message);
        this.metadata = builder.metadata;
        this.listeners = builder.listeners;
        this.focusedTextColor = builder.textColor != null ? builder.textColor : 0xE0E0E0;
        this.hintColor = builder.hintColor != null ? builder.hintColor : 0x707070;

        this.setMaxLength(builder.maxLength);
        this.setValue(builder.value);
        this.setEditable(builder.editable);
        this.setHint(builder.hint);
        if (builder.filter != null) this.setFilter(builder.filter);
        
        this.setTextColor(focusedTextColor);
        super.setResponder(this::onRespond);
    }

    private void onRespond(String value) {
        for (Consumer<String> listener : listeners) {
            listener.accept(value);
        }
    }

    public void addListener(Consumer<String> listener) {
        this.listeners.add(listener);
    }

    public boolean isEditing() {
        return this.isEditable();
    }

    @Override
    public E getMetadata() { return this.metadata; }

    @Override
    public void setMetadata(E metadata) { this.metadata = metadata; }

    @Override
    public int getX() { return this.getX(); }
    @Override
    public int getY() { return this.getY(); }
    @Override
    public int getWidth() { return this.width; }
    @Override
    public int getHeight() { return this.height; }

    public static <E> Builder<E> builder(Font font) {
        return new Builder<>(font);
    }

    public static Builder<Void> builder() {
        return new Builder<>(Minecraft.getInstance().font);
    }

    public static class Builder<E> extends AbstractWidgetBuilder<Builder<E>> {
        private final Font font;
        private final List<Consumer<String>> listeners = new ObjectArrayList<>();
        private String value = "";
        private Component hint = CommonComponents.EMPTY;
        private boolean editable = false;
        private int maxLength = 32;
        private Predicate<String> filter;
        private Integer textColor;
        private Integer hintColor;
        private E metadata;

        public Builder(Font font) {
            this.font = font;
            this.height = 20; // Altura por defecto de un edit box
        }

        public Builder<E> setValue(String value) { this.value = value; return self(); }
        public Builder<E> setHint(Component hint) { this.hint = hint; return self(); }
        public Builder<E> setEditable(boolean editable) { this.editable = editable; return self(); }
        public Builder<E> setMaxLength(int maxLength) { this.maxLength = maxLength; return self(); }
        public Builder<E> setFilter(Predicate<String> filter) { this.filter = filter; return self(); }
        public Builder<E> addListener(Consumer<String> listener) { this.listeners.add(listener); return self(); }
        public Builder<E> setTextColor(int color) { this.textColor = color; return self(); }
        public Builder<E> setMetadata(E metadata) { this.metadata = metadata; return self(); }

        public ToggleableEditBox<E> build() {
            return new ToggleableEditBox<>(this);
        }
    }
}
