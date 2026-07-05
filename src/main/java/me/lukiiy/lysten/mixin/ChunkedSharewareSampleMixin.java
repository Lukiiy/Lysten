package me.lukiiy.lysten.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.sounds.ChunkedSampleByteBuf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.nio.ByteBuffer;

@Mixin(ChunkedSampleByteBuf.class)
public abstract class ChunkedSharewareSampleMixin {
    @Unique
    private short shareware$heldSample;

    @Unique
    private int shareware$counter;

    @WrapOperation(method = "accept", at = @At(value = "INVOKE", target = "Ljava/nio/ByteBuffer;putShort(S)Ljava/nio/ByteBuffer;"))
    private ByteBuffer shareware$filter(ByteBuffer instance, short i, Operation<ByteBuffer> original) {
        if (shareware$counter == 0) {
            shareware$heldSample = (short)(i & 0xFFFC);
            shareware$counter = 15;
        } else {
            shareware$counter--;
        }

        return original.call(instance, shareware$heldSample);
    }
}