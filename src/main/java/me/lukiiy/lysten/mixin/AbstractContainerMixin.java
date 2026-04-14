package me.lukiiy.lysten.mixin;

import me.lukiiy.lysten.client.LystenClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(AbstractContainerScreen.class)
public class AbstractContainerMixin {
    @Inject(method = "extractRenderState", at = @At("TAIL"))
    private void lysten$bg(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a, CallbackInfo ci) {
        Font font = ((Screen) (Object) this).getFont();
        Component good = LystenClient.parseText(LystenClient.containerExtra.get());

        graphics.text(font, good, graphics.guiWidth() - font.width(good) - 10, graphics.guiHeight() - font.lineHeight - 10, 0xFFFFFFFF, true);
    }
}
