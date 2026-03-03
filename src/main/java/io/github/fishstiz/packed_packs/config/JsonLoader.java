package io.github.fishstiz.packed_packs.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Supplier;

/**
 * Utilidad para cargar y guardar archivos JSON.
 */
public class JsonLoader {
    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping()
            .create();

    /**
     * Carga un objeto desde un archivo JSON.
     */
    public static <T> T load(Path path, Class<T> clazz) {
        if (!Files.exists(path)) {
            return null;
        }
        try (Reader reader = Files.newBufferedReader(path)) {
            return GSON.fromJson(reader, clazz);
        } catch (Exception e) {
            System.err.println("Error cargando JSON desde " + path + ": " + e.getMessage());
            return null;
        }
    }

    /**
     * Carga un JSON o devuelve un valor por defecto si el archivo no existe o falla.
     */
    public static <T> T loadJsonOrDefault(Path path, Class<T> clazz, Supplier<T> defaultValue) {
        T loaded = load(path, clazz);
        return loaded != null ? loaded : defaultValue.get();
    }

    /**
     * Guarda un objeto en la ruta especificada.
     */
    public static <T> void save(Path path, T object) {
        try {
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }
            try (Writer writer = Files.newBufferedWriter(path)) {
                GSON.toJson(object, writer);
            }
        } catch (Exception e) {
            System.err.println("Error guardando JSON en " + path + ": " + e.getMessage());
        }
    }

    /**
     * Alias para compatibilidad con llamadas existentes: JsonLoader.saveJson(objeto, path)
     */
    public static void saveJson(Object obj, Path path) {
        save(path, obj);
    }
}
