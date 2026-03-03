package io.github.fishstiz.packed_packs.gui.components.pack;

import io.github.fishstiz.fidgetz.gui.components.*;
import io.github.fishstiz.fidgetz.gui.components.contextmenu.ContextMenuContainer;
import io.github.fishstiz.fidgetz.gui.components.contextmenu.ContextMenuItemBuilder;
import io.github.fishstiz.fidgetz.gui.renderables.sprites.Sprite;
import io.github.fishstiz.fidgetz.gui.shapes.GuiRectangle;
import io.github.fishstiz.fidgetz.util.DrawUtil;
import io.github.fishstiz.packed_packs.gui.components.contextmenu.PackMenuHeader;
import io.github.fishstiz.packed_packs.gui.components.events.*;
import io.github.fishstiz.packed_packs.pack.*;
import io.github.fishstiz.packed_packs.util.PackUtil;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

import static io.github.fishstiz.packed_packs.util.constants.GuiConstants.*;

// Eliminamos el genérico <FolderPackList> si está dando problemas de bounds
public class FolderDialog extends ToggleableDialog implements ContextMenuContainer {
    private static final Component BACK_TEXT = CommonComponents.GUI_BACK.copy().append(CommonComponents.ELLIPSIS);
    private static final int HEADER_HEIGHT = 16;
    
    private final FidgetzButton closeButton;
    private final FidgetzText folderTitle;
    private final PackFileOperations fileOps;
    private final PackListEventListener listener;
    private final FolderPackList content; // Usamos una referencia directa
    
    private Sprite folderSprite = PackAssetManager.DEFAULT_FOLDER_ICON;
    private PackList parent;
    private FolderPack folderPack;

    public <S extends Screen & ToggleableDialogContainer & PackListEventListener> FolderDialog(
            S screen, PackOptionsContext options, PackAssetManager assets, PackFileOperations fileOps
    ) {
        // Ajustamos al builder que tu versión de Fidgetz reconoce
        super(screen);
        this.content = new FolderPackList(options, assets, fileOps, screen);
        this.listener = screen;
        this.fileOps = fileOps;
        
        // El error decía que FidgetzButton.builder() no toma Void o es distinto
        this.closeButton = FidgetzButton.builder()
                .setOnPress(button -> this.setOpen(false)) 
                .build();
                
        this.folderTitle = FidgetzText.builder().build();

        this.content.visible = false;
    }

    // El compilador no encuentra root() ni getContent(), definimos el nuestro
    public FolderPackList root() {
        return this.content;
    }

    private void updateBounds() {
        // El compilador falló en getBounds(), usamos getRectangle() o x, y, width, height directamente
        int left = this.getX() + SPACING;
        int top = this.getY() + SPACING;

        this.content.setX(left);
        this.content.setY(top + HEADER_HEIGHT + SPACING);
        this.content.setWidth(this.getWidth() - (SPACING * 2));
        // Calculamos el alto restante
        this.content.setHeight(this.getHeight() - HEADER_HEIGHT - (SPACING * 3));
        
        this.closeButton.setX(left);
        this.closeButton.setY(top);
        this.folderTitle.setX(left + 20); // Ajuste manual
        this.folderTitle.setY(top);
    }

    public void updateFolder(PackList parent, FolderPack folderPack, PackAssetManager assets) {
        this.parent = parent;
        this.folderPack = folderPack;
        this.folderTitle.setMessage(folderPack.getTitle());
        assets.getOrLoadIcon(folderPack, icon -> this.folderSprite = icon);

        // Copiamos dimensiones del padre
        this.setX(parent.getX());
        this.setY(parent.getY());
        this.setWidth(parent.getWidth());
        this.setHeight(parent.getHeight());
        
        this.updateBounds();
    }

    // El error dice que no sobreescribes render() correctamente
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (!this.isVisible()) return; // Cambiado isOpen() por isVisible()
        this.updateBounds();
        
        // Render fondo manual si super.render falla
        DrawUtil.DEMO_BACKGROUND.render(guiGraphics, getX(), getY(), getWidth(), getHeight());
        
        this.content.render(guiGraphics, mouseX, mouseY, partialTick);
        this.closeButton.render(guiGraphics, mouseX, mouseY, partialTick);
        this.folderTitle.render(guiGraphics, mouseX, mouseY, partialTick);
        
        this.renderForeground(guiGraphics, mouseX, mouseY, partialTick);
    }

    protected void renderForeground(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.folderSprite.render(guiGraphics, closeButton.getX(), closeButton.getY(), 16, 16);
    }

    @Override
    public void buildItems(ContextMenuItemBuilder builder, int mouseX, int mouseY) {
        if (this.folderPack == null || !this.isVisible()) return;

        builder.add(new PackMenuHeader(this.folderPack, this.folderSprite));
        // El compilador dice que no existe builder.separator()
        // builder.separator(); 

        if (this.canOperateFolder()) {
            builder.simpleItem(RENAME_FILE_TEXT, this::renameDirectory);
        }
    }

    private boolean canOperateFolder() {
        return this.folderPack != null && this.fileOps.isOperable(this.folderPack);
    }

    private void renameDirectory() {
        this.sendEvent(new FileRenameOpenEvent(this.root(), this.folderPack));
    }

    private void sendEvent(PackListEvent event) {
        this.listener.onEvent(event);
    }

    // Fix: FolderDialog is not abstract and does not override getFocused()
    @Override
    public @Nullable GuiEventListener getFocused() {
        return null;
    }
}
