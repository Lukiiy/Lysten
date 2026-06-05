package me.lukiiy.lysten.client;

import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.item.ItemStack;

public interface ItemEntityRenderStateAccess {
    void lysten$process(ItemStack item, ItemStackRenderState state);
    boolean lysten$get2D();
}
