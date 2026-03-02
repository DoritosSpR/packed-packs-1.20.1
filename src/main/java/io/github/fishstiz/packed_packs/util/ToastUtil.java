package io.github.fishstiz.packed_packs.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.network.chat.Component;

public class ToastUtil {
    // Usamos el enum o la constante directamente si SystemToastId no compila como tipo
    public static void onDevModeToggleToast(boolean enabled) {
        SystemToast.addOrUpdate(
                Minecraft.getInstance().getToasts(),
                SystemToast.SystemToastId.PERIODIC_NOTIFICATION, // Usa una constante existente
                ResourceUtil.getModName(),
                ResourceUtil.getText("dev_mode", enabled ? "ON" : "OFF")
        );
    }

    public static void onFileFailToast(Component message) {
        SystemToast.addOrUpdate(
                Minecraft.getInstance().getToasts(), 
                SystemToast.SystemToastId.PACK_COPY_FAILURE, 
                ResourceUtil.getText("file.fail"), 
                message
        );
    }
}
