package org.lend.slcomp.client;

import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "slcompass")
public class SLCompConfig implements ConfigData {

    public boolean enabledOnOtherServers = false;

    public boolean enabledCompassText = true;

    public String[] extraServers = {};

    @ConfigEntry.Gui.Excluded // не показывать в ModMenu
    public String permanentServer = "";

}
