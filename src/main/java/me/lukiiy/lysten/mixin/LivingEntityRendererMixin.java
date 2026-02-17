package me.lukiiy.lysten.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import me.lukiiy.lysten.client.HurtContext;
import me.lukiiy.lysten.client.LystenClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<S extends LivingEntityRenderState> {
    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V", at = @At("TAIL"))
    private void lysten$renderHurtOverlay(LivingEntity entity, LivingEntityRenderState state, float partialTicks, CallbackInfo ci) {
        int color = LystenClient.hitColor.get();

        if (color != 0 && entity.hurtTime > 0) state.hasRedOverlay = ARGB.alpha(color) != 0;
    }

    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V", at = @At("HEAD"))
    private void lysten$getHurtEntity(LivingEntity entity, LivingEntityRenderState state, float partialTicks, CallbackInfo ci) {
        if (LystenClient.hitColor.get() != 0 && entity.hurtTime > 0) HurtContext.set(entity);
    }

    @Inject(method = "submit(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V", at = @At("TAIL"))
    private void lysten$clearHurtEntity(S livingEntityRenderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState, CallbackInfo ci) {
        HurtContext.clear();
    }

    @ModifyArgs(method = "submit(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/rendertype/RenderType;IIILnet/minecraft/client/renderer/texture/TextureAtlasSprite;ILnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V"))
    private void lysten$tintHurtOverlay(Args args) { // safe?
        if (!(args.get(1) instanceof LivingEntityRenderState state)) return;

        int color = LystenClient.hitColor.get();

        if (color == 0 || ARGB.alpha(color) == 0) return;
        if (!state.hasRedOverlay) return;

        args.set(5, OverlayTexture.NO_OVERLAY);
        args.set(6, color);
    }

    @ModifyExpressionValue(method = "shouldShowName(Lnet/minecraft/world/entity/LivingEntity;D)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;getCameraEntity()Lnet/minecraft/world/entity/Entity;"))
    private Entity lysten$renderOwnNametag(Entity original) {
        return LystenClient.renderOwnNametag.get() && !(Minecraft.getInstance().screen instanceof AbstractContainerScreen<?>) ? null : original;
    }

    @Inject(method = "setupRotations", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;mulPose(Lorg/joml/Quaternionfc;)V", ordinal = 1), cancellable = true, locals = LocalCapture.CAPTURE_FAILSOFT)
    private void lysten$deathRot(S livingEntityRenderState, PoseStack poseStack, float f, float g, CallbackInfo ci, float h) {
        switch (LystenClient.deathAnimStyle.get()) {
            case NONE -> {
                poseStack.mulPose(Axis.YP.rotationDegrees(0));
                ci.cancel();
            }

            case FALLBACK -> {
                poseStack.mulPose(Axis.XN.rotationDegrees(h * -90));
                ci.cancel();
            }

            case null, default -> {}
        }
    }
}
