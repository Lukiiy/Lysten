package me.lukiiy.lysten.mixin;

import me.lukiiy.lysten.client.ItemEntityRenderStateAccess;
import net.minecraft.client.renderer.entity.state.ItemEntityRenderState;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ItemEntityRenderState.class)
public class ItemEntityRenderStateMixin implements ItemEntityRenderStateAccess {
    @Unique private boolean lysten$2d = true;

    @Unique
    @Override
    public void lysten$process(ItemStack item, ItemStackRenderState state) {
        lysten$2d = !(item.getItem() instanceof BlockItem) || !state.usesBlockLight();
    }

    @Override
    public boolean lysten$get2D() {
        return lysten$2d;
    }
}