package io.github.fishstiz.packed_packs.transform.mixin.gui;

import io.github.fishstiz.fidgetz.gui.components.FidgetzButton;
import io.github.fishstiz.fidgetz.gui.renderables.sprites.Sprite;
import io.github.fishstiz.fidgetz.gui.shapes.Size;
import io.github.fishstiz.packed_packs.gui.metadata.PackSelectionScreenArgs;
import io.github.fishstiz.packed_packs.gui.screens.PackedPacksScreen;
import io.github.fishstiz.packed_packs.transform.mixin.PackSelectionScreenAccessor;
import io.github.fishstiz.packed_packs.util.ResourceUtil;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.packs.PackSelectionScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.PackRepository;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File; // En 1.20.1 solía usarse File o Path
import java.nio.file.Path;
import java.util.function.Consumer;

@Mixin(PackSelectionScreen.class)
public abstract class PackSelectionScreenMixin extends Screen implements PackSelectionScreenAccessor {
    protected PackSelectionScreenMixin(Component title) {
        super(title);
    }

    @Unique
    private PackSelectionScreenArgs packed_packs$original;

    @Unique
    private FidgetzButton<?> packed_packs$button;

    @Unique
    private Screen packed_packs$previous;

    @Override
    public void packed_packs$setPrevious(Screen previous) {
        this.packed_packs$previous = previous;
    }

    @Override
    public @Nullable Screen packed_packs$getPrevious() {
        return this.packed_packs$previous;
    }

    // El constructor en 1.20.1 es: PackSelectionScreen(PackRepository, Consumer<PackRepository>, Path, Component)
    @Inject(method = "<init>", at = @At("TAIL"))
    private void setRepository(PackRepository repository, Consumer<PackRepository> output, Path packDir, Component title, CallbackInfo ci) {
        this.packed_packs$original = new PackSelectionScreenArgs(repository, output, packDir, title);
    }

    @Inject(method = "init", at = @At("TAIL"))
    private void addPackedPacksButton(CallbackInfo ci) {
        if (this.minecraft == null) return;

        Screen previous = this.packed_packs$previous != null ? this.packed_packs$previous : this;
        
        // Posicionamiento manual para 1.20.1 (ajustar según sea necesario)
        // Normalmente cerca del botón de "Done" o "Open Pack Folder"
        int x = this.width / 2 + 158; // Ejemplo de posición
        int y = this.height - 48;

        this.packed_packs$button = FidgetzButton.builder()
                .makeSquare()
                .setTooltip(Tooltip.create(ResourceUtil.getModName()))
                .setSprite(new Sprite(ResourceUtil.getIcon("packed_packs"), Size.of16()))
                .setOnPress(() -> this.minecraft.setScreen(new PackedPacksScreen(this.minecraft, previous, this.packed_packs$original)))
                .build();

        // En 1.20.1 usamos addRenderableWidget
        this.addRenderableWidget(this.packed_packs$button);
        this.packed_packs$button.setX(x);
        this.packed_packs$button.setY(y);
    }
}
