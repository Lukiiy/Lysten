package me.lukiiy.lysten.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import me.lukiiy.lysten.client.LystenClient;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @ModifyReturnValue(method = "renderNames", at = @At("RETURN"))
    private static boolean lysten$namesInF1(boolean original) {
        return original || LystenClient.renderNamesInF1.get();
    }
}