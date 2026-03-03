package io.github.fishstiz.packed_packs.gui.components.pack;

import io.github.fishstiz.fidgetz.gui.renderables.ColoredRect;
import io.github.fishstiz.fidgetz.gui.renderables.sprites.Sprite;
import io.github.fishstiz.fidgetz.util.GuiUtil;
import io.github.fishstiz.packed_packs.gui.components.SelectionContext;
import io.github.fishstiz.packed_packs.gui.components.events.DragEvent;
import io.github.fishstiz.packed_packs.gui.components.events.PackListEventListener;
import io.github.fishstiz.packed_packs.pack.PackAssetManager;
import io.github.fishstiz.packed_packs.pack.PackFileOperations;
import io.github.fishstiz.packed_packs.pack.PackOptionsContext;
import io.github.fishstiz.packed_packs.util.constants.GuiConstants;
import io.github.fishstiz.packed_packs.util.constants.Theme;
import io.github.fishstiz.packed_packs.gui.components.pack.PackListDevMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.server.packs.repository.Pack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static io.github.fishstiz.fidgetz.util.GuiUtil.playClickSound;
import static io.github.fishstiz.packed_packs.util.InputUtil.isLeftClick;
import static io.github.fishstiz.packed_packs.util.ResourceUtil.getGuiSprite;
import static io.github.fishstiz.fidgetz.util.lang.ObjectsUtil.ifPresent;
import static io.github.fishstiz.fidgetz.util.lang.ObjectsUtil.pick;

public class AvailablePackList extends PackList {
    private static final Sprite SELECT_HIGHLIGHTED_SPRITE = Sprite.of32(getGuiSprite("transferable_list/select_highlighted"));
    private static final Sprite SELECT_SPRITE = Sprite.of32(getGuiSprite("transferable_list/select"));
    private static final int SPACING = 2;

    public AvailablePackList(PackOptionsContext options, PackAssetManager assets, PackFileOperations fileOps, PackListEventListener listener) {
        super(options, assets, fileOps, listener);
    }

    @Override
    public boolean isTransferable(Pack pack) { return true; } // Lógica simplificada para ejemplo

    @Override public void removeAll(List<Pack> packs) {}
    @Override public void addAll(List<Pack> packs) {}
    @Override public void select(Pack pack) {}
    @Override public void selectAll(List<Pack> packs) {}
    @Override public void clearSelection() {}
    @Override public Entry getEntry(Pack pack) { return null; }

    @Override
    public boolean canInteract(PackList source) {
        return source != this;
    }

    private boolean isInvalidDrop(PackList source, List<Pack> payload, Pack trigger) {
        return source == null || !source.canInteract(this) || payload.isEmpty();
    }

    // Métodos para manejar el Drag & Drop (ajustar según tu sistema de eventos)
    public boolean canDrop(DragEvent dragEvent, double mouseX, double mouseY) {
        return this.isMouseOver(mouseX, mouseY) && !this.isInvalidDrop(dragEvent.target(), dragEvent.payload(), dragEvent.trigger());
    }

    public void renderDroppableZone(GuiGraphics guiGraphics, DragEvent dragEvent, int mouseX, int mouseY, float partialTick) {
        if (this.isInvalidDrop(dragEvent.target(), dragEvent.payload(), dragEvent.trigger())) return;

        int width = this.scrollbarVisible() ? this.getWidth() - this.scrollbarOffset : this.getWidth();
        if (this.isMouseOver(mouseX, mouseY)) {
            guiGraphics.fill(this.getX(), this.getY(), this.getX() + width, this.getY() + this.getHeight(), 0x40FF0000);
        }
        guiGraphics.renderOutline(this.getX(), this.getY(), width, this.getHeight(), 0xFFFF0000);
    }

    public class Entry extends PackList.Entry {
        private final SelectionContext<Pack> context;
        private final int index;

        public Entry(SelectionContext<Pack> context, int index) {
            this.context = context;
            this.index = index;
        }

        @Override public Pack pack() { return context.item(); }
        @Override public boolean isSelectedLast() { return false; }
        @Override public boolean isTransferable() { return true; }
        @Override public void transfer() {}
        @Override protected void sendPacks(Pack trigger, List<Pack> required) {}

        public boolean isMouseOverSelect(double mouseX, double mouseY) {
            // En 1.20.1 se usa isMouseOver en lugar de isHovered()
            return AvailablePackList.this.isMouseOver(mouseX, mouseY) && 
                   GuiUtil.containsPoint(this.getX() + SPACING, this.getY(), SELECT_SPRITE.width, SELECT_SPRITE.height, (int)mouseX, (int)mouseY);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (isLeftClick(button) && this.isMouseOverSelect(mouseX, mouseY)) {
                playClickSound();
                this.transfer();
                return true;
            }
            return false;
        }

        @Override
        public void render(GuiGraphics guiGraphics, int index, int top, int left, int width, int height, int mouseX, int mouseY, boolean hovering, float partialTick) {
            if (!hovering && !this.isSelectedLast()) return;

            int x = left + SPACING;
            if (this.isTransferable()) {
                Sprite s = pick(!this.isMouseOverSelect(mouseX, mouseY), SELECT_SPRITE, SELECT_HIGHLIGHTED_SPRITE);
                s.render(guiGraphics, x, top);
            }
        }

        @Override
        protected void handleDevMenuEvent(PackListDevMenu.Event<?> event) {
            if (event instanceof PackListDevMenu.Event.Require requireEvent) {
                if (Boolean.TRUE.equals(requireEvent.value())) {
                    this.sendPacks(requireEvent.trigger(), requireEvent.required());
                }
            }
        }
    }
}
