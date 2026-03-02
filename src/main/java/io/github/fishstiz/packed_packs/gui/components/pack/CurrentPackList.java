package io.github.fishstiz.packed_packs.gui.components.pack;

import io.github.fishstiz.fidgetz.gui.renderables.ColoredRect;
import io.github.fishstiz.fidgetz.gui.renderables.GradientRect;
import io.github.fishstiz.fidgetz.util.GuiUtil;
import io.github.fishstiz.packed_packs.gui.components.MouseSelectionHandler;
import io.github.fishstiz.packed_packs.gui.components.SelectionContext;
import io.github.fishstiz.packed_packs.gui.components.events.DragEvent;
import io.github.fishstiz.packed_packs.gui.components.events.PackListEventListener;
import io.github.fishstiz.packed_packs.pack.PackAssetManager;
import io.github.fishstiz.packed_packs.pack.PackFileOperations;
import io.github.fishstiz.packed_packs.pack.PackOptionsContext;
import io.github.fishstiz.packed_packs.util.constants.Theme;
import io.github.fishstiz.packed_packs.gui.components.events.MoveEvent;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.repository.Pack;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;
import java.util.function.*;

import static io.github.fishstiz.fidgetz.util.GuiUtil.playClickSound;
import static io.github.fishstiz.packed_packs.util.InputUtil.*;
import static io.github.fishstiz.packed_packs.util.constants.GuiConstants.*;
import static io.github.fishstiz.fidgetz.util.lang.ObjectsUtil.*;

public class CurrentPackList extends PackList {
    // En 1.20.1 usamos ResourceLocation en lugar de Sprite
    private static final ResourceLocation UNSELECT_HIGHLIGHTED_SPRITE = new ResourceLocation("minecraft", "transferable_list/unselect_highlighted");
    private static final ResourceLocation UNSELECT_SPRITE = new ResourceLocation("minecraft", "transferable_list/unselect");
    private static final ResourceLocation MOVE_UP_HIGHLIGHTED_SPRITE = new ResourceLocation("minecraft", "transferable_list/move_up_highlighted");
    private static final ResourceLocation MOVE_UP_SPRITE = new ResourceLocation("minecraft", "transferable_list/move_up");
    private static final ResourceLocation MOVE_DOWN_HIGHLIGHTED_SPRITE = new ResourceLocation("minecraft", "transferable_list/move_down_highlighted");
    private static final ResourceLocation MOVE_DOWN_SPRITE = new ResourceLocation("minecraft", "transferable_list/move_down");
    
    private static final Theme DROP_THEME = Theme.GREEN_500;
    private static final ColoredRect DROP_INDEX = new ColoredRect(DROP_THEME.getARGB());
    private static final GradientRect SCROLL_UP = GradientRect.
