package org.lend.slcomp.client;

import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;

public final class ConfigManager {
    // Main
    public static final String MAIN_SERVER = "mc.storylegends.xyz";

    private static boolean initialized = false;

    private ConfigManager() {}

    public static void init() {
        if (!initialized) {
            AutoConfig.register(SLCompConfig.class, JanksonConfigSerializer::new);
            initialized = true;
        }
    }

    public static SLCompConfig getConfig() {
        return AutoConfig.getConfigHolder(SLCompConfig.class).getConfig();
    }

    public static void saveConfig() {
        AutoConfig.getConfigHolder(SLCompConfig.class).save();
    }
}
