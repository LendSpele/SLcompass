package org.lend.slcomp;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.network.ServerInfo;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;

public class Slcomp implements ModInitializer {

    private static final String TARGET_SERVER_IP = "mc.storylegends.xyz";

    @Override
    public void onInitialize() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {

            if (client.player != null) {
                ServerInfo serverInfo = client.getCurrentServerEntry();


                if (serverInfo != null && serverInfo.address.equals(TARGET_SERVER_IP)) {


                    boolean hasCompassInHand =
                            client.player.getStackInHand(Hand.MAIN_HAND).getItem() == Items.COMPASS ||
                                    client.player.getStackInHand(Hand.OFF_HAND).getItem() == Items.COMPASS;


                    boolean targetValue = !hasCompassInHand;

                    boolean currentValue = client.options.getReducedDebugInfo().getValue();

                    if (currentValue != targetValue) {
                        client.options.getReducedDebugInfo().setValue(targetValue);
                        System.out.println("Синхронизировано значение 'Reduced Debug Info'.");
                    }
                }
            }
        });
    }
}