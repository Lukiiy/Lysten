package me.lukiiy.lysten.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import me.lukiiy.lysten.client.LystenClient;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.state.HitboxRenderState;
import net.minecraft.client.renderer.entity.state.HitboxesRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatchMixin {
    @Shadow
    protected static void renderHitbox(PoseStack poseStack, VertexConsumer vertexConsumer, HitboxRenderState hitboxRenderState) {}

    @Inject(method = "renderHitboxesAndViewVector", at = @At("HEAD"), cancellable = true)
    private static void lysten$onlyRenderHitboxes(PoseStack poseStack, HitboxesRenderState hitboxesRenderState, VertexConsumer vertexConsumer, float f, CallbackInfo ci) {
        if (!LystenClient.cleanerHitboxes.get()) return;

        for (HitboxRenderState hitbox : hitboxesRenderState.hitboxes()) renderHitbox(poseStack, vertexConsumer, hitbox);

        ci.cancel();
    }
}
