package me.lukiiy.lysten.mixin;

import me.lukiiy.lysten.client.LystenClient;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {
    @ModifyVariable(method = "extractBlockOutline", at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/level/block/state/BlockState;getShape(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/phys/shapes/CollisionContext;)Lnet/minecraft/world/phys/shapes/VoxelShape;"), name = "shape")
    private VoxelShape lysten$changeShape(VoxelShape shape) {
        return LystenClient.blockOutlineFull.get() ? Shapes.block() : shape;
    }

    @ModifyArg(method = "renderHitOutline", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ShapeRenderer;renderShape(Lcom/mojang/blaze3d/vertex/PoseStack;Lcom/mojang/blaze3d/vertex/VertexConsumer;Lnet/minecraft/world/phys/shapes/VoxelShape;DDDIF)V"), index = 6)
    private int lysten$changeColor(int i) {
        int color = LystenClient.blockOutlineColor.get();

        return color != 0 ? color : i;
    }
}
