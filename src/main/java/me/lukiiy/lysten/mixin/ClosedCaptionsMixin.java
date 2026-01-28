package me.lukiiy.lysten.mixin;

import me.lukiiy.lysten.client.LystenClient;
import net.minecraft.client.gui.components.SubtitleOverlay;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(SubtitleOverlay.class)
public class ClosedCaptionsMixin {
    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;fill(IIIII)V"), index = 4)
    private int lysten$changeBgColor(int originalColor) {
        int bg = LystenClient.subtitlesBgColor.get();

        return bg != 0 ? bg : originalColor;
    }

    @ModifyArg(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Ljava/lang/String;III)V"), index = 1)
    private String lysten$noArrows(String text) {
        if (!LystenClient.subtitleArrows.get() && (text.equals(">") || text.equals("<"))) return "";

        return text;
    }
}