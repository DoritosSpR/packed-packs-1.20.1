package io.github.fishstiz.packed_packs.util;

import io.github.fishstiz.packed_packs.pack.FolderPack;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

public class PackUtil {
    public static final String ICON_FILENAME = "pack.png";
    public static final String ZIP_PACK_EXTENSION = ".zip";
    public static final String VANILLA_ID = "vanilla";

    public static final PackSource PACK_SOURCE = PackSource.create(name -> 
        Component.translatable("pack.nameAndSource", name, 
        Component.literal("Packed Packs").withStyle(ChatFormatting.YELLOW))
        .withStyle(ChatFormatting.GRAY), false);

    public static List<String> extractPackIds(Collection<Pack> packs) {
        return packs.stream()
                .filter(Objects::nonNull)
                .map(Pack::getId)
                .collect(Collectors.toList());
    }

    public static boolean isBuiltIn(Pack pack) {
        return pack.getPackSource() == PackSource.BUILT_IN;
    }

    public static boolean isFeature(Pack pack) {
        return pack.getPackSource() == PackSource.FEATURE;
    }

    public static boolean isEssential(Pack pack) {
        return pack.getId().equals(VANILLA_ID) || isBuiltIn(pack);
    }

    public static long getLastUpdatedEpochMs(Pack pack) {
        // Implementación básica para el comparador
        return 0L; 
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
        } catch (Exception e) {
            return false;
        }
    }
}
