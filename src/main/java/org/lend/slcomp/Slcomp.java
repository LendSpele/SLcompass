package org.lend.slcomp;


import net.fabricmc.api.ModInitializer;
import org.lend.slcomp.network.CompServer;
import org.lend.slcomp.network.ModPackets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Slcomp implements ModInitializer {

    public static final String MOD_ID = "SLcompass";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {

        LOGGER.info("\n \n SLcompass loaded! \n \n By L. (lendspele) 0/\n");


        ModPackets.registerPayloadTypes();
        CompServer.register();

    }
}