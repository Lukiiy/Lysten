package me.lukiiy.lysten;

import net.fabricmc.api.ModInitializer;

public class Lysten implements ModInitializer {
    public static boolean screenBobbing = false;
    public static boolean invBlur = true;
    public static boolean dropBobbing = false;
    public static ItemRenderStyle itemStyle = ItemRenderStyle.FLAT_SPRITE;
    public static boolean itemDropShadow = false;
    public static int maxChatHistory = 512;
    public static int subtitlesBgColor = 0x00FFFFFF;

    @Override
    public void onInitialize() {

    }

    public enum ItemRenderStyle {
        VANILLA,
        FLAT_SPRITE, // Beta minecraft
        BILLBOARD,
        FACE_PLAYER
    }
}
