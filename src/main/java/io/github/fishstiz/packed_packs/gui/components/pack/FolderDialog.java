package io.github.fishstiz.packed_packs.gui.components.pack;

import io.github.fishstiz.fidgetz.gui.components.*;
import io.github.fishstiz.fidgetz.gui.components.contextmenu.ContextMenuContainer;
import io.github.fishstiz.fidgetz.gui.components.contextmenu.ContextMenuItemBuilder;
import io.github.fishstiz.fidgetz.gui.renderables.sprites.Sprite;
import io.github.fishstiz.fidgetz.gui.shapes.GuiRectangle;
import io.github.fishstiz.fidgetz.util.DrawUtil;
import io.github.fishstiz.packed_packs.gui.components.contextmenu.PackMenuHeader;
import io.github.fishstiz.packed_packs.gui.components.events.*;
import io.github.fishstiz.packed_packs.pack.PackAssetManager;
import io.github.fishstiz.packed_packs.pack.PackFileOperations;
import io.github.fishstiz.packed_packs.pack.PackOptionsContext;
import io.github.fishstiz.packed_packs.pack.FolderPack;
import io.github.fishstiz.packed_packs.transform.interfaces.FilePack;
import io.github.fishstiz.packed_packs.util.PackUtil;
import io.github.fishstiz.fidgetz.util.lang.ObjectsUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import static io.github.fishstiz.packed_packs.util.constants.GuiConstants.*;

public class FolderDialog extends ToggleableDialog<FolderPackList> implements ContextMenuContainer {
    private static final Component BACK_TEXT = CommonComponents.GUI_BACK.copy().append(CommonComponents.ELLIPSIS);
    private static final int HEADER_HEIGHT = 16;
    
    private final FidgetzButton<Void> closeButton;
    private final FidgetzText<Void> folderTitle;
    private final PackFileOperations fileOps;
    private final PackListEventListener listener;
    
    private Sprite folderSprite = PackAssetManager.DEFAULT_FOLDER_ICON;
    private PackList parent;
    private FolderPack folderPack;

    public <S extends Screen & ToggleableDialogContainer & PackListEventListener> FolderDialog(
            S screen,
            PackOptionsContext options,
            PackAssetManager assets,
            PackFileOperations fileOps
    ) {
        // Corrección del Builder: Se asegura el tipo S para el screen
        super(ToggleableDialog.<FolderPackList, S>builder(screen, new FolderPackList(options, assets, fileOps, screen))
                .setBackground(DrawUtil.DEMO_BACKGROUND)
                .build());

        this.listener = screen;
        this.fileOps = fileOps;
        
        // Inicialización de widgets manual
        this.closeButton = FidgetzButton.<Void>builder()
                .setOnPress(() -> this.setOpen(false)) // Simplificado para cerrar
                .makeSquare(CROSS_SPRITE.width)
                .spriteOnly()
                .build();
                
        this.folderTitle = FidgetzText.<Void>builder()
                .setHeight(CROSS_SPRITE.height)
                .setOffsetY(1)
                .setShadow(true)
                .alignLeft()
                .build();

        // Registro de widgets para que reciban clicks y se rendericen
        this.addRenderableWidget(this.closeButton);
        this.addRenderableWidget(this.folderTitle);

        this.getContent().visible = false;
        
        // Listener de apertura/cierre
        this.addToggleListener(open -> {
            this.getContent().visible = open;
            if (!open && this.folderPack != null) {
                this.sendEvent(new FolderCloseEvent(this.getContent(), this.folderPack));
            }
        });
    }

    /**
     * En Fidgetz moderno, root() suele ser getContent()
     */
    public FolderPackList root() {
        return this.getContent();
    }

    private void updateBounds() {
        GuiRectangle bounds = this.getBounds(); // Fidgetz usa getBounds() habitualmente
        int left = bounds.getX() + SPACING;
        int top = bounds.getY() + SPACING;

        // Reposicionamiento del contenido
        this.root().setX(left);
        this.root().setY(top + HEADER_HEIGHT + SPACING);
        this.root().setWidth(bounds.getWidth() - (SPACING * 2));
        this.root().setHeight(bounds.getBottom() - SPACING - this.root().getY());
        
        this.closeButton.setX(left);
        this.closeButton.setY(top);
        
        this.folderTitle.setX(left + this.closeButton.getWidth() + SPACING);
        this.folderTitle.setY(top);
        this.folderTitle.setWidth(bounds.getRight() - this.folderTitle.getX() - SPACING);
    }

    public void updateFolder(PackList parent, FolderPack folderPack, PackAssetManager assets) {
        this.parent = parent;
        this.folderPack = folderPack;
        this.folderTitle.setMessage(folderPack.getTitle());
        assets.getOrLoadIcon(folderPack, icon -> this.folderSprite = icon);

        // Alineamos el diálogo con la lista padre
        this.setX(parent.getX());
        this.setY(parent.getY());
        this.setWidth(parent.getWidth());
        this.setHeight(parent.getHeight());
        
        this.updateBounds();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (!this.isOpen()) return;
        this.updateBounds();
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    protected void renderForeground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int left = this.closeButton.getX();
        int top = this.closeButton.getY();
        
        // Render del icono de la carpeta
        this.folderSprite.render(guiGraphics, left, top, CROSS_SPRITE.width, CROSS_SPRITE.height);

        if (this.closeButton.isMouseOver(mouseX, mouseY)) {
            WHITE_OVERLAY.render(guiGraphics, left, top, CROSS_SPRITE.width, CROSS_SPRITE.height);
            CROSS_SPRITE.render(guiGraphics, left, top);
        }
    }

    @Override
    public void buildItems(ContextMenuItemBuilder builder, int mouseX, int mouseY) {
        if (this.folderPack == null || !this.isOpen()) return;

        builder.add(new PackMenuHeader(this.folderPack, this.folderSprite))
               .simpleItem(BACK_TEXT, () -> this.setOpen(false));

        // Solo mostrar opciones de carpeta si NO hay un pack bajo el ratón
        if (this.root().getEntryAt(mouseX, mouseY) == null) {
            builder.separator();
            
            if (this.canOperateFolder()) {
                builder.simpleItem(RENAME_FILE_TEXT, this::renameDirectory)
                       .simpleItem(DELETE_FILE_TEXT, this::deleteDirectory);
            }
            
            builder.simpleItem(OPEN_FILE_TEXT, () -> PackUtil.openPack(this.folderPack))
                   .simpleItem(OPEN_PARENT_TEXT, () -> PackUtil.openParent(this.folderPack));
        }
    }

    private boolean canOperateFolder() {
        return this.folderPack != null && this.fileOps.isOperable(this.folderPack);
    }

    private void renameDirectory() {
        if (this.folderPack != null) {
            this.sendEvent(new FileRenameOpenEvent(this.root(), this.folderPack));
        }
    }

    private void deleteDirectory() {
        if (this.folderPack != null && this.fileOps.deletePack(this.folderPack)) {
            this.setOpen(false);
            if (this.parent != null) {
                this.parent.remove(this.folderPack);
            }
            this.sendEvent(new FileDeleteEvent(this.root()));
        }
    }

    private void sendEvent(PackListEvent event) {
        this.listener.onEvent(event);
    }

    // Cumplir con la interfaz de Minecraft
    @Override
    public void setFocused(@Nullable GuiEventListener listener) {
        super.setFocused(listener);
    }
}
