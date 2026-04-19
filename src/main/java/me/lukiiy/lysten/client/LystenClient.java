package me.lukiiy.lysten.client;

import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import me.lukiiy.lysten.ConfigKey;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class LystenClient implements ClientModInitializer {
    public static ConfigKey<Boolean> screenBobbing = ConfigKey.bool("screenBobbing", false);
    public static ConfigKey<Boolean> invBlur = ConfigKey.bool("invBlur", true);
    public static ConfigKey<Boolean> dropBobbing = ConfigKey.bool("dropBobbing", true);
    public static ConfigKey<ItemRenderStyle> itemStyle = ConfigKey.enumKey("itemStyle", ItemRenderStyle.class, ItemRenderStyle.FACE_CAMERA);
    public static ConfigKey<Boolean> itemDropShadow = ConfigKey.bool("itemDropShadow", true);
    public static ConfigKey<Integer> maxChatHistory = ConfigKey.integer("maxChatHistory", 512).setUnreloadable();
    public static ConfigKey<Integer> subtitlesBgColor = ConfigKey.integer("subtitlesBgColor", 0);
    public static ConfigKey<Boolean> subtitleArrows = ConfigKey.bool("subtitleArrows", true);
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
    public static ConfigKey<Boolean> chatShadow = ConfigKey.bool("chatShadow", true);
    public static ConfigKey<Boolean> blockOutlineFull = ConfigKey.bool("blockOutlineFull", false);
    public static ConfigKey<Integer> blockOutlineColor = ConfigKey.integer("blockOutlineColor", 0);
    public static ConfigKey<DeathAnimationStyle> deathAnimStyle = ConfigKey.enumKey("deathAnimStyle", DeathAnimationStyle.class, DeathAnimationStyle.VANILLA);
    public static ConfigKey<ParticleRenderStyle> particleRenderStyle = ConfigKey.enumKey("particleStyle", ParticleRenderStyle.class, ParticleRenderStyle.VANILLA);
    public static ConfigKey<Boolean> cleanerHitboxes = ConfigKey.bool("cleanerHitboxes", false);
    public static ConfigKey<Boolean> lighterBlockParticles = ConfigKey.bool("lighterBlockParticles", true);
    public static ConfigKey<Boolean> filteredFireLayer = ConfigKey.bool("filteredFireLayer", true);
    public static ConfigKey<Boolean> hideCloseBobbers = ConfigKey.bool("hideCloseBobbers", true);
    public static ConfigKey<Boolean> survivalTestHurt = ConfigKey.bool("survivalTestHurt", false);
    public static ConfigKey<Boolean> cleanerDebugMenu = ConfigKey.bool("cleanerDebugMenu", false);
    public static ConfigKey<Boolean> playerlessSubtitles = ConfigKey.bool("playerlessSubtitles", false);
    public static ConfigKey<Boolean> envlessSubtitles = ConfigKey.bool("envlessSubtitles", false);
    public static ConfigKey<Boolean> containerExtraPause = ConfigKey.bool("containerExtraPause", true);
    public static ConfigKey<Boolean> renderNamesInF1 = ConfigKey.bool("unhideGuiRenderNames", false);
    public static ConfigKey<Boolean> renderHandInF1 = ConfigKey.bool("unhideGuiHands", false);
    public static ConfigKey<Boolean> capeHitTint = ConfigKey.bool("capeHitTint", false);

    public static int vanillaHitColor = -1291911168;

    @Override
    public void onInitializeClient() {}

    public static Component parseText(String input) {
        try {
            return ComponentSerialization.CODEC.parse(JsonOps.INSTANCE, JsonParser.parseString(input)).getOrThrow();
        } catch (Exception e) {
            return Component.literal(input);
        }
    }

    public static void filteredResponder(EditBox box, UnaryOperator<String> filtered, Consumer<String> responder) {
        AtomicBoolean internal = new AtomicBoolean(false);

        box.setResponder(text -> {
            if (internal.get()) return;

            String cleaned = filtered.apply(text);
            if (!cleaned.equals(text)) {
                internal.set(true);

                int cursor = box.getCursorPosition();

                box.setValue(cleaned);
                box.setCursorPosition(Math.min(cursor, cleaned.length()));
                internal.set(false);

                text = cleaned;
            }

            responder.accept(text);
        });
    }

    public enum ItemRenderStyle {
        VANILLA,
        FLATTEN,
        BILLBOARD,
        FACE_CAMERA
    }

    public enum DeathAnimationStyle {
        VANILLA,
        NONE,
        FALLBACK,
        INVISIBLE
    }

    public enum ParticleRenderStyle {
        VANILLA,
        FACE_CAMERA
    }
}
