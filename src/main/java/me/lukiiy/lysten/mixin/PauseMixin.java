package me.lukiiy.lysten.mixin;

import me.lukiiy.lysten.client.IngameConfScreen;
import me.lukiiy.lysten.client.LystenClient;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
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

    @Inject(method = "renderBackground", at = @At("TAIL"))
    private void lysten$bg(GuiGraphics guiGraphics, int i, int j, float f, CallbackInfo ci) {
        if (!LystenClient.containerExtraPause.get()) return;

        Font font = ((Screen) (Object) this).getFont();
        Component good = Component.literal(LystenClient.containerExtra.get());

        guiGraphics.drawString(font, good, guiGraphics.guiWidth() - font.width(good) - 10, guiGraphics.guiHeight() - font.lineHeight - 10, 0xFFFFFFFF, true);
    }
}
