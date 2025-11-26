package me.lukiiy.lysten.mixin;

import me.lukiiy.lysten.Lysten;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.SubtitleOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SubtitleOverlay.class)
public class SubtitlesMixin {
    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;fill(IIIII)V"), index = 4)
    private int lysten$changeBgColor(int originalColor) {
        return Lysten.subtitlesBgColor;
    }

    @Inject(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Ljava/lang/String;III)V"), cancellable = true)
    private void lysten$noArrows(GuiGraphics guiGraphics, CallbackInfo ci) {
        if (!Lysten.subtitleArrows) ci.cancel();
    }
}