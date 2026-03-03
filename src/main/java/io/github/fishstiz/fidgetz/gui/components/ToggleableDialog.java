package io.github.fishstiz.fidgetz.gui.components;

import io.github.fishstiz.fidgetz.gui.shapes.GuiRectangle;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class ToggleableDialog<T extends LayoutElement> implements ContainerEventHandler {
    protected final Screen screen;
    protected final T root;
    protected GuiRectangle boundingBox;
    protected boolean autoClose = true;
    private boolean focused;
    private final List<GuiEventListener> children = new ArrayList<>();
    private GuiEventListener lastFocused;

    protected ToggleableDialog(Screen screen, T root) {
        this.screen = screen;
        this.root = root;
        this.boundingBox = GuiRectangle.viewOf(root);
    }

    @Override
    public List<? extends GuiEventListener> children() {
        return this.children;
    }

    @Override
    public boolean isDragging() {
        return false;
    }

    @Override
    public void setDragging(boolean dragging) {
    }

    @Override
    public @Nullable GuiEventListener getFocused() {
        return this.lastFocused;
    }

    @Override
    public void setFocused(@Nullable GuiEventListener listener) {
        this.lastFocused = listener;
    }

    public void render(GuiGraphics g, int mouseX, int mouseY, float partialTick) {
        // Lógica de renderizado del root si es necesario
    }

    public void setBoundingBox(GuiRectangle box) {
        this.boundingBox = box;
    }

    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (this.boundingBox.containsPoint(mouseX, mouseY)) {
            return ContainerEventHandler.super.mouseClicked(mouseX, mouseY, button);
        }
        if (this.autoClose) {
            this.onClose();
        }
        return false;
    }

    public void onClose() {
        if (this.screen != null) {
            // En 1.20.1 usamos setFocused(null) porque clearFocus() es privado
            this.screen.setFocused(null);
        }
    }

    // --- ELIMINADOS MÉTODOS DE 1.20.2 (nextFocusPath, etc.) ---
    // En 1.20.1 la navegación se maneja por defecto mediante ContainerEventHandler
}
