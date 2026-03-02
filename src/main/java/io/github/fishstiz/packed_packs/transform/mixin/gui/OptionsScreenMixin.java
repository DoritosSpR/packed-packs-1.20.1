package io.github.fishstiz.packed_packs.transform.mixin.gui;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import io.github.fishstiz.packed_packs.gui.screens.PackedPacksScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.OptionsScreen; // Corregido: sin .options
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(OptionsScreen.class)
public abstract class OptionsScreenMixin extends Screen {
    protected OptionsScreenMixin(Component title) {
        super(title);
    }

    @WrapWithCondition(method = "applyPacks", at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/Minecraft;setScreen(Lnet/minecraft/client/gui/screens/Screen;)V"
    ))
    public boolean shouldCloseOnApplyPacks(Minecraft instance, Screen guiScreen) {
        // En 1.20.1, minecraft puede ser nulo en ciertos contextos de inicialización
        return instance.screen == null || !(instance.screen instanceof PackedPacksScreen);
    }
}
