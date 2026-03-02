package io.github.fishstiz.packed_packs;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.loading.FMLPaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Path;

@Mod(PackedPacks.MOD_ID) // <--- ESTO ES LO QUE BUSCÁBAMOS
public class PackedPacks {
    public static final String MOD_ID = "packed_packs";
    public static final String MOD_NAME = "Packed Packs";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_NAME);

    public PackedPacks() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // 1. Inicializar el API interno (Esto conecta impl con api)
        // Normalmente esto registra los eventos y las preferencias
        io.github.fishstiz.packed_packs.impl.PackedPacksApiImpl.init(); 

        LOGGER.info("Packed Packs para Forge 1.20.1 inicializado.");
    }

    public static Path getConfigDir() {
        // En Forge 1.20.1 usamos FMLPaths para obtener la carpeta de config directamente
        return FMLPaths.CONFIGDIR.get().resolve(MOD_ID);
    }
}
