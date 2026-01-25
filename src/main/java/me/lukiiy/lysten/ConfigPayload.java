package me.lukiiy.lysten;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ConfigPayload(String data) implements CustomPacketPayload {
    public static final ResourceLocation ID = ResourceLocation.fromNamespaceAndPath(Lysten.MOD_ID, "server_config");
    public static final Type<ConfigPayload> TYPE = new Type<>(ID);
    public static final StreamCodec<FriendlyByteBuf, ConfigPayload> CODEC = StreamCodec.of((buf, payload) -> buf.writeUtf(payload.data), buf -> new ConfigPayload(buf.readUtf()));

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
