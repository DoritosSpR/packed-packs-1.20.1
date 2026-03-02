package io.github.fishstiz.packed_packs.gui.components.pack;

import io.github.fishstiz.fidgetz.gui.components.*;
import io.github.fishstiz.fidgetz.gui.components.contextmenu.ContextMenuContainer;
import io.github.fishstiz.fidgetz.gui.renderables.ColoredRect;
import io.github.fishstiz.packed_packs.api.events.ScreenEvent;
import io.github.fishstiz.packed_packs.config.Config;
import io.github.fishstiz.packed_packs.config.Preferences;
import io.github.fishstiz.packed_packs.gui.components.MouseSelectionHandler;
import io.github.fishstiz.packed_packs.gui.components.SelectionContext;
import io.github.fishstiz.packed_packs.gui.components.events.PackListEventListener;
import io.github.fishstiz.packed_packs.gui.history.Restorable;
import io.github.fishstiz.packed_packs.gui.components.ToggleableHelper;
import io.github.fishstiz.packed_packs.pack.PackAssetManager;
import io.github.fishstiz.packed_packs.pack.PackFileOperations;
import io.github.fishstiz.packed_packs.pack.PackOptionsContext;
import io.github.fishstiz.packed_packs.transform.mixin.gui.AbstractSelectionListAccessor;
import io.github.fishstiz.packed_packs.util.PackUtil;
import io.github.fishstiz.packed_packs.util.constants.GuiConstants;
import io.github.fishstiz.packed_packs.util.constants.Theme;
import io.github.fishstiz.packed_packs.gui.components.events.*;
import io.github.fishstiz.packed_packs.pack.folder.FolderPack;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.Pack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import static io.github.fishstiz.fidgetz.util.GuiUtil.playClickSound;
import static io.github.fishstiz.packed_packs.util.InputUtil.*;
import static io.github.fishstiz.fidgetz.util.lang.ObjectsUtil.*;

public abstract class PackList extends AbstractFixedListWidget<PackList.Entry> implements
        Restorable<PackList.Snapshot>,
        ContainerEventHandlerPatch,
        ContextMenuContainer {
    protected static final int OFFSET_Y = 2;
    protected static final int ITEM_HEIGHT = 32;
    protected static final int ROW_GAP = 3;
    protected final PackOptionsContext options;
    protected final PackAssetManager assets;
    protected final PackListModel list;
    private final PackFileOperations fileOps;
    private final PackListEventListener listener;

    protected PackList(PackOptionsContext options, PackAssetManager assets, PackFileOperations fileOps, PackListEventListener listener) {
        super(ITEM_HEIGHT, DEFAULT_SCROLLBAR_OFFSET, OFFSET_Y, ROW_GAP);
        this.options = options;
        this.assets = assets;
        this.fileOps = fileOps;
        this.listener = listener;
        this.list = new PackListModel(this.options);
    }

    protected abstract @NotNull Entry createEntry(SelectionContext<Pack> context, int index);

    public @Nullable Entry getEntry(@Nullable Pack pack) {
        if (pack == null) return null;
        for (Entry entry : this.children()) {
            if (Objects.equals(entry.pack(), pack)) return entry;
        }
        return null;
    }

    private void refreshEntries() {
        Entry focused = this.getFocused();
        List<Pack> selection = this.list.getSelection();
        List<Pack> visiblePacks = this.list.getVisibleItems();

        this.clearEntries();
        for (int i = 0; i < visiblePacks.size(); i++) {
            Entry entry = this.createEntry(new SelectionContext<>(selection, visiblePacks.get(i)), i);
            this.addEntry(entry);
            listener.postApiEvent(new ScreenEvent.InitPackEntry(listener.ctx(), entry));
        }

        this.clampScrollAmount();
        this.setFocused(mapOrNull(focused, f -> this.getEntry(f.pack())));
    }

    protected void refreshList() {
        this.list.refresh();
        this.refreshEntries();
    }

    public void scrollToTop() {
        this.setScrollAmount(0);
    }

    public void reload(Collection<Pack> packs) {
        this.list.replaceAll(packs);
        this.setFocused(null);
        this.refresh();
    }

    public @NotNull List<Pack> copyPacks() {
        return List.copyOf(this.list.getItems());
    }

    public List<Pack> getOrderedSelection() {
        return this.list.getOrderedSelection();
    }

    public void clearSelection() {
        this.list.clearSelection();
    }

    private void refresh() {
        this.clearSelection();
        this.refreshList();
        this.scrollToTop();
    }

    public void sort(Query.SortOption sort) {
        if (this.list.sort(sort)) {
            this.refresh();
        }
    }

    public void hideIncompatible(boolean hideIncompatible) {
        if (this.list.hideIncompatible(hideIncompatible)) {
            this.clearSelection();
            this.refreshList();
        }
    }

    public void search(@NotNull String search) {
        if (this.list.search(search)) {
            this.refresh();
        }
    }

    public boolean isQueried() {
        return this.list.isQueried();
    }

    public void addAll(List<Pack> packs) {
        for (Pack pack : packs) {
            this.list.add(pack);
        }
        this.refreshList();
    }

    public void addOrMove(Pack pack, int to) {
        this.list.insertOrMove(to, pack);
        this.list.select(pack);
    }

    public boolean moveAll(List<Pack> selection, int to) {
        if (this.list.moveAll(to, selection)) {
            this.refreshList();
            return true;
        }
        return false;
    }

    private boolean removePack(Pack pack) {
        Entry focused = this.getFocused();
        if (this.list.remove(pack)) {
            if (focused != null && focused.pack().getId().equals(pack.getId())) {
                this.setFocused(null);
            }
            return true;
        }
        return false;
    }

    public void remove(Pack pack) {
        this.removePack(pack);
        this.refreshList();
    }

    public void removeAll(List<Pack> packs) {
        boolean removed = false;
        for (Pack pack : packs) {
            removed |= this.removePack(pack);
        }
        if (removed) {
            this.refreshList();
        }
    }

    public @Nullable Pack getLastSelected() {
        return this.list.getLastSelected();
    }

    @Override
    public @Nullable Entry getSelected() {
        return this.getLastSelected() != null ? this.getEntry(this.getLastSelected()) : super.getSelected();
    }

    public boolean isSelected(Pack pack) {
        return this.list.isSelected(pack);
    }

    public void scrollToLastSelected() {
        ifPresent(this.getEntry(this.getLastSelected()), this::ensureVisible);
    }

    public void unselect(Pack pack) {
        this.list.unselect(pack);
        Entry entry = this.getEntry(pack);
        if (entry == this.getFocused()) this.setFocused(null);
        if (entry == this.getSelected()) this.setSelected(null);
    }

    public void select(Pack pack) {
        if (this.list.select(pack)) {
            Entry entry = this.getEntry(pack);
            this.setFocused(entry);
            this.setSelected(entry);
        }
    }

    public void selectAll() {
        this.list.getVisibleItems().forEach(this::select);
    }

    public void selectAll(List<Pack> packs) {
        packs.forEach(this::select);
    }

    public void selectExclusive(Pack pack) {
        this.clearSelection();
        this.select(pack);
    }

    public void selectToggle(Pack pack) {
        if (this.isSelected(pack)) this.unselect(pack);
        else this.select(pack);
    }

    public void selectRange(Pack pack) {
        this.list.selectRange(pack);
        this.select(pack);
    }

    public boolean isTransferable(Pack pack) {
        return testNullable(this.getEntry(pack), PackList.Entry::isTransferable);
    }

    public void transferAll() {
        List<Pack> payload = new ArrayList<>();
        List<Pack> visiblePacks = this.list.getVisibleItems();
        for (int i = visiblePacks.size() - 1; i >= 0; i--) {
            Pack pack = visiblePacks.get(i);
            if (this.isTransferable(pack)) payload.add(pack);
        }
        if (!payload.isEmpty()) {
            this.sendEvent(new RequestTransferEvent(this, this.getLastSelected(), payload));
        }
    }

    protected void sendEvent(PackListEvent event) {
        this.listener.onEvent(event);
    }

    public abstract boolean canInteract(PackList source);
    protected abstract boolean canDrop(DragEvent dragEvent, double mouseX, double mouseY);
    protected abstract List<Pack> handleDrop(DragEvent dragEvent, double mouseX, double mouseY);
    public abstract void renderDroppableZone(GuiGraphics guiGraphics, DragEvent dragEvent, int mouseX, int mouseY, float partialTick);

    public final void drop(DragEvent dragEvent, double mouseX, double mouseY) {
        if (this.options.isLocked()) return;
        List<Pack> dropped = this.handleDrop(dragEvent, mouseX, mouseY);
        if (!dropped.isEmpty()) {
            if (dragEvent.target() != this) {
                this.sendEvent(new DropEvent(dragEvent.target(), this, dropped));
            } else {
                this.sendEvent(new MoveEvent(this, dragEvent.trigger(), dropped));
            }
        }
    }

    protected void openFolder(FolderPack folderPack) {
        this.sendEvent(new FolderOpenEvent(this, folderPack));
    }

    private @Nullable ComponentPath handleArrowNavigation(FocusNavigationEvent.ArrowNavigation arrowNavigation) {
        Entry entry = switch (arrowNavigation.direction()) {
            case UP -> this.getPreviousEntry();
            case DOWN -> this.getNextEntry();
            default -> null;
        };
        if (entry != null) {
            if (isRangeModifierActive()) this.selectRange(entry.pack());
            else this.selectExclusive(entry.pack());
            this.sendEvent(new SelectionEvent(this));
            this.ensureVisible(entry);
            return ComponentPath.path(entry, this);
        }
        this.setFocused(null);
        return null;
    }

    @Override
    public @Nullable ComponentPath nextFocusPath(FocusNavigationEvent event) {
        if (!this.isFocused()) {
            Pack lastSelected = this.getLastSelected();
            Entry entry = (lastSelected != null) ? this.getEntry(lastSelected) : (!this.children().isEmpty() ? this.children().get(0) : null);
            if (entry != null) {
                this.select(entry.pack());
                this.ensureVisible(entry);
                return ComponentPath.path(entry, this);
            }
        } else if (event instanceof FocusNavigationEvent.ArrowNavigation arrowNavigation) {
            return this.handleArrowNavigation(arrowNavigation);
        }
        return null;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        Entry entry = this.getEntry(this.getLastSelected());
        if (isExpandFolder(keyCode, modifiers) && entry != null && entry.folderWidget != null && this.list.getSelection().size() == 1) {
            this.openFolder(entry.folderWidget.getMetadata());
            return true;
        }
        if (isTransfer(keyCode, modifiers)) {
            if (entry != null && entry.transfer()) playClickSound();
            return entry != null;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.isValidMouseClick(button)) this.updateScrollingState(mouseX, mouseY, button);
        if (!this.isMouseOver(mouseX, mouseY)) return false;
        return ContainerEventHandlerPatch.super.mouseClickedAt(mouseX, mouseY, button) || ((AbstractSelectionListAccessor) this).packed_packs$scrolling();
    }

    @Override
    protected void renderListItems(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.renderListItems(guiGraphics, mouseX, mouseY, partialTick);
        Entry focused = this.getFocused();
        if (focused != null && focused.isFocused() && this.children().contains(focused)) {
            guiGraphics.renderOutline(focused.getX(), focused.getY() - Entry.BACKGROUND_OFFSET, focused.getWidth(), focused.getHeight() + Entry.BACKGROUND_OFFSET * 2, Theme.WHITE.getARGB());
        }
    }

    public Snapshot captureState(String eventName) { return new Snapshot(this); }
    public void replaceState(@NotNull Snapshot snapshot) {
        snapshot.model.restore(this.list);
        this.refreshEntries();
        this.setFocused(this.getEntry(snapshot.focused));
        this.setSelected(this.getEntry(snapshot.selected));
    }

    public record Snapshot(PackList target, @Nullable Pack focused, @Nullable Pack selected, PackListModel.Snapshot model) implements Restorable.Snapshot<Snapshot> {
        public Snapshot(PackList target) { this(target, extractPack(target.getFocused()), extractPack(target.getSelected()), target.list.captureState()); }
    }

    private static @Nullable Pack extractPack(@Nullable Entry entry) { return mapOrNull(entry, Entry::pack); }

    // --- CLASE INTERNA ENTRY ---
    public abstract class Entry extends AbstractFixedListWidget<Entry>.Entry implements ContextMenuContainer {
        private static final Tooltip FOLDER_OPEN_INFO = Tooltip.create(Component.translatable("pack.folder.open"));
        protected static final int SPACING = 2;
        protected static final int BACKGROUND_OFFSET = 1;
        protected static final ColoredRect SELECTED_OVERLAY = new ColoredRect(Theme.BLUE_500.withAlpha(0.25F));
        protected final SelectionContext<Pack> context;
        private final List<GuiEventListener> children = new ObjectArrayList<>();
        private final List<Renderable> renderables = new ObjectArrayList<>();
        private final List<Renderable> topRenderables = new ObjectArrayList<>();
        private final List<NarratableEntry> narratables = new ObjectArrayList<>();
        private final MouseSelectionHandler<Pack> selectionHandler;
        protected final PackWidget packWidget;
        protected FidgetzButton<FolderPack> folderWidget;
        private boolean stale = false;

        protected Entry(SelectionContext<Pack> context, int index) {
            super(index);
            this.context = context;
            this.selectionHandler = new MouseSelectionHandler<>(this, context);
            
            // Iniciar carga del icono
            PackList.this.assets.getOrLoadIcon(this.pack(), icon -> {});

            this.packWidget = this.addRenderableWidget(new PackWidget(
                    this.pack(), PackList.this.assets, this.getX(), PackList.this.getRowTop(this.index),
                    this.getWidth(), PackList.this.itemHeight, SPACING
            ));

            if (this.pack() instanceof FolderPack folder && (Config.get().isDevMode() || Preferences.INSTANCE.folderPackWidget.get())) {
                this.folderWidget = this.addTopRenderableOnly(this.prependWidget(
                        ToggleableHelper.applyPref(Preferences.INSTANCE.folderPackWidget, FidgetzButton.<FolderPack>builder())
                                .setTooltip(FOLDER_OPEN_INFO).setHeight(this.packWidget.getHeight() / 3).makeSquare()
                                .setSprite(GuiConstants.HAMBURGER_SPRITE).setMetadata(folder).setOnPress(this::openFolder).build()
                ));
            }
        }

        public Pack pack() { return this.context.item(); }
        public boolean isTransferable() { return !PackList.this.options.isLocked() && !this.isStale(); }
        public boolean isSelected() { return this.context.isSelected(); }
        public boolean isSelectedLast() { return this.context.isSelectedLast(); }

        public <U extends GuiEventListener & Renderable> U addRenderableWidget(U widget) {
            this.children.add(widget); this.renderables.add(widget);
            if (widget instanceof NarratableEntry n) this.narratables.add(n);
            return widget;
        }

        public <U extends GuiEventListener> U prependWidget(U widget) {
            this.children.add(0, widget);
            if (widget instanceof NarratableEntry n) this.narratables.add(n);
            return widget;
        }

        public <U extends Renderable> U addTopRenderableOnly(U renderable) { this.topRenderables.add(renderable); return renderable; }

        public boolean transfer() {
            if (!this.isSelected() && this.isTransferable()) {
                PackList.this.sendEvent(new RequestTransferEvent(PackList.this, this.pack()));
                return true;
            }
            List<Pack> payload = new ArrayList<>();
            List<Pack> ordered = PackList.this.getOrderedSelection();
            for (int i = ordered.size() - 1; i >= 0; i--) {
                if (PackList.this.isTransferable(ordered.get(i))) payload.add(ordered.get(i));
            }
            if (!payload.isEmpty()) {
                PackList.this.sendEvent(new RequestTransferEvent(PackList.this, this.isTransferable() ? this.pack() : null, payload));
                return true;
            }
            return false;
        }

        protected boolean handleMouseAction(MouseSelectionHandler.Action action) {
            if (!action.shouldDispatch() || this.isStale()) return false;
            switch (action) {
                case SELECT -> PackList.this.select(this.pack());
                case SELECT_TOGGLE -> PackList.this.selectToggle(this.pack());
                case SELECT_EXCLUSIVE -> PackList.this.selectExclusive(this.pack());
                case SELECT_RANGE -> PackList.this.selectRange(this.pack());
                case TRANSFER -> this.transfer();
                case DRAG -> {
                    List<Pack> sel = new ArrayList<>(PackList.this.getOrderedSelection());
                    Collections.reverse(sel);
                    PackList.this.sendEvent(new DragEvent(PackList.this, sel, this.pack()));
                }
            }
            if (action.shouldSelect()) PackList.this.sendEvent(new SelectionEvent(PackList.this));
            return true;
        }

        @Override
        public boolean mouseClicked(double x, double y, int b) {
            if (super.mouseClicked(x, y, b)) return false;
            return this.handleMouseAction(this.selectionHandler.mouseClicked(x, y, b));
        }

        @Override public boolean mouseReleased(double x, double y, int b) { return this.handleMouseAction(this.selectionHandler.mouseReleased(x, y, b)); }
        @Override public boolean mouseDragged(double x, double y, int b, double dx, double dy) { return this.handleMouseAction(this.selectionHandler.mouseDragged(x, y, b, dx, dy)); }

        @Override
        public void render(GuiGraphics g, int i, int t, int l, int w, int h, int mx, int my, boolean hover, float pt) {
            if (!this.pack().getCompatibility().isCompatible() && !PackList.this.options.getUserConfig().isIncompatibleWarningsHidden()) {
                g.fill(this.getX() + BACKGROUND_OFFSET, this.getY(), this.getX() + this.getWidth() - BACKGROUND_OFFSET, this.getBottom(), Theme.RED_900.getARGB());
            }
            super.render(g, i, t, l, w, h, mx, my, hover, pt);
            if (this.isSelected()) {
                pick(isSelectedLast(), GuiConstants.WHITE_OVERLAY, SELECTED_OVERLAY).render(g, this.getX() + BACKGROUND_OFFSET, this.getY(), this.getWidth() - BACKGROUND_OFFSET * 2, this.getHeight());
            }
            if (this.isSelected() || this.isFocused()) {
                if (!this.isFocused()) g.renderOutline(l, this.getY() - BACKGROUND_OFFSET, w, h + BACKGROUND_OFFSET * 2, Theme.BLUE_500.getARGB());
            }
            this.renderForeground(g, t, l, w, h, mx, my, hover, pt);
            if (this.folderWidget != null) this.folderWidget.setPosition(this.packWidget.getContentLeft(), this.getBottom() - this.folderWidget.getHeight() - BACKGROUND_OFFSET);
            for (Renderable r : this.topRenderables) r.render(g, mx, my, pt);
        }

        protected abstract void renderForeground(GuiGraphics g, int t, int l, int w, int h, int mx, int my, boolean hover, float pt);
        @Override public List<? extends GuiEventListener> children() { return this.children; }
        @Override public List<? extends NarratableEntry> narratables() { return this.narratables; }
        public boolean isStale() { return this.stale; }
    }
}
