package io.github.fishstiz.packed_packs.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

public class JsonLoader {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static <T> T load(Path path, Class<T> clazz) {
        if (!Files.exists(path)) return null;
        try (Reader reader = Files.newBufferedReader(path)) {
            return GSON.fromJson(reader, clazz);
        } catch (Exception e) {
            return null;
        }
    }

    public static <T> void save(Path path, T object) {
        try {
            Files.createDirectories(path.getParent());
            try (Writer writer = Files.newBufferedWriter(path)) {
                GSON.toJson(object, writer);
            }
        } catch (Exception ignored) {}
    }
}
