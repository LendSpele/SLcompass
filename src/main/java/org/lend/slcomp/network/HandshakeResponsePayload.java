package org.lend.slcomp.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record HandshakeResponsePayload(int nonce) implements CustomPayload {
    public static final CustomPayload.Id<HandshakeResponsePayload> ID =
            new CustomPayload.Id<>(Identifier.of("slcomp", "handshake_response"));

    public static final PacketCodec<ByteBuf, HandshakeResponsePayload> CODEC =
            PacketCodec.tuple(
                    PacketCodecs.INTEGER, HandshakeResponsePayload::nonce,
                    HandshakeResponsePayload::new
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
