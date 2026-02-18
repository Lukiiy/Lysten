package me.lukiiy.lysten.mixin;

import me.lukiiy.lysten.client.ItemEntityRenderStateAccess;
import net.minecraft.client.renderer.entity.state.ItemEntityRenderState;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ItemEntityRenderState.class)
public class ItemEntityRenderStateMixin implements ItemEntityRenderStateAccess {
    @Unique private boolean lysten$2d = true;

    @Unique
    @Override
    public void lysten$process(ItemStack item) {
        if (item.getItem() instanceof BlockItem) lysten$2d = false;
    }

    @Override
    public boolean lysten$get2D() {
        return lysten$2d;
    }
}