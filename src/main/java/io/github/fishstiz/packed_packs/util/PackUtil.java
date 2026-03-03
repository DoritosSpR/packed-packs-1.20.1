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
    public static final PackSource PACK_SOURCE = PackSource.create(name -> 
        Component.translatable("pack.nameAndSource", name, 
        Component.literal("Packed Packs").withStyle(ChatFormatting.YELLOW))
        .withStyle(ChatFormatting.GRAY), false);

    public static boolean isBuiltIn(Pack pack) {
        return pack.getPackSource() == PackSource.BUILT_IN;
    }

    public static boolean isFeature(Pack pack) {
        return pack.getId().startsWith("update_") || pack.getId().equals("bundle");
    }

    public static long getLastUpdatedEpochMs(Pack pack) {
        // En una implementación real, buscaríamos la ruta del archivo del pack
        return System.currentTimeMillis(); 
    }

    public static boolean hasMcmeta(Path path) {
        return Files.isRegularFile(path.resolve("pack.mcmeta"));
    }

    public static boolean hasFolderConfig(Path path) {
        return Files.exists(path.resolve(".folder_config.json"));
    }

    public static boolean renamePath(Path source, Path target) {
        try {
            Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
