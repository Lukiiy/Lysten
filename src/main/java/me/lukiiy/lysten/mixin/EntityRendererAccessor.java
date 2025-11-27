package me.lukiiy.lysten.mixin;

import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EntityRenderer.class)
public interface EntityRendererAccessor {
    @Accessor("entityRenderDispatcher")
    EntityRenderDispatcher getDispatcher();

    @Accessor("shadowRadius")
    void setShadowRadius(float value);
}
