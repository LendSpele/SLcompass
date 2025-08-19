package org.lend.slcomp.network;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

public final class CompServer {
    private static final int TIMEOUT_TICKS = 60; // ~3 сек при 20 TPS
    private static final Map<UUID, Integer> awaitingUntilTick = new ConcurrentHashMap<>();
    private static boolean registered = false;

    private CompServer() {}

    public static void register() {
        if (registered) return;
        registered = true;

        // Обработчик ответа клиента
        ServerPlayNetworking.registerGlobalReceiver(HandshakeResponsePayload.ID, (payload, context) -> {
            ServerPlayerEntity player = context.player();
            if (player == null) return;
            // Ответ пришёл — снимаем игрока с ожидания
            awaitingUntilTick.remove(player.getGameProfile().getId());
        });

        // При входе игрока: шлём запрос и ставим дедлайн
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayerEntity player = handler.player;
            int nonce = ThreadLocalRandom.current().nextInt();
            ServerPlayNetworking.send(player, new HandshakeRequestPayload(nonce));
            awaitingUntilTick.put(player.getGameProfile().getId(), server.getTicks() + TIMEOUT_TICKS);
        });

        // Очистка при выходе
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            ServerPlayerEntity player = handler.player;
            if (player != null) awaitingUntilTick.remove(player.getGameProfile().getId());
        });

        // Проверка таймаутов
        ServerTickEvents.END_SERVER_TICK.register(CompServer::checkTimeouts);
    }

    private static void checkTimeouts(MinecraftServer server) {
        if (awaitingUntilTick.isEmpty()) return;
        int now = server.getTicks();

        awaitingUntilTick.entrySet().removeIf(entry -> {
            UUID uuid = entry.getKey();
            int deadline = entry.getValue();
            if (now >= deadline) {
                ServerPlayerEntity player = server.getPlayerManager().getPlayer(uuid);
                if (player != null) {
                    ServerPlayNetworkHandler nh = player.networkHandler;
                    if (nh != null && nh.isConnectionOpen()) {
                        nh.disconnect(Text.literal("The server requires the mod \"SLcompass\" installed")
                                .styled(s -> s.withColor(Formatting.RED)));
                    }
                }
                return true;
            }
            return false;
        });
    }
}
