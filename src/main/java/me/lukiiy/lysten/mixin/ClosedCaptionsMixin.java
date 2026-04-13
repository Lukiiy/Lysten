package me.lukiiy.lysten.mixin;

import me.lukiiy.lysten.client.LystenClient;
import net.minecraft.client.gui.components.SubtitleOverlay;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.WeighedSoundEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;

@Mixin(SubtitleOverlay.class)
public class ClosedCaptionsMixin {
    @Unique
    private static final String[] lysten$envIds = {"weather.rain", "ambient.cave", "ambient.sound"};

    @ModifyArg(method = "extractRenderState", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;fill(IIIII)V"), index = 4)
    private int lysten$changeBgColor(int originalColor) {
        int bg = LystenClient.subtitlesBgColor.get();

        return bg != 0 ? bg : originalColor;
    }

    @ModifyArg(method = "extractRenderState", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;text(Lnet/minecraft/client/gui/Font;Ljava/lang/String;III)V"), index = 1)
    private String lysten$noArrows(String text) {
        if (!LystenClient.subtitleArrows.get() && (text.equals(">") || text.equals("<"))) return "";

        return text;
    }

    @Inject(method = "onPlaySound", at = @At("HEAD"), cancellable = true)
    private void lysten$play(SoundInstance sound, WeighedSoundEvents soundEvent, float range, CallbackInfo ci) {
        String key = sound.getIdentifier().toLanguageKey().replace("minecraft.", "");

        if (LystenClient.playerlessSubtitles.get() && key.startsWith("entity.player")) ci.cancel();

        if (LystenClient.envlessSubtitles.get() && Arrays.stream(lysten$envIds).anyMatch(key::startsWith)) ci.cancel();
    }
}