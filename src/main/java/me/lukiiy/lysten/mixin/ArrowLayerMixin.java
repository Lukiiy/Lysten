package me.lukiiy.lysten.mixin;

import me.lukiiy.lysten.client.LystenClient;
import net.minecraft.client.renderer.entity.layers.ArrowLayer;
import net.minecraft.client.renderer.entity.state.PlayerRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ArrowLayer.class)
public class ArrowLayerMixin {
    @Inject(method = "numStuck", at = @At("HEAD"), cancellable = true)
    private void lysten$noStuckArrows(PlayerRenderState playerRenderState, CallbackInfoReturnable<Integer> cir) {
        if (!LystenClient.renderStuckArrows) cir.setReturnValue(0);
    }
}
