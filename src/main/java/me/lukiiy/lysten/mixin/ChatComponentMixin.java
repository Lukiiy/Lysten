package me.lukiiy.lysten.mixin;

import me.lukiiy.lysten.client.LystenClient;
import net.minecraft.client.gui.components.ChatComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

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
}
