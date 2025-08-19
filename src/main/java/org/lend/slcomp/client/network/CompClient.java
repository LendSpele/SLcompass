package org.lend.slcomp.client.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ServerInfo;
import org.lend.slcomp.client.ConfigManager;
import org.lend.slcomp.client.SLCompConfig;
import org.lend.slcomp.network.HandshakeRequestPayload;
import org.lend.slcomp.network.HandshakeResponsePayload;

public final class CompClient {
    private static boolean registered = false;
    private CompClient() {}

    public static void register() {
        if (registered) return;
        registered = true;

        ClientPlayNetworking.registerGlobalReceiver(HandshakeRequestPayload.ID, (payload, context) -> {
            ClientPlayNetworking.send(new HandshakeResponsePayload(payload.nonce()));

            context.client().execute(() -> {
                ConfigManager.init();
                SLCompConfig cfg = ConfigManager.getConfig();
                String current = cfg.permanentServer == null ? "" : cfg.permanentServer;


   //             String serverAddr = context.client().getNetworkHandler().getConnection().getAddress().toString();

                MinecraftClient client = MinecraftClient.getInstance();
                ServerInfo serverInfo = client.getCurrentServerEntry();
                if (serverInfo != null) {
                    String serverAddr = serverInfo.address;

                    cfg.permanentServer = serverAddr;
                    ConfigManager.saveConfig();
                }

            });
        });
    }
}
