package io.github.fishstiz.fidgetz.gui.components.contextmenu;

import net.minecraft.network.chat.Component;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ContextMenuItemBuilder {
    private final List<MenuItem> items = new ArrayList<>();
    private final Component title;

    public ContextMenuItemBuilder(Component title) {
        this.title = title;
    }

    public ContextMenuItemBuilder() {
        this(Component.empty());
    }

    public ContextMenuItemBuilder add(MenuItem item) {
        this.items.add(item);
        return this;
    }

    public ContextMenuItemBuilder icon(Supplier<io.github.fishstiz.fidgetz.gui.renderables.sprites.Sprite> icon) {
        // Implementación de icono para el menú si es necesario
        return this;
    }

    public ContextMenuItemBuilder separator() {
        // En muchos sistemas de UI, un separador es un item con texto vacío o nulo
        this.items.add(new MenuItem(Component.literal("---"), null, null, null, true, false, List.of(), null));
        return this;
    }

    public List<MenuItem> build() {
        return this.items;
    }

    public boolean isEmpty() {
        return this.items.isEmpty();
    }

    // Método de utilidad para el error de "separator()"
    private ContextMenuItemBuilder self() {
        return this;
    }
}
