package me.lukiiy.lysten.client;

import net.minecraft.world.entity.LivingEntity;

public class HurtContext {
    private static final ThreadLocal<LivingEntity> ENTITY_LOCAL = new ThreadLocal<>();

    public static void set(LivingEntity e) {
        ENTITY_LOCAL.set(e);
    }

    public static LivingEntity get() {
        return ENTITY_LOCAL.get();
    }

    public static void clear() {
        ENTITY_LOCAL.remove();
    }
}
