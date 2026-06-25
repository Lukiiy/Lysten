package me.lukiiy.lysten.mixin;

import me.lukiiy.lysten.client.LystenClient;
import net.minecraft.client.renderer.feature.NameTagFeatureRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(NameTagFeatureRenderer.class)
public class NametagRendererMixin {
    @ModifyArg(method = "prepareText", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Font;prepareText(Lnet/minecraft/util/FormattedCharSequence;FFIZZI)Lnet/minecraft/client/gui/Font$PreparedText;"), index = 6)
    private static int lysten$nametagBg(int originalBg) {
        int bg = LystenClient.nametagBg.get();

        return bg == 0 ? originalBg : bg;
    }

    @ModifyArg(method = "prepareText", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Font;prepareText(Lnet/minecraft/util/FormattedCharSequence;FFIZZI)Lnet/minecraft/client/gui/Font$PreparedText;"), index = 4)
    private static boolean lysten$toggleShadow(boolean original) {
        return LystenClient.nametagShadow.get();
    }
}
