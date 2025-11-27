package me.lukiiy.lysten.client;

import net.fabricmc.api.ClientModInitializer;

public class LystenClient implements ClientModInitializer {
    public static boolean screenBobbing = false;
    public static boolean invBlur = true;
    public static boolean dropBobbing = true;
    public static ItemRenderStyle itemStyle = ItemRenderStyle.FACE_CAMERA;
    public static boolean itemDropShadow = true;
    public static int maxChatHistory = 512;
    public static int subtitlesBgColor = 0;
    public static boolean subtitleArrows = false;
    public static int hitColor = 0;
    public static String containerExtra = "<3";
    public static boolean renderOwnNametag = true;
    public static boolean renderStuckArrows = false;
    public static boolean disableTutorialToasts = true;
    public static boolean arrowCount = true;
    public static float titleScale = 1f;
    public static float subtitleScale = 1f;
    public static boolean nametagShadow = true;
    public static int nametagBg = 0;

    @Override
    public void onInitializeClient() {}

    public enum ItemRenderStyle {
        VANILLA,
        FLAT_SPRITE, // Minecraft Beta
        BILLBOARD,
        FACE_CAMERA
    }
}
