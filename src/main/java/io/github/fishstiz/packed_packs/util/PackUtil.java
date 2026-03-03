package io.github.fishstiz.packed_packs.util;

import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.ChatFormatting;
import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PackUtil {
    public static final String ICON_FILENAME = "pack.png";
    public static final String ZIP_PACK_EXTENSION = ".zip";
    public static final String VANILLA_ID = "vanilla";
    private static final Pattern INVALID_CHARS = Pattern.compile("[^a-z0-9/._-]");

    public static final PackSource PACK_SOURCE = PackSource.create(name -> 
        Component.translatable("pack.nameAndSource", name, 
        Component.literal("Packed Packs").withStyle(ChatFormatting.YELLOW))
        .withStyle(ChatFormatting.GRAY), false);

    public static String generatePackId(String name) {
        return INVALID_CHARS.matcher(name.toLowerCase().replace(" ", "_")).replaceAll("");
    }

    public static List<String> extractPackIds(Collection<Pack> packs) {
        return packs.stream()
                .filter(Objects::nonNull)
                .map(Pack::getId)
                .collect(Collectors.toList());
    }

    public static boolean isEssential(Pack pack) {
        return pack.getId().equals(VANILLA_ID) || pack.getPackSource() == PackSource.BUILT_IN;
    }

    public static Path validatePackPath(Pack pack) {
        // En 1.20.1 la ruta se obtiene a través del recurso del pack si está disponible
        // Esta es una implementación simplificada para el sistema de archivos
        return null; 
    }

    public static boolean deletePath(Path path) {
        try {
            if (Files.isDirectory(path)) {
                try (var stream = Files.walk(path)) {
                    stream.sorted(Comparator.reverseOrder()).forEach(p -> {
                        try { Files.delete(p); } catch (IOException ignored) {}
                    });
                }
            } else {
                Files.deleteIfExists(path);
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean hasMcmeta(Path path) {
        return Files.isRegularFile(path.resolve("pack.mcmeta"));
    }

    public static boolean hasFolderConfig(Path path) {
        return Files.exists(path.resolve(io.github.fishstiz.packed_packs.pack.FolderResources.FOLDER_CONFIG_FILENAME));
    }

    public static boolean renamePath(Path source, Path target) {
        try {
            Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public static void openPack(Pack pack) {
        Path path = validatePackPath(pack);
        if (path != null) Util.getPlatform().openPath(path.toFile());
    }

    public static void openParent(Pack pack) {
        Path path = validatePackPath(pack);
        if (path != null && path.getParent() != null) {
            Util.getPlatform().openPath(path.getParent().toFile());
        }
    }
}
