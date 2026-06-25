package me.lukiiy.lysten.mixin;

import me.lukiiy.lysten.client.LystenClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Screen.class)
public abstract class ScreenMixin {
    @Shadow @Final @Nullable protected Minecraft minecraft;

    @Shadow protected abstract void extractPanorama(GuiGraphicsExtractor graphics, float a);

    @Shadow
    protected abstract void extractBlurredBackground(GuiGraphicsExtractor graphics);

    @Inject(method = "extractBackground", at = @At("HEAD"), cancellable = true)
    private void lysten$renderBg(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a, CallbackInfo ci) {
        if (minecraft != null && ((!LystenClient.uiSeeThrough.get() && !(minecraft.gui.screen() instanceof PauseScreen)) || minecraft.level == null)) {
            extractPanorama(graphics, a);
            ci.cancel();
        }
    }

    @Inject(method = "extractBackground", at = @At("TAIL"))
    private void lysten$blurBg(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a, CallbackInfo ci) {
        if (minecraft != null && minecraft.gui.screen() instanceof AbstractContainerScreen<?> && LystenClient.invBlur.get()) extractBlurredBackground(graphics);
    }
}