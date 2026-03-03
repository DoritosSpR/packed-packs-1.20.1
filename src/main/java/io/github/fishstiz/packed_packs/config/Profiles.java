package io.github.fishstiz.packed_packs.config;

import io.github.fishstiz.packed_packs.util.JsonLoader;
import java.nio.file.Path;
import java.io.Serializable;
import java.util.function.Supplier;

public class Profiles {
    public static final String PROFILE_EXTENSION = ".json";

    public static <T extends Serializable> T loadJsonOrDefault(Path path, Class<T> clazz, Supplier<T> defaultValue) {
        T result = JsonLoader.load(path, clazz);
        return result != null ? result : defaultValue.get();
    }

    public static String toId(Path path) {
        return removeExtension(path.getFileName().toString());
    }

    public static String removeExtension(String fileName) {
        int lastDot = fileName.lastIndexOf('.');
        return (lastDot > 0) ? fileName.substring(0, lastDot) : fileName;
    }

    public static Path getFile(Path saveFolder, String id) {
        return saveFolder.resolve(id + PROFILE_EXTENSION);
    }
}
