package io.github.fishstiz.packed_packs.util;

import io.github.fishstiz.packed_packs.pack.FolderPack;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Stream;

public class PackUtil {
    public static final String ICON_FILENAME = "pack.png";
    public static final String VANILLA_ID = "vanilla";

    public static final PackSource PACK_SOURCE = PackSource.create(name -> 
        Component.translatable("pack.nameAndSource", name, 
        Component.literal("Packed Packs").withStyle(ChatFormatting.YELLOW))
        .withStyle(ChatFormatting.GRAY), false);

    public static List<Pack> flattenPacks(Collection<Pack> packs) {
        List<Pack> flattened = new ObjectArrayList<>();
        for (Pack pack : packs) {
            if (pack instanceof FolderPack) {
                flattened.addAll(((FolderPack) pack).flatten());
            } else {
                flattened.add(pack);
            }
        }
        return flattened;
    }

    public static String generatePackId(Path path) {
        return path.getFileName().toString().toLowerCase().replaceAll("[^a-z0-9/._-]", "_");
    }

    public static Path validatePackPath(Pack pack) {
        // Lógica para obtener el Path del pack, dependiendo de tu implementación de FilePack
        return null; 
    }

    public static boolean deletePath(Path path) {
        try {
            if (Files.isDirectory(path)) {
                try (Stream<Path> walk = Files.walk(path)) {
                    walk.sorted(Comparator.reverseOrder()).forEach(p -> {
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
}
