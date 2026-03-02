package io.github.fishstiz.packed_packs.util;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;

public class ToastUtil {
    // En 1.20.1, SystemToastId es una clase interna. 
    // Si el constructor es privado o requiere parámetros, usamos los predefinidos o ajustamos:
    private static final SystemToast.SystemToastId FILE_OPS_FAIL_ID = SystemToast.SystemToastId.PACK_COPY_FAILURE;
    private static final SystemToast.SystemToastId DEV_MODE_ID = SystemToast.SystemToastId.PERIODIC_NOTIFICATION;

    private ToastUtil() {}

    public static void onDevModeToggleToast(boolean enabled) {
        Component enableText = enabled ? CommonComponents.OPTION_ON : CommonComponents.OPTION_OFF;
        SystemToast.addOrUpdate(
                Minecraft.getInstance().getToasts(),
                DEV_MODE_ID,
                ResourceUtil.getModName(),
                ResourceUtil.getText("dev_mode", enableText)
        );
    }

    public static void onFileFailToast(Component message) {
        SystemToast.addOrUpdate(Minecraft.getInstance().getToasts(), FILE_OPS_FAIL_ID, ResourceUtil.getText("file.fail"), message);
    }

    public static Component getRenameFailText(String from, String to) {
        return ResourceUtil.getText("file.rename.fail", from, to);
    }

    public static Component getDeleteFailText(String fileName) {
        return ResourceUtil.getText("file.delete.fail", fileName);
    }
}
