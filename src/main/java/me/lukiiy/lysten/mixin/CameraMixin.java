package me.lukiiy.lysten.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(Camera.class)
public class CameraMixin {
    @WrapOperation(
            method = "setup",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/Mth;lerp(DDD)D"
            )
    )
    private double noLerp(
            double delta,
            double old,
            double current,
            Operation<Double> original
    ) {
        return old;
    }

    @WrapOperation(
            method = "setup",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/Mth;lerp(FFF)F"
            )
    )
    private float noEyeLerp(
            float delta,
            float old,
            float current,
            Operation<Float> original
    ) {
        return old;
    }
}
