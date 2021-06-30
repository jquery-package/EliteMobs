package com.magmaguy.elitemobs.announcements;

import com.magmaguy.elitemobs.ChatColorConverter;
import com.magmaguy.elitemobs.config.ConfigValues;
import com.magmaguy.elitemobs.config.EventsConfig;
import com.magmaguy.elitemobs.thirdparty.discordsrv.DiscordSRVAnnouncement;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class AnnouncementPriority {
    public static void announce(String message, World world, int announcementPriority) {
        if (announcementPriority == 1 && !ConfigValues.eventsConfig.getBoolean(EventsConfig.ANNOUNCEMENT_BROADCAST_WORLD_ONLY))
            Bukkit.broadcastMessage(ChatColorConverter.convert(message));
        else
            for (Player player : world.getPlayers())
                player.sendMessage(ChatColorConverter.convert(message));
        if (announcementPriority < 3) return;
        new DiscordSRVAnnouncement(ChatColorConverter.convert(message));
    }
}
