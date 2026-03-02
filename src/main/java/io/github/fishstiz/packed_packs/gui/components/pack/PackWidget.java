package io.github.fishstiz.packed_packs.gui.components.pack;

import io.github.fishstiz.fidgetz.gui.components.AbstractWidget;
import io.github.fishstiz.fidgetz.gui.renderables.sprites.Sprite;
import io.github.fishstiz.packed_packs.pack.PackAssetManager;
import io.github.fishstiz.packed_packs.util.constants.Theme;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
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

        // 1. Obtener icono (Consultar caché del manager)
        Sprite iconSprite = this.assets.getIcon(this.pack);
        
        // 2. Renderizar Icono (Uso de blit estándar 1.20.1)
        guiGraphics.blit(iconSprite.location, currentX, currentY, 0, 0, ICON_SIZE, ICON_SIZE, ICON_SIZE, ICON_SIZE);

        // 3. Renderizar Textos
        int textX = currentX + ICON_SIZE + spacing;
        int textWidth = this.width - (ICON_SIZE + (spacing * 3));

        // Título: Blanco si es compatible, Rojo si no
        int titleColor = this.pack.getCompatibility().isCompatible() ? Theme.WHITE.getARGB() : Theme.RED_500.getARGB();
        guiGraphics.drawString(font, this.pack.getTitle(), textX, currentY + 1, titleColor);

        // Descripción: Dividida en dos líneas para que no se salga
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
