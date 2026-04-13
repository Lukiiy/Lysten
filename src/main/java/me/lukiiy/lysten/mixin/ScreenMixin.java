package me.lukiiy.lysten.mixin;

import me.lukiiy.lysten.client.LystenClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
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

    @Inject(method = "extractBackground", at = @At("HEAD"), cancellable = true)
    private void lysten$renderBg(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a, CallbackInfo ci) {
        if ((!LystenClient.uiSeeThrough.get() && !(minecraft.screen instanceof PauseScreen)) || minecraft.level == null) {
            extractPanorama(graphics, a);
            ci.cancel();
        }
    }
}