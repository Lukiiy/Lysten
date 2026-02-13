package me.lukiiy.lysten.mixin;

import me.lukiiy.lysten.client.ItemEntityRenderStateAccess;
import net.minecraft.client.renderer.entity.state.ItemEntityRenderState;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ItemEntityRenderState.class)
public class ItemEntityRenderStateMixin implements ItemEntityRenderStateAccess {
    @Unique private ItemStack lysten$item;

    @Override
    public void lysten$setItem(ItemStack item) {
        lysten$item = item;
    }

    @Override
    public ItemStack lysten$getItem() {
        return lysten$item;
    }
}