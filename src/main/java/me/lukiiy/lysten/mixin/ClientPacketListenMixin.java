package me.lukiiy.lysten.mixin;

import me.lukiiy.lysten.ConfigPayload;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenMixin {
    @Inject(method = "handleCustomPayload", at = @At("HEAD"), cancellable = true)
    private void lysten$handlePacket(CustomPacketPayload payload, CallbackInfo ci) {
        if (payload.type().id().equals(ConfigPayload.ID)) return;

        ci.cancel();
    }
}
