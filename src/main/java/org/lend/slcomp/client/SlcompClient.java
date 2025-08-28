package org.lend.slcomp.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import org.lend.slcomp.client.network.CompClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.minecraft.text.Text;
import net.minecraft.text.Style;
import net.minecraft.util.Formatting;

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


        CompClient.register();


        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player == null) return;

            if (client.getCurrentServerEntry() == null && client.getServer() == null) return;

            ServerInfo serverInfo = client.getCurrentServerEntry();
            boolean isSinglePlayer = client.getServer() != null;

            boolean forceActive = serverInfo != null && serverInfo.address.equals(ConfigManager.MAIN_SERVER);
            boolean onExtraServer = serverInfo != null && Arrays.asList(config.extraServers).contains(serverInfo.address);
            boolean userEnabled = config.enabledOnOtherServers && onExtraServer;
            boolean permServer = serverInfo != null && serverInfo.address.equals(ConfigManager.getConfig().permanentServer);
            boolean singleEnabled = config.enabledSingle && isSinglePlayer;

            if (forceActive || userEnabled || permServer || singleEnabled) {
                boolean hasCompassInHand =
                        client.player.getStackInHand(Hand.MAIN_HAND).getItem() == Items.COMPASS ||
                                client.player.getStackInHand(Hand.OFF_HAND).getItem() == Items.COMPASS ||
                                client.player.getStackInHand(Hand.MAIN_HAND).getItem() == Items.FILLED_MAP ||
                                client.player.getStackInHand(Hand.OFF_HAND).getItem() == Items.FILLED_MAP;

                //  Отправка координат над хотбаром если включено

                if (config.enabledCompassText && hasCompassInHand) {

                    String x = String.format("%.2f", client.player.getX());
                    String y = String.format("%.2f", client.player.getY());
                    String z = String.format("%.2f", client.player.getZ());

                    Text compass = Text.empty()
                            .append(Text.literal("X: ").setStyle(Style.EMPTY.withColor(Formatting.RED)))
                            .append(Text.literal(x).setStyle(Style.EMPTY.withColor(Formatting.RED)))

                            .append(Text.literal(" Y: ").setStyle(Style.EMPTY.withColor(Formatting.GREEN)))
                            .append(Text.literal(y).setStyle(Style.EMPTY.withColor(Formatting.GREEN)))

                            .append(Text.literal(" Z: ").setStyle(Style.EMPTY.withColor(Formatting.AQUA)))
                            .append(Text.literal(z).setStyle(Style.EMPTY.withColor(Formatting.AQUA)));

                    client.player.sendMessage(compass, true);

                }

                boolean targetValue = !hasCompassInHand;
                boolean currentValue = client.options.getReducedDebugInfo().getValue();

                if (currentValue != targetValue) {
                    client.options.getReducedDebugInfo().setValue(targetValue);
                }
            }
        });
    }
}