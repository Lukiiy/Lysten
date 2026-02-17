package me.lukiiy.lysten.mixin;

import me.lukiiy.lysten.client.LystenClient;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;

@Mixin(ChatComponent.class)
public class ChatComponentMixin {
    @ModifyConstant(method = "*", constant = @Constant(intValue = 100))
    private static int lysten$maxHistory(int original) {
        return LystenClient.maxChatHistory.get();
    }

    @ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/ArrayListDeque;<init>(I)V"))
    private int lysten$maxRecentHistory(int original) {
        return LystenClient.maxChatHistory.get();
    }

    @ModifyVariable(method = "addMessage(Lnet/minecraft/network/chat/Component;Lnet/minecraft/network/chat/MessageSignature;Lnet/minecraft/client/GuiMessageTag;)V", at = @At("HEAD"), argsOnly = true)
    private Component lysten$shadows(Component component) {
        return LystenClient.chatShadow.get() ? component : component.copy().withStyle(component.getStyle().withoutShadow());
    }
}
