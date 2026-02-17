package me.lukiiy.lysten.client;

import net.minecraft.world.item.ItemStack;

public interface ItemEntityRenderStateAccess {
    void lysten$setItem(ItemStack item);
    ItemStack lysten$getItem();
}
