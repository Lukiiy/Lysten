package me.lukiiy.lysten.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import me.lukiiy.lysten.client.LystenClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.feature.NameTagFeatureRenderer;
import org.joml.Quaternionf;
import org.joml.Quaternionfc;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(NameTagFeatureRenderer.Storage.class)
public class NameTagFeatureStorageMixin {
    @WrapOperation(method = "add", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/PoseStack;mulPose(Lorg/joml/Quaternionfc;)V", ordinal = 0))
    private void lysten$billboard(PoseStack instance, Quaternionfc quaternionfc, Operation<Void> original) {
        if (!LystenClient.billboardedNametags.get()) {
            original.call(instance, quaternionfc);
            return;
        }

        original.call(instance, new Quaternionf().rotationY((float) Math.toRadians(180 - Minecraft.getInstance().getEntityRenderDispatcher().camera.yRot())));
    }
}
