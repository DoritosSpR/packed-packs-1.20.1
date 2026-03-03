package io.github.fishstiz.packed_packs.util;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class ResourceUtil {
    public static final String MOD_ID = "packed_packs";
    public static final String MOD_NAME = "Packed Packs";

    public static Component getText(String key) {
        return Component.translatable(MOD_ID + "." + key);
    }

    public static ResourceLocation getGuiSprite(String path) {
        // En 1.20.1 se usa el constructor de ResourceLocation directamente
        return new ResourceLocation(MOD_ID, "textures/gui/sprites/" + path + ".png");
    }

    public static ResourceLocation getVanillaSprite(String path) {
        return new ResourceLocation("minecraft", "textures/gui/sprites/" + path + ".png");
    }

    public static ResourceLocation fromPath(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
