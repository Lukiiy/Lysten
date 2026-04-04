package me.lukiiy.lysten.client;

import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

public class HurtTints {
    private static final Map<LivingEntityRenderState, Integer> damaged = Collections.synchronizedMap(new WeakHashMap<>());

    public static void set(LivingEntityRenderState state, int tint) {
        damaged.put(state, tint);
    }

    public static int get(LivingEntityRenderState state) {
        return damaged.getOrDefault(state, 0);
    }

    public static void remove(LivingEntityRenderState state) {
        damaged.remove(state);
    }
}