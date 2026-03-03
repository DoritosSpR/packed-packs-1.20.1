package io.github.fishstiz.fidgetz.util;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.fishstiz.fidgetz.gui.renderables.sprites.GuiSprite;
import io.github.fishstiz.fidgetz.gui.renderables.sprites.Sprite;
import net.minecraft.Util;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class DrawUtil {
    // CORRECCIÓN 1.20.1: Usar constructores de ResourceLocation manuales
    public static final Sprite DEMO_BACKGROUND = new GuiSprite(new ResourceLocation("minecraft", "popup/background"), 236, 34);
    public static final ResourceLocation SHADOW_SPRITE = new ResourceLocation("fidgetz", "drop_shadow");
    private static final int SHADOW_BORDER = 32;

    private DrawUtil() {
    }

    public static int renderScrollingStringLeftAlign(
            GuiGraphics guiGraphics,
            Font font,
            Component text,
            int startX,
            int startY,
            int endX,
            int endY,
            int color,
            boolean shadow
    ) {
        int textWidth = font.width(text);
        int textY = (startY + endY - 9) / 2 + 1;
        int availableWidth = endX - startX;

        if (textWidth > availableWidth) {
            int overflowWidth = textWidth - availableWidth;
            double timeSeconds = Util.getMillis() / 1000.0;
            double scrollDuration = Math.max(overflowWidth * 0.5, 3.0);
            double scrollFactor = Math.sin((Math.PI / 2) * Math.cos((Math.PI * 2) * timeSeconds / scrollDuration)) / 2.0 + 0.5;
            double scrollOffset = Mth.lerp(scrollFactor, 0.0, overflowWidth);

            guiGraphics.enableScissor(startX, startY, endX, endY);
            // En 1.20.1 drawString devuelve la posición final X
            int width = guiGraphics.drawString(font, text, startX - (int) scrollOffset, textY, color, shadow);
            guiGraphics.disableScissor();

            return width;
        } else {
            return guiGraphics.drawString(font, text, startX, textY, color, shadow);
        }
    }

    public static int renderScrollingStringLeftAlign(
            GuiGraphics guiGraphics,
            Font font,
            Component text,
            int startX,
            int startY,
            int endX,
            int endY,
            int color
    ) {
        return renderScrollingStringLeftAlign(guiGraphics, font, text, startX, startY, endX, endY, color, true);
    }

    public static void renderDropShadow(GuiGraphics guiGraphics, int x, int y, int width, int height, int shadowSize) {
        float scale = (float) shadowSize / SHADOW_BORDER;
        int offset = Math.round(SHADOW_BORDER * scale);
        RenderSystem.enableBlend();
        
        // CORRECCIÓN 1.20.1: blitSprite NO EXISTE. 
        // Si SHADOW_SPRITE es un sprite del atlas, debes usar blit().
        // Asumiendo que SHADOW_SPRITE es una textura completa o manejada por el atlas:
        guiGraphics.blit(
                SHADOW_SPRITE,
                x - offset,
                y - offset,
                0, 0, // UV
                width + offset * 2,
                height + offset * 2,
                width + offset * 2, // textureWidth
                height + offset * 2 // textureHeight
        );
        
        RenderSystem.defaultBlendFunc();
    }
}
