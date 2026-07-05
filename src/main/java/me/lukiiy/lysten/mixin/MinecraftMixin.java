package me.lukiiy.lysten.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import me.lukiiy.lysten.Lysten;
import me.lukiiy.lysten.client.LystenClient;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelTargetBundle;
import net.minecraft.client.renderer.ShaderManager;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Shadow
    @Final
    private ShaderManager shaderManager;

    @ModifyReturnValue(method = "renderNames", at = @At("RETURN"))
    private static boolean lysten$namesInF1(boolean original) {
        return original || LystenClient.renderNamesInF1.get();
    }

    @Inject(method = "setLevel", at = @At("TAIL"))
    public void lysten$getwareShader(ClientLevel clientLevel, CallbackInfo ci) { // TODO move out of here
        LystenClient.postChain = shaderManager.getPostChain(Identifier.fromNamespaceAndPath(Lysten.MOD_ID, "shareware"), LevelTargetBundle.MAIN_TARGETS);
    }
}