package io.github.fishstiz.packed_packs.pack;

import io.github.fishstiz.packed_packs.config.Folder;
import io.github.fishstiz.packed_packs.transform.interfaces.FilePack;
import io.github.fishstiz.packed_packs.util.PackUtil;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.server.packs.repository.Pack;

import java.nio.file.Path;
import java.util.List;
import java.util.regex.Pattern;

public class PackFileOperations {
    private final PackOptionsContext options;
    private final PackRepositoryManager repository;

    public PackFileOperations(PackOptionsContext options, PackRepositoryManager repository) {
        this.options = options;
        this.repository = repository;
    }

    public boolean isOperable(Pack pack) {
        return !this.options.hasOverride(pack) &&
               !this.options.isFixed(pack) &&
               !this.options.isRequired(pack) &&
               !this.options.isLocked() &&
               this.repository.getSelection().getStatus(pack).canDisallow() && // Reemplazo de isEnabled
               ((FilePack) pack).packed_packs$getPath() != null;
    }

    private void remapFolderConfig(FolderPack folderPack, String name, Path path) {
        Folder folder = folderPack.getFolderConfig(); // Acceso directo desde el FolderPack
        if (folder == null) return;

        String newId = PackUtil.generatePackId(name);
        List<String> newPackIds = new ObjectArrayList<>();

        String pattern = "^" + Pattern.quote(folderPack.getId());
        for (String nestedPackId : folder.getPackIds()) {
            if (nestedPackId.startsWith(folderPack.getId())) {
                String newPackId = nestedPackId.replaceAll(pattern, newId);
                newPackIds.add(newPackId);
            } else {
                newPackIds.add(nestedPackId);
            }
        }

        folder.trySetPackIds(newPackIds);
        // Usando el nombre de archivo estándar de configuración de carpetas
        folder.save(path.resolve("packed_packs_folder.json")); 
    }

    public boolean renamePack(Pack pack, String name) {
        if (!this.isOperable(pack)) {
            return false;
        }

        Path path = PackUtil.validatePackPath(pack);
        if (path == null) {
            return false;
        }

        Path newPath = path.getParent().resolve(name);
        if (!PackUtil.renamePath(path, newPath)) {
            return false;
        }

        if (pack instanceof FolderPack folderPack) {
            this.remapFolderConfig(folderPack, name, newPath);
        }

        return true;
    }

    public boolean deletePack(Pack pack) {
        if (!this.isOperable(pack)) {
            return false;
        }

        Path path = PackUtil.validatePackPath(pack);
        if (path == null || !PackUtil.deletePath(path)) {
            return false;
        }

        this.repository.removePack(pack);
        return true;
    }
}
