package io.github.fishstiz.packed_packs;

import io.github.fishstiz.packed_packs.impl.PackedPacksApiImpl;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.nio.file.Path;

@Mod("packed_packs")
public class PackedPacks {
    public static final String MOD_ID = "packed_packs";
    public static final Logger LOGGER = LogManager.getLogger();

    public PackedPacks() {
        // Inicializa el API Singleton
        PackedPacksApiImpl.getInstance();
    }

    public static Path getConfigDir() {
        return FMLPaths.CONFIGDIR.get().resolve(MOD_ID);
    }
}
