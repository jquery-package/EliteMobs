package com.magmaguy.elitemobs.npcs;

import com.magmaguy.elitemobs.EntityTracker;
import com.magmaguy.elitemobs.MetadataHandler;
import com.magmaguy.elitemobs.adventurersguild.AdventurersGuildGUI;
import com.magmaguy.elitemobs.commands.shops.CustomShopHandler;
import com.magmaguy.elitemobs.commands.shops.ShopHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class NPCInteractions implements Listener {

    public enum NPCInteractionType {
        GUILD_GREETER,
        CHAT,
        CUSTOM_SHOP,
        PROCEDURALLY_GENERATED_SHOP,
        BAR,
        ARENA,
        NONE
    }

    @EventHandler
    public void playerNPCInteract(PlayerInteractAtEntityEvent event) {

        NPCEntity npcEntity = EntityTracker.getNPCEntity(event.getRightClicked());
        if (npcEntity == null) return;
        if (npcEntity.getIsSleeping()) return;

        event.setCancelled(true);

        switch (npcEntity.getInteractionType()) {
            case GUILD_GREETER:
                if (event.getPlayer().hasPermission("elitemobs.adventurersguild"))
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            AdventurersGuildGUI.mainMenu(event.getPlayer());
                        }
                    }.runTaskLater(MetadataHandler.PLUGIN, 1);
                break;
            case CHAT:
                npcEntity.sayDialog(event.getPlayer());
                break;
            case CUSTOM_SHOP:
                if (event.getPlayer().hasPermission("elitemobs.customshop.npc"))
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            CustomShopHandler.customShopInitializer(event.getPlayer());
                        }
                    }.runTaskLater(MetadataHandler.PLUGIN, 1);

                break;
            case PROCEDURALLY_GENERATED_SHOP:
                if (event.getPlayer().hasPermission("elitemobs.shop.npc"))
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            ShopHandler.shopInitializer(event.getPlayer());
                        }
                    }.runTaskLater(MetadataHandler.PLUGIN, 1);
                break;
            case BAR:
                break;
            case ARENA:
                break;
            case NONE:
                break;

        }

    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {

        if (!event.getInventory().getType().equals(InventoryType.MERCHANT)) return;

        for (NPCEntity npcEntity : EntityTracker.getNPCEntities())
            if (event.getInventory().getName().equals(npcEntity.getName())) {
                event.setCancelled(true);
                return;
            }

    }


}
