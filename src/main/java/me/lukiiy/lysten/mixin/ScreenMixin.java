package me.lukiiy.lysten.mixin;

import me.lukiiy.lysten.client.LystenClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public abstract class ScreenMixin {
    @Shadow @Nullable protected Minecraft minecraft;
    @Shadow protected abstract void renderPanorama(GuiGraphics guiGraphics, float f);
    @Shadow protected abstract void renderBlurredBackground(GuiGraphics guiGraphics);

    @Shadow
    protected abstract void renderMenuBackground(GuiGraphics guiGraphics);

    @Inject(method = "renderBackground", at = @At("HEAD"), cancellable = true)
    private void lysten$renderBg(GuiGraphics guiGraphics, int i, int j, float f, CallbackInfo ci) { // TODO
        if ((!LystenClient.uiSeeThrough && !(minecraft.screen instanceof PauseScreen)) || minecraft.level == null) renderPanorama(guiGraphics, f);

        renderBlurredBackground(guiGraphics);
        renderMenuBackground(guiGraphics);
        ci.cancel();
    }
}
