package me.lukiiy.lysten.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import me.lukiiy.lysten.Lysten;
import me.lukiiy.lysten.client.HurtContext;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(EquipmentLayerRenderer.class)
public class EquipmentLayerMixin {
    @Redirect(method = "renderLayers(Lnet/minecraft/client/resources/model/EquipmentClientInfo$LayerType;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/client/model/Model;Lnet/minecraft/world/item/ItemStack;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/resources/ResourceLocation;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/model/Model;renderToBuffer(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;III)V"))
    private void lysten$hurtArmorTint(Model instance, PoseStack poseStack, VertexConsumer vertexConsumer, int i, int j, int k) {
        LivingEntity entity = HurtContext.get();
        if (entity != null && entity.hurtTime > 0) k = Lysten.hitColor;

        instance.renderToBuffer(poseStack, vertexConsumer, i, j, k);
    }
}
