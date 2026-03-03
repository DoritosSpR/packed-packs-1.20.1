package io.github.fishstiz.packed_packs.util;

import net.minecraft.resources.ResourceLocation;

public class ResourceUtil {
    public static final String MOD_ID = "packed_packs";

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    public static ResourceLocation getIcon(String name) {
        // En 1.20.1 los sprites de la GUI suelen ir en esta ruta
        return new ResourceLocation(MOD_ID, "textures/gui/sprites/" + name + ".png");
    }
    
    public static ResourceLocation getGuiSprite(String name) {
        return getIcon(name);
    }
}
