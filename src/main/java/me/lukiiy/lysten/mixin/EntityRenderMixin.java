package me.lukiiy.lysten.mixin;

import me.lukiiy.lysten.client.LystenClient;
import net.minecraft.client.renderer.entity.EntityRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(EntityRenderer.class)
public class EntityRenderMixin {
    @ModifyArg(method = "renderNameTag", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/Font;drawInBatch(Lnet/minecraft/network/chat/Component;FFIZLorg/joml/Matrix4f;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/client/gui/Font$DisplayMode;II)V"), index = 8)
    private int lysten$nametagBg(int originalBg) {
        return LystenClient.nametagBg == 0 ? originalBg : LystenClient.nametagBg;
    }

    @ModifyVariable(method = "renderNameTag", at = @At("STORE"), ordinal = 0)
    private boolean lysten$toggleShadow(boolean original) {
        return LystenClient.nametagShadow;
    }
}
