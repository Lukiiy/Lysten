package me.lukiiy.lysten.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import me.lukiiy.lysten.Lysten;
import me.lukiiy.lysten.client.IngameConfScreen;
import me.lukiiy.lysten.client.LystenClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.SpriteIconButton;
import net.minecraft.client.gui.layouts.LinearLayout;
import net.minecraft.client.gui.screens.PauseScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.social.SocialInteractionsScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(PauseScreen.class)
public abstract class PauseMixin {
    @Shadow
    protected abstract Button openScreenButton(Component message, Supplier<Screen> newScreen);

    @Inject(method = "createPauseMenu", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/layouts/LinearLayout;addChild(Lnet/minecraft/client/gui/layouts/LayoutElement;)Lnet/minecraft/client/gui/layouts/LayoutElement;", shift = At.Shift.AFTER, ordinal = 3))
    private void lysten$configBtn(CallbackInfo ci, @Local(name = "iconButtonRow") LinearLayout iconButtonRow) {
        iconButtonRow.addChild(SpriteIconButton.builder(Component.translatable("lysten.config.mini"), _ -> Minecraft.getInstance().setScreenAndShow(new IngameConfScreen((PauseScreen) (Object) this)), true).width(20).sprite(Identifier.fromNamespaceAndPath(Lysten.MOD_ID, "button"), 15, 15).withTootip().build());
    }

    @Inject(method = "extractBackground", at = @At("TAIL"))
    private void lysten$bg(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float a, CallbackInfo ci) {
        if (!LystenClient.containerExtraPause.get()) return;

        Font font = ((Screen) (Object) this).getFont();
        Component good = LystenClient.parseText(LystenClient.containerExtra.get());

        graphics.text(font, good, graphics.guiWidth() - font.width(good) - 10, graphics.guiHeight() - font.lineHeight - 10, 0xFFFFFFFF, true);
    }
}
