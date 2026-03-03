package io.github.fishstiz.packed_packs.util;

import io.github.fishstiz.packed_packs.pack.FolderPack;
import net.minecraft.server.packs.repository.Pack;
import java.util.ArrayList;
import java.util.List;

public class PackUtil {
    
    public static List<Pack> flattenPacks(List<Pack> packs) {
        List<Pack> flattened = new ArrayList<>();
        for (Pack pack : packs) {
            // Cambio clave: Casting tradicional para evitar errores de compilador en 1.20.1
            if (pack instanceof FolderPack) {
                FolderPack folderPack = (FolderPack) pack;
                flattened.addAll(folderPack.flatten());
            } else {
                flattened.add(pack);
            }
        }
        return flattened;
    }
    
    // En 1.20.1 PackSource.create recibe un decorador de nombre y un booleano (si es fijo)
    public static final PackSource PACK_SOURCE = PackSource.create(name -> 
            Component.translatable("pack.nameAndSource", name, Component.literal("Packed Packs").withStyle(ChatFormatting.YELLOW))
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

    // Métodos añadidos para solucionar errores de compilación previos:
    
    public static boolean hasFolderConfig(Path path) {
        return Files.exists(path.resolve(FolderResources.FOLDER_CONFIG_FILENAME));
    }

    public static Path validatePackPath(Pack pack) {
        if (pack instanceof FilePack filePack) {
            return filePack.packed_packs$getPath();
        }
        return null;
    }

    public static boolean renamePath(Path source, Path target) {
        try {
            Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static void openParent(Pack pack) {
        Path path = validatePackPath(pack);
        if (path != null && path.getParent() != null) {
            Util.getPlatform().openFile(path.getParent().toFile());
        }
    }

    public static boolean isFeature(Pack pack) {
        // En 1.20.1 los packs de "features" suelen identificarse por el ID o el origen
        return pack.getId().startsWith("fabric/") || pack.getId().startsWith("legacy/");
    }

    // --- Fin de métodos añadidos ---

    public static long getLastUpdatedEpochMs(Pack pack) {
        Path path = validatePackPath(pack);
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
            if (pack.getId().startsWith("folder/")) {
                flattened.addAll(folderPack.flatten());
            } else {
                flattened.add(pack);
            }
        }
        return flattened;
    }

    public static void openPack(Pack pack) {
        Path path = validatePackPath(pack);
        if (path != null) {
            Util.getPlatform().openFile(path.toFile());
        }
    }

    public static boolean deletePath(Path path) {
        try {
            if (Files.isDirectory(path)) {
                try (Stream<Path> walk = Files.walk(path)) {
                    walk.sorted(Comparator.reverseOrder())
                        .map(Path::toFile)
                        .forEach(File::delete);
                }
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
                if (hasMcmeta(path) || path.toString().endsWith(ZIP_PACK_EXTENSION) || hasFolderConfig(path)) {
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
