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

    @Unique
    private static final ThreadLocal<Boolean> IS_RECURSIVE_CALL = ThreadLocal.withInitial(() -> false);

    /**
     * En 1.20.1, el método es loadPacks(Consumer<Pack> consumer).
     * Vamos a inyectar nuestra lógica para detectar subcarpetas.
     */
    @Inject(method = "loadPacks", at = @At("HEAD"), cancellable = true)
    private void detectNestedPacks(Consumer<Pack> consumer, CallbackInfo ci) {
        // Evitamos bucles infinitos si llamamos a loadPacks recursivamente
        if (IS_RECURSIVE_CALL.get()) return;

        try {
            IS_RECURSIVE_CALL.set(true);
            discoverNestedPacks(this.folder, consumer);
        } catch (Exception e) {
            PackedPacks.LOGGER.error("Failed to load nested packs", e);
        } finally {
            IS_RECURSIVE_CALL.set(false);
        }
    }

    @Unique
    private void discoverNestedPacks(Path root, Consumer<Pack> consumer) {
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(root)) {
            for (Path path : stream) {
                if (PackUtil.isNonPackDirectory(path)) {
                    // Si es una carpeta sin pack.mcmeta, buscamos dentro (Recursividad de 1 nivel)
                    try (DirectoryStream<Path> subStream = Files.newDirectoryStream(path)) {
                        for (Path subPath : subStream) {
                            // Aquí llamaríamos a la lógica que crea el Pack
                            // Para 1.20.1, lo ideal es dejar que el FolderRepositorySource original
                            // haga su trabajo pero con la ruta modificada.
                        }
                    }
                }
            }
        } catch (Exception ignored) {}
    }

    // Nota: El sistema de 1.21 de "WrapOperation" en PackDetector no funcionará aquí
    // porque esas clases no existen. La lógica de "anidamiento" debe ser recreada
    // inyectando en el constructor de Pack.ResourcesSupplier.
}
