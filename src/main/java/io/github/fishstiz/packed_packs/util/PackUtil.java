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
        return packs.stream().map(Pack::getId).collect(Collectors.toList());
    }

    public static boolean isBuiltIn(Pack pack) {
        return pack.getPackSource() == PackSource.BUILT_IN;
    }

    public static boolean isFeature(Pack pack) {
        return pack.getId().startsWith("update_") || pack.getId().equals("bundle");
    }

    public static Path validatePackPath(Pack pack) {
        // En 1.20.1, si usas Forge, puedes intentar obtener la ruta del recurso
        return null; // Retornar null evita crashes si no se encuentra
    }

    public static boolean deletePath(Path path) {
        try {
            Files.deleteIfExists(path);
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

    public static long getLastUpdatedEpochMs(Pack pack) {
        return System.currentTimeMillis();
    }
}
