package org.lend.slcomp;

import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.ConfigData;

@Config(name = "slcompass")
public class SLCompConfig implements ConfigData {

    public boolean enabledOnOtherServers = false;

    public String[] extraServers = {};

}
