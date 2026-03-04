package io.github.fishstiz.packed_packs.pack;

import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackRepository;
import java.util.Collection;

public class PackRepositoryManager {
    private final PackRepository repository;

    public PackRepositoryManager(PackRepository repository) {
        this.repository = repository;
    }

    // En 1.20.1 no existe getSelection().getStatus(), usamos la lógica directa del repositorio
    public boolean isEnabled(String id) {
        return this.repository.getSelectedIds().contains(id);
    }

    public void removePack(Pack pack) {
        // Lógica para deshabilitar o quitar de la lista de selección
        Collection<String> selected = this.repository.getSelectedIds();
        if (selected.contains(pack.getId())) {
            // Aquí iría la lógica para refrescar el repositorio
        }
    }

    public PackRepository getRepository() {
        return this.repository;
    }
}
