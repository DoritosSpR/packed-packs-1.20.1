package io.github.fishstiz.fidgetz.transform.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.fishstiz.fidgetz.gui.components.ToggleableDialogContainer;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.AbstractWidget;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Mixin({Screen.class, AbstractWidget.class})
public abstract class ContainerEventHandlerMixin implements ContainerEventHandler {

    /**
     * Usamos el nombre intermedio/mapeado para asegurar que Mixin lo encuentre en 1.20.1
     * Si "handleTabNavigation" falla, usamos el selector de parámetros explícito.
     */
    @WrapOperation(
        method = "handleTabNavigation(Lnet/minecraft/client/gui/navigation/ScreenDirection;)Z", 
        at = @At(
            value = "NEW",
            target = "(Ljava/util/Collection;)Ljava/util/ArrayList;"
        ),
        remap = true
    )
    private ArrayList<GuiEventListener> fidgetz$filterTab(Collection<GuiEventListener> children, Operation<ArrayList<GuiEventListener>> original) {
        if (this instanceof ToggleableDialogContainer dialog) {
            return fidgetz$filter(dialog, children);
        }
        return original.call(children);
    }

    @Unique
    private ArrayList<GuiEventListener> fidgetz$filter(ToggleableDialogContainer self, Collection<? extends GuiEventListener> children) {
        ArrayList<GuiEventListener> focusable = new ArrayList<>();
        for (GuiEventListener child : children) {
            // Filtramos los elementos que están cubiertos por un diálogo/modal
            if (!self.isChildCovered(child)) {
                focusable.add((GuiEventListener) child);
            }
        }
        return focusable;
    }
}
