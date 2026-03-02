package io.github.fishstiz.packed_packs.gui.components.pack;

import io.github.fishstiz.fidgetz.gui.renderables.sprites.Sprite;
import io.github.fishstiz.packed_packs.pack.PackAssetManager;
import io.github.fishstiz.packed_packs.util.constants.Theme;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget; // Clase base de Vanilla
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

public class PackWidget extends AbstractWidget {
    private final Pack pack;
    private final PackAssetManager assets;
    private final int spacing;
    private static final int ICON_SIZE = 32;

    public PackWidget(Pack pack, PackAssetManager assets, int x, int y, int width, int height, int spacing) {
        // En 1.20.1 el constructor de AbstractWidget es (x, y, width, height, message)
        super(x, y, width, height, Component.empty());
        this.pack = pack;
        this.assets = assets;
        this.spacing = spacing;
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        Minecraft mc = Minecraft.getInstance();
        Font font = mc.font;

        int currentX = this.getX() + spacing;
        int currentY = this.getY();

        Sprite iconSprite = this.assets.getIcon(this.pack);
        
        // El método blit en 1.20.1 requiere ResourceLocation, x, y, u, v, width, height, textureWidth, textureHeight
        guiGraphics.blit(iconSprite.location, currentX, currentY, 0, 0, ICON_SIZE, ICON_SIZE, ICON_SIZE, ICON_SIZE);

        int textX = currentX + ICON_SIZE + spacing;
        int textWidth = this.width - (ICON_SIZE + (spacing * 3));

        int titleColor = this.pack.getCompatibility().isCompatible() ? Theme.WHITE.getARGB() : Theme.RED_500.getARGB();
        guiGraphics.drawString(font, this.pack.getTitle(), textX, currentY + 1, titleColor);

        List<FormattedCharSequence> lines = font.split(this.pack.getDescription(), textWidth);
        for (int i = 0; i < Math.min(lines.size(), 2); i++) {
            guiGraphics.drawString(font, lines.get(i), textX, currentY + 12 + (i * 10), Theme.GRAY_500.getARGB());
        }
    }

    public int getContentLeft() {
        return this.getX() + spacing + ICON_SIZE + spacing;
    }

    @Override
    protected void updateWidgetNarration(net.minecraft.client.gui.narration.NarrationElementOutput narrationElementOutput) {
        narrationElementOutput.add(net.minecraft.client.gui.narration.NarratedElementType.TITLE, this.pack.getTitle());
    }
}
