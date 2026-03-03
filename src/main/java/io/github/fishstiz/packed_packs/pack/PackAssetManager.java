package io.github.fishstiz.packed_packs.pack;

import com.google.common.hash.Hashing;
import com.mojang.blaze3d.platform.NativeImage;
import io.github.fishstiz.fidgetz.gui.renderables.sprites.Sprite;
import io.github.fishstiz.packed_packs.PackedPacks;
import io.github.fishstiz.packed_packs.pack.FolderPack;
import io.github.fishstiz.packed_packs.util.PackUtil;
import io.github.fishstiz.packed_packs.util.ResourceUtil;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.resources.IoSupplier;
import org.jetbrains.annotations.Nullable;

import java.io.InputStream;
import java.nio.file.NoSuchFileException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class PackAssetManager {
    // Adaptación 1.20.1: Uso del constructor de ResourceLocation en lugar de withDefaultNamespace
    public static final Sprite DEFAULT_FOLDER_ICON = Sprite.of16(ResourceUtil.id("textures/misc/unknown_folder.png"));
    public static final Sprite DEFAULT_ICON = Sprite.of16(new ResourceLocation("textures/misc/unknown_pack.png"));
    
    private final Map<String, Sprite> cachedIcons = new Object2ObjectOpenHashMap<>();
    private final Minecraft minecraft;
    private Map<String, Sprite> staleIcons;

    public PackAssetManager(Minecraft minecraft) {
        this.minecraft = minecraft;
    }

    public Sprite getIcon(Pack pack) {
        return this.cachedIcons.getOrDefault(pack.getId(), this.staleIcons != null
                ? this.staleIcons.getOrDefault(pack.getId(), getDefaultIcon(pack))
                : getDefaultIcon(pack)
        );
    }

    public void getOrLoadIcon(Pack pack, Consumer<Sprite> iconCallback) {
        if (this.staleIcons != null) {
            Sprite staleIcon = this.staleIcons.get(pack.getId());
            if (staleIcon != null) {
                iconCallback.accept(staleIcon);
            }
        }

        Sprite cachedIcon = this.cachedIcons.get(pack.getId());
        if (cachedIcon != null) {
            iconCallback.accept(cachedIcon);
        } else {
            this.loadPackIcon(pack).thenAcceptAsync(location -> {
                Sprite sprite = location != null ? Sprite.of16(location) : getDefaultIcon(pack);
                this.cachedIcons.put(pack.getId(), sprite);
                iconCallback.accept(sprite);
            }, this.minecraft);
        }
    }

    public void clearIconCache() {
        this.staleIcons = new Object2ObjectOpenHashMap<>(this.cachedIcons);
        this.cachedIcons.clear();
    }

    public static Sprite getDefaultIcon(Pack pack) {
        return pack instanceof FolderPack ? DEFAULT_FOLDER_ICON : DEFAULT_ICON;
    }

    public static ResourceLocation getDefaultLocation(Pack pack) {
        return getDefaultIcon(pack).location;
    }

    /**
     * Adaptado para Minecraft 1.20.1
     */
    private CompletableFuture<@Nullable ResourceLocation> loadPackIcon(Pack pack) {
        return CompletableFuture.supplyAsync(() -> {
            try (PackResources packResources = pack.open()) {
                // En 1.20.1, el acceso a root resources se hace así:
                IoSupplier<InputStream> iconIoSupplier = packResources.getRootResource(PackUtil.ICON_FILENAME);
                if (iconIoSupplier == null) return null;

                // Adaptación 1.20.1: Constructor manual de ResourceLocation
                ResourceLocation icon = new ResourceLocation("minecraft", hashIconName(pack.getId()));
                
                try (InputStream iconStream = iconIoSupplier.get()) {
                    NativeImage nativeImage = NativeImage.read(iconStream);
                    TextureManager manager = this.minecraft.getTextureManager();
                    
                    // Ejecutamos el registro en el hilo principal de renderizado
                    this.minecraft.execute(() -> manager.register(icon, new DynamicTexture(nativeImage)));
                    return icon;
                }
            } catch (Exception e) {
                if (!(e instanceof NoSuchFileException)) {
                    PackedPacks.LOGGER.warn("Failed to load icon from pack '{}'", pack.getId(), e);
                }
                return null;
            }
        }, Util.backgroundExecutor());
    }

    @SuppressWarnings("deprecation")
    private static String hashIconName(String id) {
        // En 1.20.1, validPathChar es el método correcto para sanitizar
        return "packed_packs/pack/" + Util.sanitizeName(id, ResourceLocation::validPathChar) + "/" + Hashing.sha1().hashUnencodedChars(id) + "/icon";
    }
}
