package io.github.fishstiz.packed_packs.util;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class ResourceUtil {
    public static final String MOD_ID = "packed_packs";

    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    /**
     * Devuelve un componente de texto traducible usando el prefijo del mod.
     */
    public static Component getText(String key) {
        return Component.translatable(MOD_ID + "." + key);
    }

    /**
     * Obtiene un sprite de la carpeta de texturas del mod.
     */
    public static ResourceLocation getIcon(String name) {
        return new ResourceLocation(MOD_ID, "textures/gui/sprites/" + name + ".png");
    }
    
    public static ResourceLocation getGuiSprite(String name) {
        return getIcon(name);
    }

    /**
     * Obtiene un sprite del namespace de Minecraft (vanilla).
     */
    public static ResourceLocation getVanillaSprite(String name) {
        return new ResourceLocation("minecraft", name);
    }
}
