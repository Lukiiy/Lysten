package me.lukiiy.lysten.client;

import me.lukiiy.lysten.ConfigKey;
import net.fabricmc.api.ClientModInitializer;

public class LystenClient implements ClientModInitializer {
    public static ConfigKey<Boolean> screenBobbing = ConfigKey.bool("screenBobbing", false);
    public static ConfigKey<Boolean> invBlur = ConfigKey.bool("invBlur", true); // TODO
    public static ConfigKey<Boolean> dropBobbing = ConfigKey.bool("dropBobbing", true);
    public static ConfigKey<ItemRenderStyle> itemStyle = ConfigKey.enumKey("itemStyle", ItemRenderStyle.class, ItemRenderStyle.FACE_CAMERA);
    public static ConfigKey<Boolean> itemDropShadow = ConfigKey.bool("itemDropShadow", true);
    public static ConfigKey<Integer> maxChatHistory = ConfigKey.integer("maxChatHistory", 512).setUnreloadable();
    public static ConfigKey<Integer> subtitlesBgColor = ConfigKey.integer("subtitlesBgColor", 0);
    public static ConfigKey<Boolean> subtitleArrows = ConfigKey.bool("subtitleArrows", false);
    public static ConfigKey<Integer> hitColor = ConfigKey.integer("hitColor", 0);
    public static ConfigKey<String> containerExtra = ConfigKey.string("containerExtra", "<3");
    public static ConfigKey<Boolean> renderOwnNametag = ConfigKey.bool("renderOwnNametag", true);
    public static ConfigKey<Boolean> renderStuckArtifacts = ConfigKey.bool("renderStuckArtifacts", false);
    public static ConfigKey<Boolean> tutorialToasts = ConfigKey.bool("tutorialToasts", true);
    public static ConfigKey<Boolean> arrowCount = ConfigKey.bool("arrowCount", true);
    public static ConfigKey<Float> titleScale = ConfigKey.floatVal("titleScale", 1f);
    public static ConfigKey<Float> subtitleScale = ConfigKey.floatVal("subtitleScale", 1f);
    public static ConfigKey<Boolean> nametagShadow = ConfigKey.bool("nametagShadow", true);
    public static ConfigKey<Integer> nametagBg = ConfigKey.integer("nametagBg", 0);
    public static ConfigKey<Boolean> uiSeeThrough = ConfigKey.bool("uiSeeThrough", true);
    public static ConfigKey<Boolean> armorHitTint = ConfigKey.bool("armorHitTint", false);
    public static ConfigKey<Boolean> chatShadow = ConfigKey.bool("chatShadow", true); // TODO
    public static ConfigKey<Boolean> blockOutlineFull = ConfigKey.bool("blockOutlineFull", false);
    public static ConfigKey<Integer> blockOutlineColor = ConfigKey.integer("blockOutlineColor", 0);
    public static ConfigKey<DeathAnimationStyle> deathAnimStyle = ConfigKey.enumKey("deathAnimStyle", DeathAnimationStyle.class, DeathAnimationStyle.VANILLA);
    public static ConfigKey<ParticleRenderStyle> particleRenderStyle = ConfigKey.enumKey("particleStyle", ParticleRenderStyle.class, ParticleRenderStyle.VANILLA);
    public static ConfigKey<Boolean> cleanerHitboxes = ConfigKey.bool("cleanerHitboxes", false);
    public static ConfigKey<Boolean> lighterBlockParticles = ConfigKey.bool("lighterBlockParticles", true);
    public static ConfigKey<Boolean> filteredFireLayer = ConfigKey.bool("filteredFireLayer", true);

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
        FACE_CAMERA
    }
}
