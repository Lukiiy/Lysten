package me.lukiiy.lysten.mixin;

import me.lukiiy.lysten.client.IngameConfScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PauseScreen.class)
public abstract class PauseMixin {
    @Inject(method = "createPauseMenu", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/layouts/GridLayout;visitWidgets(Ljava/util/function/Consumer;)V", shift = At.Shift.AFTER))
    private void lysten$configBtn(CallbackInfo ci) {
        PauseScreen screen = (PauseScreen) (Object) this;

        ((ScreenAccessor) screen).addWidgetToRender(Button.builder(Component.translatable("lysten.config.mini"), b -> Minecraft.getInstance().setScreen(new IngameConfScreen(screen))).bounds(screen.width - 70, screen.height - 30, 60, 20).build());
    }
}
