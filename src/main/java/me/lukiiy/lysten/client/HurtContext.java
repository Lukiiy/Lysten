package me.lukiiy.lysten.client;

import net.minecraft.world.entity.LivingEntity;

public class HurtContext {
    private static final ThreadLocal<LivingEntity> THREAD_LOCAL = new ThreadLocal<>();

    public static void set(LivingEntity e) {
        THREAD_LOCAL.set(e);
    }

    public static LivingEntity get() {
        return THREAD_LOCAL.get();
    }

    public static void clear() {
        THREAD_LOCAL.remove();
    }
}
