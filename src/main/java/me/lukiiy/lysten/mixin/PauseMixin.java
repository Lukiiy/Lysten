package me.lukiiy.lysten.mixin;

import me.lukiiy.lysten.client.IngameConfScreen;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(PauseScreen.class)
public abstract class PauseMixin {
    @Shadow
    protected abstract Button openScreenButton(Component component, Supplier<Screen> supplier);

    @Inject(method = "createPauseMenu", at = @At("TAIL"))
    private void lysten$configBtn(CallbackInfo ci) {
        Component text = Component.translatable("lysten.config.mini");
        PauseScreen screen = (PauseScreen) (Object) this;
        Button button = openScreenButton(text, () -> new IngameConfScreen(screen));

        button.setWidth(screen.getFont().width(text) + 10);
        button.setX(10);
        button.setY(screen.height / 2);

        ((ScreenAccessor) screen).addWidgetToRender(button);
    }
}
