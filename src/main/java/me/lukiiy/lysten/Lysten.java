package me.lukiiy.lysten;

import me.lukiiy.lysten.client.LystenClient;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Lysten implements ModInitializer {
    public static final String MOD_ID = "lysten";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final Config CONFIG = new Config("lysten", "Lysten!");

    @Override
    public void onInitialize() {
        initConfig();
    }

    public static void initConfig() {
        CONFIG.setIfAbsent("screenBobbing", "false");
        CONFIG.setIfAbsent("invBlur", "true");
        CONFIG.setIfAbsent("dropBobbing", "true");
        CONFIG.setIfAbsent("itemStyle", "FACE_CAMERA");
        CONFIG.setIfAbsent("itemDropShadow", "true");
        CONFIG.setIfAbsent("maxChatHistory", "512");
        CONFIG.setIfAbsent("subtitlesBgColor", "0");
        CONFIG.setIfAbsent("subtitleArrows", "false");
        CONFIG.setIfAbsent("hitColor", "0");
        CONFIG.setIfAbsent("containerExtra", "<3");
        CONFIG.setIfAbsent("renderOwnNametag", "true");
        CONFIG.setIfAbsent("renderStuckArtifacts", "false");
        CONFIG.setIfAbsent("tutorialToasts", "true");
        CONFIG.setIfAbsent("arrowCount", "true");
        CONFIG.setIfAbsent("titleScale", "1.0");
        CONFIG.setIfAbsent("subtitleScale", "1.0");
        CONFIG.setIfAbsent("nametagShadow", "true");
        CONFIG.setIfAbsent("nametagBg", "0");
        CONFIG.setIfAbsent("uiSeeThrough", "true");
        CONFIG.setIfAbsent("armorHitTint", "false");

        loadConfig();
    }

    public static void loadConfig() {
        LystenClient.screenBobbing = CONFIG.getBoolean("screenBobbing");
        LystenClient.invBlur = CONFIG.getBoolean("invBlur");
        LystenClient.dropBobbing = CONFIG.getBoolean("dropBobbing");
        LystenClient.itemStyle = LystenClient.ItemRenderStyle.valueOf(CONFIG.getOrDefault("itemStyle", "FACE_CAMERA"));
        LystenClient.itemDropShadow = CONFIG.getBoolean("itemDropShadow");
        LystenClient.maxChatHistory = (int) CONFIG.getLong("maxChatHistory");
        LystenClient.subtitlesBgColor = (int) CONFIG.getLong("subtitlesBgColor");
        LystenClient.subtitleArrows = CONFIG.getBoolean("subtitleArrows");
        LystenClient.hitColor = (int) CONFIG.getLong("hitColor");
        LystenClient.containerExtra = CONFIG.getOrDefault("containerExtra", "❤");
        LystenClient.renderOwnNametag = CONFIG.getBoolean("renderOwnNametag");
        LystenClient.renderStuckArtifacts = CONFIG.getBoolean("renderStuckArtifacts");
        LystenClient.tutorialToasts = CONFIG.getBoolean("tutorialToasts");
        LystenClient.arrowCount = CONFIG.getBoolean("arrowCount");

        try {
            LystenClient.titleScale = Float.parseFloat(CONFIG.getOrDefault("titleScale", "1.0"));
            LystenClient.subtitleScale = Float.parseFloat(CONFIG.getOrDefault("subtitleScale", "1.0"));
        } catch (NumberFormatException e) {
            LystenClient.titleScale = 1.0f;
            LystenClient.subtitleScale = 1.0f;
        }

        LystenClient.nametagShadow = CONFIG.getBoolean("nametagShadow");
        LystenClient.nametagBg = (int) CONFIG.getLong("nametagBg");
        LystenClient.uiSeeThrough = CONFIG.getBoolean("uiSeeThrough");
        LystenClient.armorHitTint = CONFIG.getBoolean("armorHitTint");
    }
}
