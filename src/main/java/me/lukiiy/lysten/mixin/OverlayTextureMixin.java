package me.lukiiy.lysten.mixin;

import me.lukiiy.lysten.Lysten;
import me.lukiiy.lysten.client.LystenClient;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(OverlayTexture.class)
public class OverlayTextureMixin {
    @Shadow @Final private DynamicTexture texture;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void lysten$getVanilla(CallbackInfo ci) {
        LystenClient.vanillaHitColor = texture.getPixels().getPixel(0, OverlayTexture.RED_OVERLAY_V);
        Lysten.LOGGER.info(String.valueOf(LystenClient.vanillaHitColor));
    }
}
