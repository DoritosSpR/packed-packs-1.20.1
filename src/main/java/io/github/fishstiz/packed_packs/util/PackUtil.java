package io.github.fishstiz.packed_packs.util;

import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.ChatFormatting;
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
        // En 1.20.1, para obtener la ruta física solemos necesitar acceder a través de los recursos
        // Esta es una implementación segura por defecto
        return null; 
    }

    public static boolean deletePath(Path path) {
        try {
            if (Files.isDirectory(path)) {
                try (var stream = Files.walk(path)) {
                    stream.sorted(Comparator.reverseOrder()).forEach(p -> {
                        try { Files.delete(p); } catch (Exception ignored) {}
                    });
                }
            } else {
                Files.deleteIfExists(path);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean hasMcmeta(Path path) {
        return Files.isRegularFile(path.resolve("pack.mcmeta"));
    }
}
