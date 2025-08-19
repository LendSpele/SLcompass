package org.lend.slcomp.network;

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public final class ModPackets {
    private ModPackets() {}

    public static void registerPayloadTypes() {
        // S2C: сервер -> клиент
        PayloadTypeRegistry.playS2C().register(HandshakeRequestPayload.ID, HandshakeRequestPayload.CODEC);
        // C2S: клиент -> сервер
        PayloadTypeRegistry.playC2S().register(HandshakeResponsePayload.ID, HandshakeResponsePayload.CODEC);
    }
}
