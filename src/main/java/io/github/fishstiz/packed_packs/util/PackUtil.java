package io.github.fishstiz.packed_packs.util;

import com.sun.jna.platform.FileUtils;
import io.github.fishstiz.packed_packs.PackedPacks;
import io.github.fishstiz.packed_packs.pack.folder.FolderPack;
import io.github.fishstiz.packed_packs.pack.folder.FolderResources;
import io.github.fishstiz.packed_packs.platform.Services;
import io.github.fishstiz.packed_packs.transform.interfaces.FilePack;
import io.github.fishstiz.packed_packs.transform.mixin.UtilAccess;
import io.github.fishstiz.fidgetz.util.lang.CollectionsUtil;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackResources;
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
    
    // En 1.20.1 PackSource.create recibe el componente directamente
    public static final PackSource PACK_SOURCE = PackSource.create(name ->
            Component.translatable("pack.nameAndSource", name, ResourceUtil.getModName().withStyle(ChatFormatting.YELLOW))
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
        Path path = ((FilePack) pack).packed_packs$getPath();
        if (path == null) return -1;

        try {
            return Files.getLastModifiedTime(path).toInstant().toEpochMilli();
        } catch (IOException e) {
            PackedPacks.LOGGER.error("Failed to get age of pack '{}'", pack.getId());
            return -1;
        }
    }

    public static List<String> extractPackIds(Collection<Pack> packs) {
        return CollectionsUtil.extractNonNull(packs, Pack::getId);
    }

    public static String joinPackNames(Collection<Path> paths) {
        return String.join(", ", CollectionsUtil.extractNonNull(paths, PackUtil::generatePackName));
    }

    public static boolean hasMcmeta(Path path) {
        return Files.isRegularFile(path.resolve("pack.mcmeta"), LinkOption.NOFOLLOW_LINKS);
    }

    public static boolean hasFolderConfig(Path path) {
        return Files.isRegularFile(path.resolve(FolderResources.FOLDER_CONFIG_FILENAME), LinkOption.NOFOLLOW_LINKS);
    }

    public static boolean isBuiltIn(Pack pack) {
        // En 1.20.1 comparamos con constantes de PackSource
        return pack.getPackSource() == PackSource.BUILT_IN || Services.PLATFORM.isBuiltInPack(pack);
    }

    public static boolean isEssential(Pack pack) {
        return pack.getId().equals(VANILLA_ID) || pack.getId().equals(FABRIC_ID);
    }

    public static boolean isFeature(Pack pack) {
        // Feature packs suelen ser de posicion fija o marcados específicamente
        return pack.getPackSource() == PackSource.FEATURE;
    }

    public static boolean isNonPackDirectory(Path path) {
        return Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS) && !hasMcmeta(path);
    }

    public static boolean isZipPack(Pack pack) {
        Path path = validatePackPath(pack);
        return path != null && Files.isRegularFile(path) && PackUtil.fileName(path).endsWith(ZIP_PACK_EXTENSION);
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

    public static Path validatePackPath(Pack pack) {
        if (pack == null) return null;
        Path path = ((FilePack) pack).packed_packs$getPath();
        if (path == null) return null;

        try {
            return Files.exists(path) ? path : null;
        } catch (Exception e) {
            PackedPacks.LOGGER.error("[packed_packs] Could not read file: '{}'", path);
            return null;
        }
    }

    public static List<Path> mapValidDirectories(Collection<String> paths) {
        if (paths == null || paths.isEmpty()) return Collections.emptyList();
        List<Path> validPaths = new ObjectArrayList<>(paths.size());
        for (String path : paths) {
            if (path == null || path.isBlank()) continue;
            try {
                Path resolved = Paths.get(path);
                if (Files.isDirectory(resolved, LinkOption.NOFOLLOW_LINKS)) {
                    validPaths.add(resolved.toAbsolutePath().normalize());
                }
            } catch (Exception e) {
                PackedPacks.LOGGER.error("[packed_packs] Failed to resolve path: '{}'", path);
            }
        }
        return validPaths;
    }

    public static void openPack(Pack pack) {
        Path path = ((FilePack) pack).packed_packs$getPath();
        if (path != null) Util.getPlatform().openPath(path.toFile());
    }

    public static void openParent(Path path) {
        File file = path.toFile();
        if (!file.exists()) return;
        Util.getPlatform().openFile(file.getParentFile());
    }

    public static boolean deletePath(Path path) {
        FileUtils fileUtils = FileUtils.getInstance();
        if (fileUtils.hasTrash()) {
            try {
                fileUtils.moveToTrash(path.toFile());
                return true;
            } catch (IOException e) {
                PackedPacks.LOGGER.warn("Failed to move to trash: '{}'", path, e);
            }
        }
        // Fallback a borrado normal
        try {
            if (Files.isDirectory(path)) {
                org.apache.commons.io.FileUtils.deleteDirectory(path.toFile());
            } else {
                Files.deleteIfExists(path);
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean renamePath(Path path, Path newName) {
        try {
            Files.move(path, newName);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    // El sistema de validación ha sido simplificado para 1.20.1 (sin DirectoryValidator ni PackDetector)
    public static PathValidationResults validatePaths(List<Path> paths) {
        PathValidationResults results = new PathValidationResults(paths);
        for (Path path : paths) {
            if (Files.exists(path)) {
                if (hasMcmeta(path) || path.toString().endsWith(ZIP_PACK_EXTENSION)) {
                    results.addValid(path);
                } else if (Files.isDirectory(path)) {
                    // Si es un directorio sin mcmeta, miramos dentro
                    try (DirectoryStream<Path> stream = Files.newDirectoryStream(path)) {
                        for (Path child : stream) {
                            if (hasMcmeta(child) || child.toString().endsWith(ZIP_PACK_EXTENSION)) {
                                results.addValid(path);
                                break;
                            }
                        }
                    } catch (IOException ignored) {}
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
