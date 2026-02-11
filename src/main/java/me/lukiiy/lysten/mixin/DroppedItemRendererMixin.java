package me.lukiiy.lysten.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import me.lukiiy.lysten.client.LystenClient;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.entity.state.ItemEntityRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionfc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntityRenderer.class)
public class DroppedItemRendererMixin {
    @Unique private static ItemEntityRenderState lysten$state;
    @Unique private static ItemStack lysten$stack;
    @Unique private static float lysten$shadowCache;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void lysten$init(EntityRendererProvider.Context context, CallbackInfo ci) {
        lysten$shadowCache = ((EntityRenderAccessor) this).getShadowRadius();
    }

    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/item/ItemEntity;Lnet/minecraft/client/renderer/entity/state/ItemEntityRenderState;F)V", at = @At("HEAD"))
    private void lysten$grabStack(ItemEntity itemEntity, ItemEntityRenderState itemEntityRenderState, float f, CallbackInfo ci) {
        lysten$stack = itemEntity.getItem();
    }

    @Inject(method = "submit(Lnet/minecraft/client/renderer/entity/state/ItemEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V", at = @At("HEAD"), cancellable = true)
    private void lysten$render(ItemEntityRenderState itemEntityRenderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState, CallbackInfo ci) {
        DroppedItemRendererMixin.lysten$state = itemEntityRenderState;

        if (LystenClient.itemStyle.get() == LystenClient.ItemRenderStyle.FLAT_SPRITE && !(lysten$stack.getItem() instanceof BlockItem)) {
            ci.cancel();
            poseStack.pushPose();

            Camera camera = Minecraft.getInstance().gameRenderer.getMainCamera();
            poseStack.mulPose(Axis.YP.rotationDegrees(-camera.yRot()));
            poseStack.scale(1, 1, .01f);
            poseStack.translate(0, LystenClient.dropBobbing.get() ? (float) (Math.sin(itemEntityRenderState.ageInTicks / 10 + itemEntityRenderState.bobOffset) * .1f + .1f) : .2f, 0);

            itemEntityRenderState.item.submit(poseStack, submitNodeCollector, itemEntityRenderState.lightCoords, OverlayTexture.NO_OVERLAY, itemEntityRenderState.outlineColor);
            poseStack.popPose();
        }

        if (LystenClient.itemDropShadow.get()) ((EntityRenderAccessor) this).setShadowRadius(lysten$shadowCache);
        else ((EntityRenderAccessor) this).setShadowRadius(0);
    }

    @ModifyVariable(method = "submit(Lnet/minecraft/client/renderer/entity/state/ItemEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V", at = @At("STORE"), ordinal = 1)
    private float lysten$bobHeight(float value) {
        return LystenClient.dropBobbing.get() ? value : 0;
    }

    @Redirect(method = "submit(Lnet/minecraft/client/renderer/entity/state/ItemEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;mulPose(Lorg/joml/Quaternionfc;)V"))
    private void lysten$rotation(PoseStack instance, Quaternionfc quaternionfc) {
        EntityRenderDispatcher dispatcher = Minecraft.getInstance().getEntityRenderDispatcher();

        switch (LystenClient.itemStyle.get()) {
            case BILLBOARD -> instance.mulPose(dispatcher.camera.rotation());
            case FACE_CAMERA -> {
                Vec3 itemPos = new Vec3(lysten$state.x, lysten$state.y, lysten$state.z);
                Vec3 sub = Minecraft.getInstance().gameRenderer.getMainCamera().position().subtract(itemPos).normalize();

                float yaw = (float) Math.atan2(sub.x, sub.z);
                float pitch = (float) Math.asin(-sub.y);

                instance.mulPose(Axis.YP.rotation(yaw));
                instance.mulPose(Axis.XP.rotation(pitch));
            }
            default -> instance.mulPose(quaternionfc);
        }
    }
}
