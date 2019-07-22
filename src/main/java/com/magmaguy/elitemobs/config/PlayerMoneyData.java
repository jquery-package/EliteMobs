package com.magmaguy.elitemobs.config;

import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

/**
 * Created by MagmaGuy on 17/06/2017.
 */
public class PlayerMoneyData {

    private static FileConfiguration fileConfiguration;
    private static File file;

    public static FileConfiguration getFileConfiguration() {
        return fileConfiguration;
    }

    public static double getDouble(String entry) {
        return fileConfiguration.getDouble(entry);
    }

    public static void saveConfig() {
        ConfigurationEngine.fileSaverCustomValues(fileConfiguration, file);
    }

    public static void initializeConfig() {
        file = ConfigurationEngine.fileCreator("data", "playerMoneyData.yml");
        fileConfiguration = ConfigurationEngine.fileConfigurationCreator(file);
        ConfigurationEngine.fileSaverCustomValues(fileConfiguration, file);
    }

}
