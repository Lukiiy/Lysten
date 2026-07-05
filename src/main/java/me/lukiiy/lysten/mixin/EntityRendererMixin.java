package me.lukiiy.lysten.mixin;

import net.minecraft.client.renderer.entity.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {
    @ModifyVariable(method = "extractRenderState", at = @At("HEAD"), argsOnly = true, ordinal = 0)
    private float lysten$ignoreMovementInterp(float tickDelta) {
        return 0;
    }
}
