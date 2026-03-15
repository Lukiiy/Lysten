package me.lukiiy.lysten.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import me.lukiiy.lysten.client.LystenClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.ScreenEffectRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.GameType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ScreenEffectRenderer.class)
public class ScreenEffectRenderMixin {
    @Inject(method = "renderFire", at = @At("HEAD"), cancellable = true)
    private static void lysten$cancelFireOverlay(PoseStack poseStack, MultiBufferSource multiBufferSource, TextureAtlasSprite textureAtlasSprite, CallbackInfo ci) {
        if (!LystenClient.filteredFireLayer.get()) return;

        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) return;

        if (player.isCreative() || player.hasEffect(MobEffects.FIRE_RESISTANCE)) ci.cancel();
    }
}