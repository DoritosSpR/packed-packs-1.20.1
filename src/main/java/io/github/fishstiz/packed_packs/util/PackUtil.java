package io.github.fishstiz.packed_packs.util;

// Eliminamos com.sun.jna.platform.FileUtils si da problemas y usamos commons-io o java.nio
import io.github.fishstiz.packed_packs.PackedPacks;
import io.github.fishstiz.packed_packs.pack.folder.FolderPack;
import io.github.fishstiz.packed_packs.pack.folder.FolderResources;
import io.github.fishstiz.packed_packs.transform.interfaces.FilePack;
import io.github.fishstiz.fidgetz.util.lang.CollectionsUtil;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;

public class PackUtil {
    public static final String HIGH_CONTRAST_ID = "high_contrast";
    public static final String VANILLA_ID = "vanilla";
    public static final String FABRIC_ID = "fabric";
    public static final String ZIP_PACK_EXTENSION = ".zip";
    public static final String ICON_FILENAME = "pack.png";
    
    // En 1.20.1 PackSource.create recibe un decorador de nombre y un booleano
    public static final PackSource PACK_SOURCE = PackSource.create(name -> 
            Component.translatable("pack.nameAndSource", name, ResourceUtil.getModName().copy().withStyle(ChatFormatting.YELLOW))
                    .withStyle(ChatFormatting.GRAY), false);

    private static final String FILE_PREFIX = "file/";
    private static final String DELIMITER = "/";

    private PackUtil() {}

    public static String fileName(Path path) {
        return path.getFileName().toString();
    }

    public static String generatePackName(Path path) {
        return fileName(path);
    }

    public static String generatePackId(String name) {
        return FILE_PREFIX + name;
    }

    public static String generatePackId(Path path) {
        return generatePackId(generatePackName(path));
    }

    public static String generateNestedPackId(Path path) {
        return FILE_PREFIX + generatePackName(path.getParent()) + DELIMITER + generatePackName(path);
    }

    public static long getLastUpdatedEpochMs(Pack pack) {
        if (!(pack instanceof FilePack filePack)) return -1;
        Path path = filePack.packed_packs$getPath();
        if (path == null) return -1;

        try {
            return Files.getLastModifiedTime(path).toInstant().toEpochMilli();
        } catch (IOException e) {
            return -1;
        }
    }

    public static List<String> extractPackIds(Collection<Pack> packs) {
        return CollectionsUtil.extractNonNull(packs, Pack::getId);
    }

    public static boolean hasMcmeta(Path path) {
        return Files.isRegularFile(path.resolve("pack.mcmeta"), LinkOption.NOFOLLOW_LINKS);
    }

    public static boolean isBuiltIn(Pack pack) {
        // Simplificado para Forge 1.20.1
        return pack.getPackSource() == PackSource.BUILT_IN;
    }

    public static boolean isEssential(Pack pack) {
        return pack.getId().equals(VANILLA_ID) || pack.getId().contains("mod_resources");
    }

    public static boolean isNonPackDirectory(Path path) {
        return Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS) && !hasMcmeta(path);
    }

    public static List<Pack> flattenPacks(Collection<Pack> packs) {
        List<Pack> flattened = new ObjectArrayList<>();
        for (Pack pack : packs) {
            if (pack instanceof FolderPack folderPack) {
                flattened.addAll(folderPack.flatten());
            } else {
                flattened.add(pack);
            }
        }
        return flattened;
    }

    public static void openPack(Pack pack) {
        if (pack instanceof FilePack filePack) {
            Path path = filePack.packed_packs$getPath();
            if (path != null) Util.getPlatform().openFile(path.toFile());
        }
    }

    public static boolean deletePath(Path path) {
        try {
            if (Files.isDirectory(path)) {
                // Usamos el borrado simple si no quieres dependencias pesadas
                Files.walk(path)
                     .sorted(Comparator.reverseOrder())
                     .map(Path::toFile)
                     .forEach(File::delete);
            } else {
                Files.deleteIfExists(path);
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static PathValidationResults validatePaths(List<Path> paths) {
        PathValidationResults results = new PathValidationResults(paths);
        for (Path path : paths) {
            if (Files.exists(path)) {
                if (hasMcmeta(path) || path.toString().endsWith(ZIP_PACK_EXTENSION)) {
                    results.addValid(path);
                }
            }
        }
        return results;
    }

    public static class PathValidationResults {
        public final List<Path> valid;
        public final Set<Path> rejected;

        public PathValidationResults(Collection<Path> packs) {
            this.valid = new ArrayList<>();
            this.rejected = new ObjectOpenHashSet<>(packs);
        }

        private void addValid(Path path) {
            this.valid.add(path);
            this.rejected.remove(path);
        }
    }
}
