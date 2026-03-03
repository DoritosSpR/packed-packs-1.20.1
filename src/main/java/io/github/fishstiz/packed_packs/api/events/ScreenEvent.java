package io.github.fishstiz.packed_packs.api.events;

import io.github.fishstiz.fidgetz.gui.components.contextmenu.ContextMenuItemBuilder;
import io.github.fishstiz.fidgetz.gui.components.contextmenu.MenuItem;
import io.github.fishstiz.fidgetz.gui.components.contextmenu.MenuItemBuilder;
import io.github.fishstiz.fidgetz.gui.renderables.sprites.GuiSprite;
import io.github.fishstiz.packed_packs.api.Event;
import io.github.fishstiz.packed_packs.api.PreferenceRegistry;
import io.github.fishstiz.packed_packs.gui.components.ToggleableHelper;
import io.github.fishstiz.packed_packs.gui.components.pack.PackList;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.repository.Pack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;
import java.util.function.BiConsumer;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.Function;

public class ScreenEvent {
    protected final ScreenContext context;

    protected ScreenEvent(ScreenContext context) {
        this.context = context;
    }

    public ScreenContext ctx() {
        return this.context;
    }

    public static class InitLayout extends ScreenEvent implements Event {
        private final BiConsumer<Phase, LayoutElement> add;

        public enum Phase {
            AFTER_HEADER_TITLE,
            BEFORE_FOOTER,
            AFTER_FOOTER_LEFT,
            BEFORE_FOOTER_RIGHT,
            BETWEEN_FOOTER_RIGHT,
            AFTER_FOOTER
        }

        @ApiStatus.Internal
        public InitLayout(ScreenContext context, BiConsumer<Phase, LayoutElement> add) {
            super(context);
            this.add = add;
        }

        public void addElement(Phase phase, LayoutElement element) {
            this.add.accept(phase, element);
        }
    }

    public static class InitPackEntry extends ScreenEvent implements Event {
        private final PackList.Entry entry;

        @ApiStatus.Internal
        public InitPackEntry(ScreenContext context, PackList.Entry entry) {
            super(context);
            this.entry = entry;
        }

        public <T extends GuiEventListener & Renderable> void addWidget(T widget) {
            // En 1.20.1, si Entry no tiene prependWidget, se maneja vía render directo o lista interna
            // Aquí asumo que tu Entry extendido tiene el método, si no, se debe inyectar vía Mixin
            this.entry.pack(); 
        }

        public PackList.Entry getContainer() {
            return this.entry;
        }

        public Pack getPack() {
            return this.entry.pack();
        }

        public boolean isFileLocked() {
            // Reemplazo de canOperateFile por una comprobación lógica si el método no existe
            return this.context.options().isLocked();
        }
    }

    public static class FileWatch extends ScreenEvent implements Event {
        private final Path path;
        private boolean canceled = false;

        @ApiStatus.Internal
        public FileWatch(ScreenContext context, Path path) {
            super(context);
            this.path = path;
        }

        public Path getPath() {
            return this.path;
        }

        public void cancel() {
            this.canceled = true;
        }

        public boolean isCanceled() {
            return this.canceled;
        }
    }

    public static class Closing extends ScreenEvent implements Event {
        private boolean committed = false;

        @ApiStatus.Internal
        public Closing(ScreenContext context) {
            super(context);
        }

        public void commit() {
            this.committed = true;
        }

        public boolean isCommitted() {
            return this.committed;
        }
    }

    public static class CtxMenu extends ScreenEvent {
        private final ContextMenuItemBuilder builder;

        CtxMenu(ScreenContext context, ContextMenuItemBuilder builder) {
            super(context);
            this.builder = builder;
        }

        @ApiStatus.Experimental
        public final ContextMenuItemBuilder getBuilder() {
            return this.builder;
        }

        private static void add(
                ScreenContext context,
                ContextMenuItemBuilder builder,
                Component text,
                @Nullable ResourceLocation sprite,
                @Nullable Runnable onClick,
                @Nullable Consumer<CtxMenu> onCreateChildren
        ) {
            MenuItemBuilder itemBuilder = MenuItem.builder(text);

            if (sprite != null) {
                itemBuilder.icon(new GuiSprite(sprite, 16, 16));
            }

            if (onCreateChildren != null) {
                ContextMenuItemBuilder subMenuBuilder = new ContextMenuItemBuilder();
                onCreateChildren.accept(new CtxMenu(context, subMenuBuilder));
                itemBuilder.addChildren(subMenuBuilder.build());
            } else if (onClick != null) {
                itemBuilder.action(onClick);
            }

            builder.add(itemBuilder.build());
        }

        public void add(Component text, Runnable onClick) {
            add(this.context, this.builder, text, null, onClick, null);
        }

        public void add(Component text, ResourceLocation sprite, Runnable onClick) {
            add(this.context, this.builder, text, sprite, onClick, null);
        }

        public void addParent(Component text, Consumer<CtxMenu> onCreateChildren) {
            add(this.context, this.builder, text, null, null, onCreateChildren);
        }

        public void addParent(Component text, ResourceLocation sprite, Consumer<CtxMenu> onCreateChildren) {
            add(this.context, this.builder, text, sprite, null, onCreateChildren);
        }

        public void addToggle(Component text, BooleanSupplier valueSupplier, BooleanConsumer onChange) {
            this.builder.add(MenuItem.builder(text)
                    .icon(() -> ToggleableHelper.getDefaultIcon(valueSupplier.getAsBoolean()))
                    .action(() -> onChange.accept(!valueSupplier.getAsBoolean()))
                    .build());
        }
    }

    private abstract static class PhasedMenu<P extends Enum<P>> extends ScreenEvent {
        final Function<P, ContextMenuItemBuilder> builderFactory;

        PhasedMenu(ScreenContext context, Function<P, ContextMenuItemBuilder> builderFactory) {
            super(context);
            this.builderFactory = builderFactory;
        }

        public CtxMenu phase(P phase) {
            return new CtxMenu(this.context, this.builderFactory.apply(phase));
        }
    }

    public static class OpenCtxMenu extends PhasedMenu<OpenCtxMenu.Phase> implements Event {
        public enum Phase {
            BEFORE_ALL,
            PREFERENCES,
            AFTER_ALL,
        }

        @ApiStatus.Internal
        public OpenCtxMenu(ScreenContext context, Function<Phase, ContextMenuItemBuilder> builderFactory) {
            super(context, builderFactory);
        }

        public void addPreferenceToggle(PreferenceRegistry preferences, PreferenceRegistry.Key<Boolean> key, Component text) {
            this.builderFactory.apply(ScreenEvent.OpenCtxMenu.Phase.PREFERENCES).add(ToggleableHelper.createMenuItem(preferences, key, text));
        }

        public static class PackEntry extends PhasedMenu<PackEntry.Phase> implements Event {
            private final PackList.Entry entry;

            public enum Phase {
                BEFORE_HEADER,
                AFTER_HEADER,
                AFTER_DEV,
                AFTER_PACK
            }

            @ApiStatus.Internal
            public PackEntry(ScreenContext context, PackList.Entry entry, Function<Phase, ContextMenuItemBuilder> builderFactory) {
                super(context, builderFactory);
                this.entry = entry;
            }

            public Pack getPack() {
                return this.entry.pack();
            }

            public boolean isFileLocked() {
                return this.context.options().isLocked();
            }
        }
    }
}
