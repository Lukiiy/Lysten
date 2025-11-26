package me.lukiiy.lysten.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import me.lukiiy.lysten.Lysten;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.entity.state.ItemEntityRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionfc;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntityRenderer.class)
public class DroppedItemRendererMixin {
    @Unique private static ItemEntityRenderState state;

    @Inject(method = "render(Lnet/minecraft/client/renderer/entity/state/ItemEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At("HEAD"), cancellable = true)
    private void lysten$render(ItemEntityRenderState state, PoseStack poseStack, MultiBufferSource buffers, int light, CallbackInfo ci) {
        DroppedItemRendererMixin.state = state;

        if (Lysten.itemStyle == Lysten.ItemRenderStyle.FLAT_SPRITE) {
            ci.cancel();
            poseStack.pushPose();

            Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
            poseStack.mulPose(Axis.YP.rotationDegrees(-camera.getYRot()));
            poseStack.scale(1f, 1f, 0.1f);

            state.item.render(poseStack, buffers, light, OverlayTexture.NO_OVERLAY);
            poseStack.popPose();
        }

        EntityRendererAccessor accessor = (EntityRendererAccessor) this;

        if (!Lysten.itemDropShadow) accessor.setShadowRadius(0f);

    }

    @ModifyVariable(method = "render(Lnet/minecraft/client/renderer/entity/state/ItemEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At("STORE"), ordinal = 1)
    private float lysten$modifyBobHeight(float value) {
        return Lysten.dropBobbing ? value : 0f;
    }

    @Redirect(method = "render(Lnet/minecraft/client/renderer/entity/state/ItemEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;mulPose(Lorg/joml/Quaternionfc;)V"))
    private void lysten$rotation(PoseStack instance, Quaternionfc quaternionfc) {
        EntityRenderDispatcher dispatcher = ((EntityRendererAccessor) this).getDispatcher();

        switch (Lysten.itemStyle) {
            case BILLBOARD -> instance.mulPose(dispatcher.cameraOrientation());
            case FACE_CAMERA -> {
                Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
                Vec3 itemPos = new Vec3(state.x, state.y, state.z);

                Vec3 sub = camera.getPosition().subtract(itemPos).normalize();

                float yaw = (float) Math.atan2(sub.x, sub.z);
                float pitch = (float) Math.asin(-sub.y);

                instance.mulPose(Axis.YP.rotation(yaw));
                instance.mulPose(Axis.XP.rotation(pitch));
            }
            default -> instance.mulPose(quaternionfc);
        }
    }
}
