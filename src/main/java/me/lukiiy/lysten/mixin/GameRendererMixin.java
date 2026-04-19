package me.lukiiy.lysten.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.lukiiy.lysten.client.LystenClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.state.OptionsRenderState;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Environment(EnvType.CLIENT)
@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Redirect(method = "renderLevel", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/state/OptionsRenderState;bobView:Z", opcode = Opcodes.GETFIELD))
    private boolean lysten$bob(OptionsRenderState state) {
        return LystenClient.screenBobbing.get() && state.bobView;
    }

    @ModifyExpressionValue(method = "renderItemInHand", at = @At(value = "FIELD", target = "Lnet/minecraft/client/renderer/state/OptionsRenderState;hideGui:Z", opcode = Opcodes.GETFIELD))
    private boolean lysten$handInF1(boolean original) {
        if (LystenClient.renderHandInF1.get()) return false;

        return original;
    }
}
