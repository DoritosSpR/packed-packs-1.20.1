package io.github.fishstiz.packed_packs.gui.components.pack;

import com.mojang.blaze3d.platform.InputConstants;
import io.github.fishstiz.fidgetz.gui.components.*;
import io.github.fishstiz.fidgetz.gui.layouts.FlexLayout;
import io.github.fishstiz.fidgetz.util.DrawUtil;
import io.github.fishstiz.packed_packs.gui.components.events.FileRenameCloseEvent;
import io.github.fishstiz.packed_packs.gui.components.events.FileRenameEvent;
import io.github.fishstiz.packed_packs.gui.components.events.PackListEventListener;
import io.github.fishstiz.packed_packs.pack.PackAssetManager;
import io.github.fishstiz.packed_packs.pack.PackFileOperations;
import io.github.fishstiz.packed_packs.util.PackUtil;
import io.github.fishstiz.packed_packs.util.ToastUtil;
import net.minecraft.client.gui.ComponentPath;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.navigation.FocusNavigationEvent;
import net.minecraft.client.gui.navigation.ScreenAxis;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.Pack;
import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.regex.Pattern;

import static io.github.fishstiz.packed_packs.util.PackUtil.ZIP_PACK_EXTENSION;
import static io.github.fishstiz.packed_packs.util.constants.GuiConstants.CROSS_SPRITE;
import static io.github.fishstiz.packed_packs.util.constants.GuiConstants.SPACING;

public class FileRenameModal extends Modal<LinearLayout> {
    private static final int MAX_LENGTH = 255;
    private static final int CONTENT_WIDTH = 256;
    private static final int SHADOW_SIZE = 24;
    private static final Pattern ILLEGAL_CHAR_PATTERN = Pattern.compile(".*[<>:\"/\\\\|?*].*");
    
    private final RenderableRectWidget sprite;
    private final FidgetzText title;
    private final ToggleableEditBox nameEditor;
    private final FidgetzButton saveButton;
    private final PackFileOperations fileOps;
    private final PackAssetManager assets;
    private PackList packList;
    private Pack pack;
    private String oldName;

    public <S extends Screen & ToggleableDialogContainer & PackListEventListener> FileRenameModal(
            S screen,
            PackFileOperations fileOps,
            PackAssetManager assets
    ) {
        super(Modal.builder(screen, LinearLayout.vertical().spacing(SPACING)).padding(SPACING));
        this.fileOps = fileOps;
        this.assets = assets;

        // Corregido: Eliminados los <Void> que causaban error de parámetros en el Builder
        this.sprite = RenderableRectWidget.builder(PackAssetManager.DEFAULT_ICON)
                .makeSquare()
                .build();
        
        this.title = FidgetzText.builder()
                .setOffsetY(1)
                .setShadow(true)
                .build();

        FidgetzButton closeButton = FidgetzButton.builder()
                .makeSquare()
                .onPress(btn -> this.closeModal())
                .setSprite(CROSS_SPRITE)
                .build();

        this.nameEditor = ToggleableEditBox.builder()
                .setWidth(CONTENT_WIDTH)
                .setEditable(true)
                .addListener(this::handleChange)
                .setMaxLength(MAX_LENGTH)
                .setFilter(this::testInput)
                .build();

        FidgetzButton cancelButton = FidgetzButton.builder()
                .onPress(btn -> this.closeModal())
                .message(CommonComponents.GUI_CANCEL)
                .build();

        this.saveButton = FidgetzButton.builder()
                .onPress(btn -> this.saveName())
                .message(CommonComponents.GUI_DONE)
                .build();

        FlexLayout titleRow = FlexLayout.horizontal(this::getContentWidth).spacing(SPACING);
        titleRow.addChild(this.sprite);
        titleRow.addFlexChild(this.title);
        titleRow.addChild(closeButton);

        FlexLayout buttonRow = FlexLayout.horizontal(this::getContentWidth).spacing(SPACING);
        buttonRow.addFlexChild(cancelButton);
        buttonRow.addFlexChild(this.saveButton);

        this.root().addChild(titleRow);
        this.root().addChild(this.nameEditor);
        this.root().addChild(buttonRow);
    }

    private int getContentWidth() { return CONTENT_WIDTH; }

    public void open(PackList packList, Pack pack) {
        this.packList = packList;
        this.pack = pack;
        this.sprite.setRenderableRect(this.assets.getIcon(pack));
        this.title.setMessage(pack.getTitle());

        this.oldName = sanitizeNameForEdit(pack);
        this.nameEditor.setValue(this.oldName);
        this.nameEditor.setSuggestion(PackUtil.isZipPack(pack) ? ZIP_PACK_EXTENSION : null);
        this.saveButton.active = false;

        this.repositionElements();
        this.setOpen(true);
    }

    private void handleChange(String name) {
        this.saveButton.active = this.canSave(name);
    }

    private boolean testInput(String input) {
        if (input == null || (!input.isEmpty() && input.isBlank())) return false;
        return testIllegalChars(input);
    }

    private boolean canSave(String input) {
        if (input == null || input.isBlank() || this.pack == null) return false;
        String trimmed = input.trim();
        return !Objects.equals(this.oldName, trimmed) && testIllegalChars(input);
    }

    private void saveName() {
        String newName = this.nameEditor.getValue();
        if (!this.canSave(newName)) return;

        String sanitizedName = sanitizeNameForSave(this.pack, newName);
        if (this.fileOps.renamePack(this.pack, sanitizedName)) {
            Component nameText = Component.literal(sanitizedName);
            if (this.packList != null) {
                PackList.Entry entry = this.packList.getEntry(this.pack);
                if (entry != null) entry.onRename(nameText);
            }
            ((PackListEventListener) this.screen).onEvent(new FileRenameEvent(this.packList, this.pack, nameText));
            this.closeModal();
        } else {
            ToastUtil.onFileFailToast(ToastUtil.getRenameFailText(pack.getTitle().getString(), newName));
        }
    }

    private static String sanitizeNameForEdit(Pack pack) {
        String name = pack.getTitle().getString();
        return PackUtil.isZipPack(pack) ? name.replaceFirst(Pattern.quote(ZIP_PACK_EXTENSION) + "$", "") : name;
    }

    private static String sanitizeNameForSave(Pack pack, String newName) {
        newName = FilenameUtils.getName(newName).trim();
        return PackUtil.isZipPack(pack) ? newName + ZIP_PACK_EXTENSION : newName;
    }

    private static boolean testIllegalChars(@NotNull String input) {
        input = input.trim();
        return input.equals(FilenameUtils.getName(input)) && !ILLEGAL_CHAR_PATTERN.matcher(input).matches();
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (!this.isOpen()) return;
        DrawUtil.renderDropShadow(guiGraphics, root().getX(), root().getY(), root().getWidth(), root().getHeight(), SHADOW_SIZE);
        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.isOpen() && keyCode == InputConstants.KEY_RETURN && this.canSave(this.nameEditor.getValue())) {
            this.saveName();
            return true;
        }
        return super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public @Nullable ComponentPath nextFocusPath(FocusNavigationEvent event) {
        if (this.nameEditor.isFocused() && event instanceof FocusNavigationEvent.ArrowNavigation arrowNav) {
            if (arrowNav.direction().getAxis() == ScreenAxis.HORIZONTAL) {
                return ComponentPath.path(this.nameEditor, this);
            }
        }
        return super.nextFocusPath(event);
    }
}
