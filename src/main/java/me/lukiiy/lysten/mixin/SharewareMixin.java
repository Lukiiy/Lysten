package me.lukiiy.lysten.mixin;

import com.mojang.blaze3d.resource.CrossFrameResourcePool;
import me.lukiiy.lysten.client.LystenClient;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.*;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(GameRenderer.class)
public class SharewareMixin {
    @Shadow
    @Final
    private Minecraft minecraft;

    @Shadow
    @Final
    private CrossFrameResourcePool resourcePool;


    @Inject(method = "render", at = @At("TAIL"))
    private void lysten$forceware(DeltaTracker deltaTracker, boolean bl, CallbackInfo ci) {
        if (LystenClient.postChain != null) LystenClient.postChain.process(this.minecraft.getMainRenderTarget(), resourcePool);
    }
}
