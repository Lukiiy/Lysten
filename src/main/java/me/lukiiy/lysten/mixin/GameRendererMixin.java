package me.lukiiy.lysten.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import me.lukiiy.lysten.client.LystenClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.renderer.GameRenderer;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Environment(EnvType.CLIENT)
@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    @Shadow @Final private Minecraft minecraft;

    @Redirect(method = "renderLevel", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/OptionInstance;get()Ljava/lang/Object;"))
    private Object lysten$bob(OptionInstance<Boolean> instance) {
        if (instance == minecraft.options.bobView()) return LystenClient.screenBobbing.get() && instance.get();

        return instance.get();
    }

    @ModifyExpressionValue(method = "renderItemInHand", at = @At(value = "FIELD", target = "Lnet/minecraft/client/Options;hideGui:Z", opcode = Opcodes.GETFIELD))
    private boolean lysten$handInF1(boolean original) {
        if (LystenClient.renderHandInF1.get()) return false;

        return original;
    }
}
