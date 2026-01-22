package me.lukiiy.lysten.mixin;

import me.lukiiy.lysten.client.LystenClient;
import net.minecraft.client.renderer.*;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {
    @ModifyArg(method = "renderHitOutline", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ShapeRenderer;renderShape(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/world/phys/shapes/VoxelShape;DDDI)V", ordinal = 0), index = 2)
    private VoxelShape lysten$changeShape(VoxelShape voxelShape) {
        return LystenClient.blockOutlineFull ? Shapes.block() : voxelShape;
    }

    @ModifyArg(method = "renderBlockOutline", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/LevelRenderer;renderHitOutline(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/world/entity/Entity;DDDLnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)V", ordinal = 0), index = 8)
    private int lysten$changeColor(int i) {
        return LystenClient.blockOutlineColor != 0 ? LystenClient.blockOutlineColor : i;
    }
}
