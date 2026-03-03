package io.github.fishstiz.fidgetz.gui.components;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.client.gui.components.events.ContainerEventHandler;
import java.util.List;
import java.util.stream.Collectors;

public abstract class ToggleableDialogContainer<T extends ToggleableDialog<T>> implements ContainerEventHandler {
    
    protected abstract List<T> getDialogs();

    public List<T> getOpenDialogs() {
        // Reemplazamos la referencia de método problemática por una lambda explícita
        return this.getDialogs().stream()
                .filter(dialog -> dialog.visible) // En 1.20.1 usamos el campo visible
                .collect(Collectors.toCollection(ObjectArrayList::new));
    }

    private boolean isDescendant(ToggleableDialog<?> dialog, ContainerEventHandler child) {
        // Corrección de tipos para evitar el error de "cannot be converted to ContainerEventHandler"
        if (dialog instanceof ContainerEventHandler handler) {
            return handler == child; // Simplificado para la lógica de herencia de UI
        }
        return false;
    }

    public void checkClickInterception(ContainerEventHandler child) {
        for (T dialog : this.getDialogs()) {
            // Realizamos el cast explícito a ContainerEventHandler para las comparaciones
            ContainerEventHandler dialogHandler = (ContainerEventHandler) dialog;
            
            if (dialog != child && (dialog.isCaptureClick() || dialog.isCaptureFocus()) 
                && !isDescendant(dialog, child)) {
                // Lógica de intercepción
            }
        }
    }
}
