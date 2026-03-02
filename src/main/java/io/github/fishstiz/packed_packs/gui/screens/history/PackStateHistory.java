package io.github.fishstiz.packed_packs.gui.screens.history;

import io.github.fishstiz.packed_packs.gui.components.pack.PackListModel;
import java.util.Stack;

public class PackStateHistory {
    private final Stack<State> undoStack = new Stack<>();
    private final Stack<State> redoStack = new Stack<>();
    private static final int MAX_HISTORY = 50;

    public void push(State state) {
        if (state == null) return;
        
        // Si el estado actual es igual al último, no duplicamos
        if (!undoStack.isEmpty() && undoStack.peek().equals(state)) {
            return;
        }

        undoStack.push(state);
        redoStack.clear(); // Al hacer una acción nueva, invalidamos el "rehacer"

        if (undoStack.size() > MAX_HISTORY) {
            undoStack.remove(0);
        }
    }

    public State undo(State currentState) {
        if (undoStack.isEmpty()) return null;
        
        redoStack.push(currentState);
        return undoStack.pop();
    }

    public State redo(State currentState) {
        if (redoStack.isEmpty()) return null;
        
        undoStack.push(currentState);
        return redoStack.pop();
    }

    public void clear() {
        undoStack.clear();
        redoStack.clear();
    }

    public boolean canUndo() {
        return !undoStack.isEmpty();
    }

    public boolean canRedo() {
        return !redoStack.isEmpty();
    }

    /**
     * Representa un momento específico de la configuración de los packs.
     * Usamos un 'record' de Java 17 para simplificar la comparación.
     */
    public record State(
        PackListModel.Snapshot availableSnapshot,
        PackListModel.Snapshot selectedSnapshot
    ) {}
}
