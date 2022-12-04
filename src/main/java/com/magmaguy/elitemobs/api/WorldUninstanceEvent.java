package com.magmaguy.elitemobs.api;

import com.magmaguy.elitemobs.config.dungeonpackager.DungeonPackagerConfigFields;
import lombok.Getter;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class WorldUninstanceEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    @Getter
    private final DungeonPackagerConfigFields dungeonPackagerConfigFields;

    public WorldUninstanceEvent(DungeonPackagerConfigFields dungeonPackagerConfigFields) {
        this.dungeonPackagerConfigFields = dungeonPackagerConfigFields;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

}
