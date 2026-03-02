package io.github.fishstiz.packed_packs.transform.mixin.folders;

import io.github.fishstiz.packed_packs.PackedPacks;
import io.github.fishstiz.packed_packs.transform.interfaces.FilePack;
import io.github.fishstiz.packed_packs.util.PackUtil;
import net.minecraft.server.packs.repository.FolderRepositorySource;
import net.minecraft.server.packs.repository.Pack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Consumer;

@Mixin(FolderRepositorySource.class)
public abstract class FolderRepositorySourceMixin {

    @Shadow @Final private Path folder;

    /**
     * En 1.20.1, el método es loadPacks(Consumer<Pack> consumer).
     * Especificamos la firma completa para evitar errores de mapeo.
     */
    @Inject(method = "loadPacks(Ljava/util/function/Consumer;)V", at = @At("TAIL"))
    private void injectNestedPackDetection(Consumer<Pack> consumer, CallbackInfo ci) {
        try {
            discoverNestedPacks(this.folder, consumer);
        } catch (Exception e) {
            PackedPacks.LOGGER.error("[Packed Packs] Failed to load nested packs from {}", this.folder, e);
        }
    }

    @Unique
    private void discoverNestedPacks(Path root, Consumer<Pack> consumer) {
        if (!Files.isDirectory(root)) return;

        try (DirectoryStream<Path> stream = Files.newDirectoryStream(root)) {
            for (Path path : stream) {
                // Si es un directorio que NO es un pack (no tiene mcmeta), entramos
                if (PackUtil.isNonPackDirectory(path)) {
                    try (DirectoryStream<Path> subStream = Files.newDirectoryStream(path)) {
                        for (Path subPath : subStream) {
                            // Si el sub-archivo/carpeta SI es un pack, lo registramos
                            if (PackUtil.hasMcmeta(subPath) || subPath.toString().endsWith(".zip")) {
                                createAndRegisterPack(subPath, consumer, true);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            PackedPacks.LOGGER.error("Error scanning directory: {}", root, e);
        }
    }

    @Unique
    private void createAndRegisterPack(Path path, Consumer<Pack> consumer, boolean isNested) {
        String name = PackUtil.generatePackName(path);
        String id = isNested ? PackUtil.generateNestedPackId(path) : PackUtil.generatePackId(path);

        // En 1.20.1, el método Pack.readMetaAndCreate es el que FolderRepositorySource usa internamente
        // pero como es complejo de llamar manualmente con todos los parámetros, 
        // a menudo se usa una implementación custom o se accede vía Accessor.
        
        // NOTA: Para que esto compile, necesitarás un método que cree el objeto Pack.
        // Como FolderRepositorySource ya tiene la lógica de creación, lo más limpio
        // suele ser usar un @Invoker para el método privado 'createPack' (si existe en tu versión de Forge)
        // o construir el Pack manualmente.
    }
}
