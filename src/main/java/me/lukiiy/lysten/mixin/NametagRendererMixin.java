package me.lukiiy.lysten.mixin;

import me.lukiiy.lysten.client.LystenClient;
import net.minecraft.client.renderer.feature.NameTagFeatureRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(NameTagFeatureRenderer.class)
public class NametagRendererMixin {
    @ModifyArg(method = "renderTranslucent", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Font;drawInBatch(Lnet/minecraft/network/chat/Component;FFIZLorg/joml/Matrix4fc;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/client/gui/Font$DisplayMode;II)V"), index = 8)
    private int lysten$nametagBg(int originalBg) {
        int bg = LystenClient.nametagBg.get();

        return bg == 0 ? originalBg : bg;
    }

    @ModifyArg(method = "renderTranslucent", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Font;drawInBatch(Lnet/minecraft/network/chat/Component;FFIZLorg/joml/Matrix4fc;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/client/gui/Font$DisplayMode;II)V"), index = 4)
    private boolean lysten$toggleShadow(boolean original) {
        return LystenClient.nametagShadow.get();
    }
}
