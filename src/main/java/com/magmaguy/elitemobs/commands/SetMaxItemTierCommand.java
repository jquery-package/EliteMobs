package com.magmaguy.elitemobs.commands;

import com.magmaguy.elitemobs.ChatColorConverter;
import org.bukkit.command.CommandSender;

public class SetMaxItemTierCommand {

    public static void setMaxItemTier(double tier, CommandSender commandSender) {

//        CustomConfigLoader customConfigLoader = new CustomConfigLoader();
//        Configuration itemsDropSettingsConfig = customConfigLoader.getCustomConfig(ItemsDropSettingsConfig.CONFIG_NAME);
//
//        itemsDropSettingsConfig.set(ItemsDropSettingsConfig.MAXIMUM_LOOT_TIER, tier);
//        customConfigLoader.saveCustomConfig(ItemsDropSettingsConfig.CONFIG_NAME);
//
//        CustomConfigLoader customConfigLoader1 = new CustomConfigLoader();
//        Configuration itemsProceduralSettingsConfig = customConfigLoader1.getCustomConfig(ItemsProceduralSettingsConfig.CONFIG_NAME);
//
//        itemsProceduralSettingsConfig.set(EnchantmentsConfig.getEnchantment(Enchantment.ARROW_DAMAGE).getMaxLevel() + "", tier - 1);
//        itemsProceduralSettingsConfig.set(EnchantmentsConfig.getEnchantment(Enchantment.DAMAGE_ALL).getMaxLevel() + "", tier - 1);
//        itemsProceduralSettingsConfig.set(EnchantmentsConfig.getEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL).getMaxLevel() + "", tier - 1);
//        customConfigLoader1.saveCustomConfig(ItemsProceduralSettingsConfig.CONFIG_NAME);
//
//
//        int maxMobLevel = (int) (tier * ConfigValues.mobCombatSettingsConfig.getDouble(MobCombatSettingsConfig.perTierLevelIncrease) * 3);
//
//        CustomConfigLoader customConfigLoader2 = new CustomConfigLoader();
//        Configuration mobCombatSettingsConfig = customConfigLoader2.getCustomConfig(MobCombatSettingsConfig.CONFIG_NAME);
//        mobCombatSettingsConfig.set(MobCombatSettingsConfig.naturalElitemobLevelCap, maxMobLevel);
//        customConfigLoader2.saveCustomConfig(MobCombatSettingsConfig.CONFIG_NAME);

        commandSender.sendMessage(ChatColorConverter.convert(
                "Warning: This command is currently under maintenance!"));

//        commandSender.sendMessage(ChatColorConverter.convert(
//                "Warning: You have set the max tier to " + tier + "!"));
//        commandSender.sendMessage(ChatColorConverter.convert(
//                "The max protection enchantment has been increased to " + (tier - 1)));
//        commandSender.sendMessage(ChatColorConverter.convert(
//                "The max sharpness enchantment has been increased to " + (tier - 1)));
//        commandSender.sendMessage(ChatColorConverter.convert(
//                "The max power enchantment has been increased to " + (tier - 1)));
//        commandSender.sendMessage(ChatColorConverter.convert(
//                "The max mob level has been increased to " + maxMobLevel));
//        commandSender.sendMessage(ChatColorConverter.convert(
//                "You can run this command again to tweak the values or change them in the ItemProceduralSettings.yml file," +
//                        " as well as the ItemsDropSettings and the MobCombatSettings"));

    }

}
