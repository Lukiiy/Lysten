package me.lukiiy.lysten.client;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.util.ARGB;

public class LystenClient implements ClientModInitializer {
    public static boolean screenBobbing = false;
    public static boolean invBlur = true;
    public static boolean dropBobbing = true;
    public static ItemRenderStyle itemStyle = ItemRenderStyle.FACE_CAMERA;
    public static boolean itemDropShadow = true;
    public static int maxChatHistory = 512;
    public static int subtitlesBgColor = 0x00FFFFFF;
    public static boolean subtitleArrows = true;
    public static int hitColor = 0x00FF00FF;
    public static String containerExtra = "<3";
    public static boolean renderOwnNametag = true;
    public static boolean renderStuckArrows = false;
    public static boolean disableTutorialToasts = true;
    public static boolean arrowCount = true;
    public static float titleScale = 1f;
    public static float subtitleScale = 1f;

    @Override
    public void onInitializeClient() {}

    public static boolean isColorTransparent(int color) {
        return ARGB.alpha(color) == 0;
    }

    public enum ItemRenderStyle {
        VANILLA,
        FLAT_SPRITE, // Minecraft Beta
        BILLBOARD,
        FACE_CAMERA
    }
}
