package org.lend.slcomp.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import org.lend.slcomp.client.network.CompClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import static org.lend.slcomp.Slcomp.MOD_ID;

public class SlcompClient implements ClientModInitializer {

    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    private SLCompConfig config;

    @Override
    public void onInitializeClient() {
        ConfigManager.init();
        config = ConfigManager.getConfig();

        LOGGER.info("\n \n SLcompass loaded! \n \n By L. (lendspele) 0/\n");


        ConfigManager.init();
        CompClient.register();


        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null || client.getCurrentServerEntry() == null) return;

            ServerInfo serverInfo = client.getCurrentServerEntry();
            boolean forceActive = serverInfo.address.equals(ConfigManager.MAIN_SERVER);
            boolean onExtraServer = Arrays.asList(config.extraServers).contains(serverInfo.address);
            boolean userEnabled = config.enabledOnOtherServers && onExtraServer;
            boolean permServer = serverInfo.address.equals(ConfigManager.getConfig().permanentServer);

            if (forceActive || userEnabled || permServer) {
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