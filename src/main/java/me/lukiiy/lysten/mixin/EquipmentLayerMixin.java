package me.lukiiy.lysten.mixin;

import me.lukiiy.lysten.client.HurtContext;
import me.lukiiy.lysten.client.LystenClient;
import net.minecraft.client.renderer.entity.layers.EquipmentLayerRenderer;
import net.minecraft.util.ARGB;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(EquipmentLayerRenderer.class)
public class EquipmentLayerMixin {
    @ModifyArg(method = "renderLayers(Lnet/minecraft/client/resources/model/EquipmentClientInfo$LayerType;Lnet/minecraft/resources/ResourceKey;Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/world/item/ItemStack;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/SubmitNodeCollector;ILnet/minecraft/resources/Identifier;II)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/OrderedSubmitNodeCollector;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/rendertype/RenderType;IIILnet/minecraft/client/renderer/texture/TextureAtlasSprite;ILnet/minecraft/client/renderer/feature/ModelFeatureRenderer$CrumblingOverlay;)V"), index = 6)
    private int lysten$hurtArmorColor(int original) {
        if (!LystenClient.armorHitTint.get()) return original;

        int color = LystenClient.hitColor.get();
        if (color == 0 || ARGB.alpha(color) == 0) return original;

        LivingEntity entity = HurtContext.get();
        if (entity != null && entity.hurtTime > 0) return color;

        return original;
    }
}
