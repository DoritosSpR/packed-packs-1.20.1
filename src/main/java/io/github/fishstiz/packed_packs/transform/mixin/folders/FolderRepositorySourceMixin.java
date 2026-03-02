package io.github.fishstiz.packed_packs.transform.mixin.folders;

import io.github.fishstiz.packed_packs.PackedPacks;
import io.github.fishstiz.packed_packs.transform.interfaces.FilePack;
import io.github.fishstiz.packed_packs.transform.mixin.folders.additional.FolderRepositorySourceAccessor;
import io.github.fishstiz.packed_packs.util.PackUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.FolderRepositorySource;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
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
     * En 1.20.1, inyectamos al final de loadPacks para añadir nuestros packs detectados
     * a la lista que Minecraft ya está construyendo.
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
                // Si es un directorio que NO es un pack (no tiene mcmeta), buscamos dentro
                if (PackUtil.isNonPackDirectory(path)) {
                    try (DirectoryStream<Path> subStream = Files.newDirectoryStream(path)) {
                        for (Path subPath : subStream) {
                            // Si el sub-archivo/carpeta es un pack, lo registramos
                            if (PackUtil.hasMcmeta(subPath) || subPath.toString().endsWith(".zip")) {
                                createAndRegisterPack(subPath, consumer, true);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            PackedPacks.LOGGER.error("Error scanning directory for nested packs: {}", root, e);
        }
    }

    @Unique
    private void createAndRegisterPack(Path path, Consumer<Pack> consumer, boolean isNested) {
        String id = isNested ? PackUtil.generateNestedPackId(path) : PackUtil.generatePackId(path);
        
        // Accedemos al PackSource original (Mundo, Servidor, etc.) mediante el Accessor
        PackSource source = ((FolderRepositorySourceAccessor) this).packed_packs$getPackSource();

        // Creamos el proveedor de recursos (encargado de leer el ZIP o la carpeta)
        Pack.ResourcesSupplier resourcesSupplier = Pack.createContentsSupplier(path);

        // En 1.20.1 usamos readMetaAndCreate para validar el pack.mcmeta automáticamente
        Pack pack = Pack.readMetaAndCreate(
            id, 
            Component.literal(PackUtil.generatePackName(path)), 
            false, // isRequired
            resourcesSupplier, 
            PackType.CLIENT_RESOURCES, 
            Pack.Position.TOP, 
            source
        );

        if (pack != null) {
            // Guardamos metadatos adicionales en el objeto Pack mediante nuestras interfaces
            ((FilePack) pack).packed_packs$setPath(path);
            ((FilePack) pack).packed_packs$setNestedPack(isNested);
            
            consumer.accept(pack);
        }
    }
}
