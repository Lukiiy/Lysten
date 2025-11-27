package me.lukiiy.lysten.mixin;

import me.lukiiy.lysten.client.LystenClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
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
    @Inject(method = "renderBackground", at = @At("TAIL"))
    private void lysten$bg(GuiGraphics guiGraphics, int i, int j, float f, CallbackInfo ci) {
        if (LystenClient.invBlur) ((ScreenAccessor) this).renderBgBlur(guiGraphics);

        Font font = ((Screen) (Object) this).getFont();

        guiGraphics.drawString(font, Component.literal(LystenClient.containerExtra), guiGraphics.guiWidth() - font.width(LystenClient.containerExtra) - 10, guiGraphics.guiHeight() - font.lineHeight - 10, 0xFFFFFFFF, true);
    }
}
