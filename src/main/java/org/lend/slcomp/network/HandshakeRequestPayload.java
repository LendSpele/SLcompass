package org.lend.slcomp.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record HandshakeRequestPayload(int nonce) implements CustomPayload {
    public static final CustomPayload.Id<HandshakeRequestPayload> ID =
            new CustomPayload.Id<>(Identifier.of("slcomp", "handshake_request"));

    public static final PacketCodec<ByteBuf, HandshakeRequestPayload> CODEC =
            PacketCodec.tuple(
                    PacketCodecs.INTEGER, HandshakeRequestPayload::nonce,
                    HandshakeRequestPayload::new
            );

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
