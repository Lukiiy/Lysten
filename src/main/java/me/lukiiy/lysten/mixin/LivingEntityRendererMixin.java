package me.lukiiy.lysten.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import me.lukiiy.lysten.Lysten;
import me.lukiiy.lysten.client.HurtContext;
import me.lukiiy.lysten.client.LystenClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<S extends LivingEntityRenderState> {
    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V", at = @At("TAIL"))
    private void lysten$renderHurtOverlay(LivingEntity entity, LivingEntityRenderState state, float partialTicks, CallbackInfo ci) {
        if (entity.hurtTime > 0) state.hasRedOverlay = !LystenClient.isColorTransparent(Lysten.hitColor);
    }

    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;F)V", at = @At("HEAD"))
    private void lysten$getHurtEntity(LivingEntity entity, LivingEntityRenderState state, float partialTicks, CallbackInfo ci) {
        if (entity.hurtTime > 0) HurtContext.set(entity);
    }

    @Inject(method = "render(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At("TAIL"))
    private void lysten$clearHurtEntity(S livingEntityRenderState, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, CallbackInfo ci) {
        HurtContext.clear();
    }

    @Redirect(method = "render(Lnet/minecraft/client/renderer/entity/state/LivingEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/EntityModel;renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;III)V"))
    private void lysten$changeHurtOverlay(EntityModel<S> instance, PoseStack poseStack, VertexConsumer vertexConsumer, int i, int j, int k, S livingEntityRenderState) {
        if (livingEntityRenderState.hasRedOverlay) k = Lysten.hitColor;

        instance.renderToBuffer(poseStack, vertexConsumer, i, j, k);
    }

    @ModifyExpressionValue(method = "shouldShowName(Lnet/minecraft/world/entity/LivingEntity;D)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/Minecraft;getCameraEntity()Lnet/minecraft/world/entity/Entity;"))
    private Entity lysten$renderOwnNametag(Entity original) {
        return Lysten.renderOwnNametag && !(Minecraft.getInstance().screen instanceof AbstractContainerScreen<?>) ? null : original;
    }
}
