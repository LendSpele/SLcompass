package org.lend.slcomp;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import java.util.Arrays;

public class Slcomp implements ModInitializer {

    private SLCompConfig config;



    @Override
    public void onInitialize() {

        ConfigManager.init();
        config = ConfigManager.getConfig();

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.getCurrentServerEntry() == null) return;

            ServerInfo serverInfo = client.getCurrentServerEntry();

            boolean forceActive = serverInfo.address.equals(ConfigManager.MAIN_SERVER);
            boolean onExtraServer = Arrays.asList(config.extraServers).contains(serverInfo.address);
            boolean userEnabled = config.enabledOnOtherServers && onExtraServer;

            if (forceActive || userEnabled) {
                boolean hasCompassInHand =
                        client.player.getStackInHand(Hand.MAIN_HAND).getItem() == Items.COMPASS ||
                                client.player.getStackInHand(Hand.OFF_HAND).getItem() == Items.COMPASS ||
                                client.player.getStackInHand(Hand.MAIN_HAND).getItem() == Items.FILLED_MAP ||
                                client.player.getStackInHand(Hand.OFF_HAND).getItem() == Items.FILLED_MAP;

                boolean targetValue = !hasCompassInHand;
                boolean currentValue = client.options.getReducedDebugInfo().getValue();

                if (currentValue != targetValue) {
                    client.options.getReducedDebugInfo().setValue(targetValue);
                }
            }
        });
    }
}