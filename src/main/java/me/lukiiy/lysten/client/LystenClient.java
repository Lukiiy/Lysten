package me.lukiiy.lysten.client;

import net.fabricmc.api.ClientModInitializer;

public class LystenClient implements ClientModInitializer {
    public static boolean screenBobbing;
    public static boolean invBlur;
    public static boolean dropBobbing;
    public static ItemRenderStyle itemStyle;
    public static boolean itemDropShadow;
    public static int maxChatHistory;
    public static int subtitlesBgColor;
    public static boolean subtitleArrows;
    public static int hitColor;
    public static String containerExtra;
    public static boolean renderOwnNametag;
    public static boolean renderStuckArtifacts;
    public static boolean tutorialToasts;
    public static boolean arrowCount;
    public static float titleScale;
    public static float subtitleScale;
    public static boolean nametagShadow;
    public static int nametagBg;
    public static boolean uiSeeThrough;
    public static boolean armorHitTint;
    public static boolean chatShadow;
    public static boolean blockOutlineFull;
    public static int blockOutlineColor;
    public static DeathAnimationStyle deathAnimStyle;
    public static ParticleRenderStyle particleRenderStyle;

    @Override
    public void onInitializeClient() {}

    public enum ItemRenderStyle {
        VANILLA,
        FLAT_SPRITE,
        BILLBOARD,
        FACE_CAMERA
    }

    public enum DeathAnimationStyle {
        VANILLA,
        NONE,
        FALLBACK
    }

    public enum ParticleRenderStyle {
        VANILLA,
        FACE_CAMERA,
        FLAT
    }
}
