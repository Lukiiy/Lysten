package me.lukiiy.lysten.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import me.lukiiy.lysten.client.LystenClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.FishingHookRenderer;
import net.minecraft.client.renderer.entity.state.FishingHookRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(FishingHookRenderer.class)
public class FishingHookRenderMixin {
    @Unique
    private static final double lysten$BOBBER_TRESHOLD = Math.pow(.75, 2);

    @Redirect(method = "submit(Lnet/minecraft/client/renderer/entity/state/FishingHookRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitCustomGeometry(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/rendertype/RenderType;Lnet/minecraft/client/renderer/SubmitNodeCollector$CustomGeometryRenderer;)V", ordinal = 0))
    private void lysten$hideBobberSprite(SubmitNodeCollector instance, PoseStack poseStack, RenderType renderType, SubmitNodeCollector.CustomGeometryRenderer customGeometryRenderer, FishingHookRenderState state) {
        if (LystenClient.hideCloseBobbers.get() && Minecraft.getInstance().options.getCameraType().isFirstPerson() && state.distanceToCameraSq < lysten$BOBBER_TRESHOLD) return;

        instance.submitCustomGeometry(poseStack, renderType, customGeometryRenderer);
    }
}