package me.lukiiy.lysten;

import net.fabricmc.api.ModInitializer;

public class Lysten implements ModInitializer {
    public static boolean screenBobbing = false;
    public static boolean invBlur = true;
    public static boolean dropBobbing = true;
    public static ItemRenderStyle itemStyle = ItemRenderStyle.FACE_CAMERA;
    public static boolean itemDropShadow = true;
    public static int maxChatHistory = 512;
    public static int subtitlesBgColor = 0x00FFFFFF;
    public static boolean subtitleArrows = true;
    public static int hitColor = 0x00FFFFFF;
    public static String containerExtra = "<3";
    public static boolean renderOwnNametag = true;
    public static boolean renderStuckArrows = false;

    @Override
    public void onInitialize() {

    }

    public enum ItemRenderStyle {
        VANILLA,
        FLAT_SPRITE, // Minecraft Beta
        BILLBOARD,
        FACE_CAMERA
    }
}
