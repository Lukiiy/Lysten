package me.lukiiy.lysten.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import me.lukiiy.lysten.client.HurtTints;
import me.lukiiy.lysten.client.LystenClient;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.CapeLayer;
import net.minecraft.client.renderer.entity.state.AvatarRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(CapeLayer.class)
public class CapeLayerMixin {
    @WrapOperation(method = "submit(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/client/renderer/entity/state/AvatarRenderState;FF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/SubmitNodeCollector;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/rendertype/RenderType;IIILnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V"))
    private <S> void lysten$tintCape(SubmitNodeCollector instance, Model<? super S> model, S o, PoseStack poseStack, RenderType renderType, int i, int j, int k, ModelFeatureRenderer.CrumblingOverlay crumblingOverlay, Operation<Void> original) {
        AvatarRenderState avatar = (AvatarRenderState) o;

        int tint = LystenClient.capeHitTint.get() ? HurtTints.get(avatar) : 0;
        if (tint == 0) {
            original.call(instance, model, o, poseStack, renderType, i, j, k, crumblingOverlay);
            return;
        }

        instance.submitModel(model, o, poseStack, renderType, i, j, tint, null, k, crumblingOverlay);
    }
}
