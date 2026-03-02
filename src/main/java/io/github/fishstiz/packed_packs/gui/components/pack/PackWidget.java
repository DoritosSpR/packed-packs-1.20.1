package io.github.fishstiz.packed_packs.gui.components.pack;

import io.github.fishstiz.fidgetz.gui.components.AbstractWidget;
import io.github.fishstiz.packed_packs.pack.PackAssetManager;
import io.github.fishstiz.packed_packs.util.constants.Theme;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.util.FormattedCharSequence;

import java.util.List;

public class PackWidget extends AbstractWidget {
    private final Pack pack;
    private final PackAssetManager assets;
    private final int spacing;
    
    // Altura estándar del icono de un Resource Pack en Minecraft
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

        // 1. Renderizar el Icono del Pack
        ResourceLocation iconLocation = this.assets.getIcon(this.pack);
        guiGraphics.blit(iconLocation, currentX, currentY, 0, 0, ICON_SIZE, ICON_SIZE, ICON_SIZE, ICON_SIZE);

        // 2. Calcular posición para el texto (a la derecha del icono)
        int textX = currentX + ICON_SIZE + spacing;
        int textWidth = this.width - (ICON_SIZE + (spacing * 3));

        // 3. Renderizar Título (Nombre del Pack)
        // Usamos ARGB para el color (Theme.WHITE suele ser 0xFFFFFFFF)
        Component title = this.pack.getTitle();
        guiGraphics.drawString(font, title, textX, currentY + 1, Theme.WHITE.getARGB());

        // 4. Renderizar Descripción (con ajuste de línea si es muy larga)
        Component description = this.pack.getDescription();
        List<FormattedCharSequence> lines = font.split(description, textWidth);
        
        int descY = currentY + 12; // Un poco debajo del título
        // En 1.20.1 limitamos a 2 líneas para que quepa en los 32px de alto
        for (int i = 0; i < Math.min(lines.size(), 2); i++) {
            guiGraphics.drawString(font, lines.get(i), textX, descY + (i * 10), Theme.GRAY_500.getARGB());
        }
    }

    // Método de utilidad para que otros componentes (como el botón de carpeta) 
    // sepan dónde empieza el contenido de texto.
    public int getContentLeft() {
        return this.getX() + spacing + ICON_SIZE + spacing;
    }

    @Override
    protected void updateWidgetNarration(net.minecraft.client.gui.narration.NarrationElementOutput narrationElementOutput) {
        // Opcional: Para accesibilidad
        narrationElementOutput.add(net.minecraft.client.gui.narration.NarratedElementType.TITLE, this.pack.getTitle());
    }
}
