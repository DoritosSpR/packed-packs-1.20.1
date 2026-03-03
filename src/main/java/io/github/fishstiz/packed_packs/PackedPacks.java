package io.github.fishstiz.packed_packs;

import io.github.fishstiz.packed_packs.impl.PackedPacksApiImpl;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("packed_packs")
public class PackedPacks {
    public PackedPacks() {
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
    }

    private void setup(final FMLCommonSetupEvent event) {
        // Ahora el símbolo es visible gracias al import
        PackedPacksApiImpl.getInstance();
    }
}
