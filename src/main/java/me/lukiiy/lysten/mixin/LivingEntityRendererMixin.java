package me.lukiiy.lysten.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import me.lukiiy.lysten.client.HurtTints;
import me.lukiiy.lysten.client.LystenClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.HashMap;
import java.util.Map;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<S extends LivingEntityRenderState> {
    @Unique private static final Map<LivingEntityRenderState, Float> lysten$HURT = new HashMap<>();

    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V", at = @At("TAIL"))
    private void lysten$renderHurtOverlay(LivingEntity entity, S state, float partialTicks, CallbackInfo ci) {
        boolean isHurt = entity.hurtTime > 0 || entity.deathTime > 0;
        int color = LystenClient.hitColor.get();

        if (color != 0) state.hasRedOverlay = isHurt && ARGB.alpha(color) != 0;

        if (isHurt) HurtTints.set(state, color != 0 ? color : LystenClient.vanillaHitColor);
        else HurtTints.remove(state);

        if (LystenClient.survivalTestHurt.get() && entity.hurtTime > 0) lysten$HURT.put(state, entity.hurtTime - partialTicks);
        else lysten$HURT.remove(state);
    }

    @Inject(method = "getShadowRadius(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;)F", at = @At("HEAD"), cancellable = true)
    private void lysten$shadow(S state, CallbackInfoReturnable<Float> cir) {
        if (state.deathTime > 0) cir.setReturnValue(0f);
    }

    @Inject(method = "submit(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V", at = @At("HEAD"), cancellable = true)
    private void lysten$cancelRender(S state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera, CallbackInfo ci) {
        if (LystenClient.deathAnimStyle.get() == LystenClient.DeathAnimationStyle.INVISIBLE && state.deathTime > 0) {
            HurtTints.remove(state);
            lysten$HURT.remove(state);

            ci.cancel();
        }
    }

    @Inject(method = "submit(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V", at = @At("TAIL"))
    private void lysten$clearHurtEntity(S state, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState camera, CallbackInfo ci) {
        HurtTints.remove(state);
        lysten$HURT.remove(state);
    }

    @ModifyArgs(method = "submit(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/level/CameraRenderState;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/rendertype/RenderType;IIILnet/minecraft/client/renderer/texture/TextureAtlasSprite;ILnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V"))
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
        return LystenClient.renderOwnNametag.get() && !(Minecraft.getInstance().gui.screen() instanceof AbstractContainerScreen<?>) ? null : original;
    }

    @Inject(method = "setupRotations", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;mulPose(Lorg/joml/Quaternionfc;)V", ordinal = 1), cancellable = true, locals = LocalCapture.CAPTURE_FAILSOFT)
    private void lysten$deathRot(S state, PoseStack poseStack, float bodyRot, float entityScale, CallbackInfo ci, float h) {
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

    @Inject(method = "setupRotations", at = @At("TAIL"))
    private void lysten$survivaltestHurtAnim(S state, PoseStack poseStack, float bodyRot, float entityScale, CallbackInfo ci) { // TODO
        Float hurt = lysten$HURT.get(state);
        if (hurt == null || hurt <= 0) return;

        float progress = 1 - (hurt / 10f);
        float snapEnd = .15f;
        float holdEnd = .35f;
        float angle;

        if (progress < snapEnd) {
            float delta = progress / snapEnd;

            angle = delta * 28;
        } else if (progress < holdEnd) {
            angle = 28;
        } else {
            float delta = (progress - holdEnd) / (1 - holdEnd);

            angle = (1 - delta) * 28;
        }

        float pivotY = state.boundingBoxHeight * .2f;

        poseStack.translate(0, pivotY, 0);
        poseStack.mulPose(Axis.XP.rotationDegrees(angle));
        poseStack.translate(0, -pivotY, 0);
    }

    @ModifyExpressionValue(method = "shouldShowName(Lnet/minecraft/world/entity/LivingEntity;D)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Hud;isHidden()Z"))
    private boolean lysten$namesInF1(boolean hidden) {
        if (LystenClient.renderNamesInF1.get()) return false;

        return hidden;
    }
}
