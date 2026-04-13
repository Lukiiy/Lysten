package me.lukiiy.lysten.mixin;

import me.lukiiy.lysten.client.LystenClient;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.DebugScreenOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(DebugScreenOverlay.class)
public abstract class DebugScreenOverlayMixin {
    @Redirect(method = "extractLines", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;fill(IIIII)V"))
    private void lysten$bg(GuiGraphicsExtractor instance, int x0, int y0, int x1, int y1, int col) {
        if (!LystenClient.cleanerDebugMenu.get()) instance.fill(x0, y0, x1, y1, col);
    }

    @ModifyArg(method = "extractLines", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;text(Lnet/minecraft/client/gui/Font;Ljava/lang/String;IIIZ)V"), index = 5)
    private boolean lysten$shadow(boolean original) {
        return LystenClient.cleanerDebugMenu.get() || original;
    }
}