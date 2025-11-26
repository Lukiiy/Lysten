package me.lukiiy.lysten.client;

import net.fabricmc.api.ClientModInitializer;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.LivingEntity;

public class LystenClient implements ClientModInitializer {
    public static LivingEntity hurtContextEntity = null;

    @Override
    public void onInitializeClient() {
    }

    public static boolean isColorTransparent(int color) {
        return ARGB.alpha(color) == 0;
    }
}
