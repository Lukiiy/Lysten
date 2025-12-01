package me.lukiiy.lysten.mixin;

import me.lukiiy.lysten.client.LystenClient;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.util.FormattedCharSequence;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(ChatComponent.class)
public class ChatComponentMixin {
    @ModifyConstant(method = "*", constant = @Constant(intValue = 100))
    private static int lysten$maxHistory(int original) {
        return LystenClient.maxChatHistory;
    }

    @ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/ArrayListDeque;<init>(I)V"))
    private int lysten$maxRecentHistory(int original) {
        return LystenClient.maxChatHistory;
    }

    @Redirect(method = "method_71991", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphics;drawString(Lnet/minecraft/client/gui/Font;Lnet/minecraft/util/FormattedCharSequence;III)V"))
    private void lysten$toggleShadow(GuiGraphics instance, Font font, FormattedCharSequence formattedCharSequence, int i, int j, int k) {
        instance.drawString(font, formattedCharSequence, i, j, k, LystenClient.chatShadow);
    }
}
