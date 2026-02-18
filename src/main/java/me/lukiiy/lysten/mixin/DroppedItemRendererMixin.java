package me.lukiiy.lysten.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import me.lukiiy.lysten.client.ItemEntityRenderStateAccess;
import me.lukiiy.lysten.client.LystenClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemEntityRenderer;
import net.minecraft.client.renderer.entity.state.ItemEntityRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.world.entity.item.ItemEntity;
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
    @Unique private ItemEntityRenderState lysten$state;
    @Unique private float lysten$shadowCache;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void lysten$init(EntityRendererProvider.Context context, CallbackInfo ci) {
        lysten$shadowCache = ((EntityRenderAccessor) this).getShadowRadius();
    }

    @Inject(method = "extractRenderState(Lnet/minecraft/world/entity/item/ItemEntity;Lnet/minecraft/client/renderer/entity/state/ItemEntityRenderState;F)V", at = @At("TAIL"))
    private void lysten$getStack(ItemEntity entity, ItemEntityRenderState state, float f, CallbackInfo ci) {
        ((ItemEntityRenderStateAccess) state).lysten$process(entity.getItem());
    }

    @Inject(method = "submit(Lnet/minecraft/client/renderer/entity/state/ItemEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V", at = @At("HEAD"))
    private void lysten$render(ItemEntityRenderState itemEntityRenderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState, CallbackInfo ci) {
        lysten$state = itemEntityRenderState;
    }

    @WrapOperation(method = "submit(Lnet/minecraft/client/renderer/entity/state/ItemEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;mulPose(Lorg/joml/Quaternionfc;)V"))
    private void lysten$flatten(PoseStack instance, Quaternionfc quaternionfc, Operation<Void> original, @Local(argsOnly = true) ItemEntityRenderState state) {
        if (LystenClient.itemStyle.get() == LystenClient.ItemRenderStyle.FLAT_SPRITE && (((ItemEntityRenderStateAccess)state).lysten$get2D())) {
            instance.mulPose(Axis.YP.rotationDegrees(-Minecraft.getInstance().gameRenderer.getMainCamera().yRot()));
            instance.scale(1, 1, .01f);
        } else original.call(instance, quaternionfc);
    }

    @Inject(method = "submit(Lnet/minecraft/client/renderer/entity/state/ItemEntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;Lnet/minecraft/client/renderer/state/CameraRenderState;)V", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;pushPose()V"))
    private void lysten$shadow(ItemEntityRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera, CallbackInfo ci) {
        ((EntityRenderAccessor) this).setShadowRadius(LystenClient.itemDropShadow.get() ? lysten$shadowCache : 0);
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
