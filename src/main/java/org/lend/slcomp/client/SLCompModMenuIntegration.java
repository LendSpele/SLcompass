package org.lend.slcomp.client;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import me.shedaniel.autoconfig.AutoConfig;

import java.util.Arrays;

public class SLCompModMenuIntegration {

    public static Screen getConfigScreen(Screen parent) {
        SLCompConfig config = AutoConfig.getConfigHolder(SLCompConfig.class).getConfig();

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.translatable("slcomp.config.title"));

        ConfigCategory general = builder.getOrCreateCategory(Text.translatable("slcomp.config.category.general"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        // Чекбокс для включения на других серверах
        general.addEntry(
                entryBuilder.startBooleanToggle(Text.translatable("slcomp.config.enabled_others"), config.enabledOnOtherServers)
                        .setSaveConsumer(newValue -> {
                            config.enabledOnOtherServers = newValue;
                            AutoConfig.getConfigHolder(SLCompConfig.class).save();
                        })
                        .build()
        );

        general.addEntry(
                entryBuilder.startBooleanToggle(Text.translatable("slcomp.config.enabled_single"), config.enabledSingle)
                        .setSaveConsumer(newValue -> {
                            config.enabledSingle = newValue;
                            AutoConfig.getConfigHolder(SLCompConfig.class).save();
                        })
                        .build()
        );

        general.addEntry(
                entryBuilder.startBooleanToggle(Text.translatable("slcomp.config.compasstext"), config.enabledCompassText)
                        .setSaveConsumer(newValue -> {
                            config.enabledCompassText = newValue;
                            AutoConfig.getConfigHolder(SLCompConfig.class).save();
                        })
                        .build()
        );

        // Список дополнительных серверов
        general.addEntry(
                entryBuilder.startStrList(
                                Text.translatable("slcomp.config.extra_servers"),
                                Arrays.asList(config.extraServers) // <--- тут List, а не массив
                        )
                        .setSaveConsumer(newValue -> {
                            config.extraServers = newValue.toArray(new String[0]);
                            AutoConfig.getConfigHolder(SLCompConfig.class).save();
                        })
                        .build()
        );

        return builder.build();
    }
}
