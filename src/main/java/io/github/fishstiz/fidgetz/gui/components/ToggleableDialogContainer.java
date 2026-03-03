package io.github.fishstiz.fidgetz.gui.components;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import java.util.List;
import java.util.stream.Collectors;

public interface ToggleableDialogContainer extends ContainerEventHandler {
    
    List<? extends ToggleableDialog<?>> getDialogs();

    default List<? extends ToggleableDialog<?>> getOpenDialogs() {
        return this.getDialogs().stream()
                .filter(dialog -> dialog.visible)
                .collect(Collectors.toCollection(ObjectArrayList::new));
    }

    private boolean isDescendant(ToggleableDialog<?> dialog, ContainerEventHandler child) {
        if (dialog instanceof ContainerEventHandler handler) {
            return handler == child;
        }
        return false;
    }

    default void checkClickInterception(ContainerEventHandler child) {
        for (ToggleableDialog<?> dialog : this.getDialogs()) {
            if (dialog != child && (dialog.isCaptureClick() || dialog.isCaptureFocus()) 
                && !isDescendant(dialog, child)) {
                // Aquí iría la lógica de bloqueo de entrada si un modal está activo
            }
        }
    }
}
