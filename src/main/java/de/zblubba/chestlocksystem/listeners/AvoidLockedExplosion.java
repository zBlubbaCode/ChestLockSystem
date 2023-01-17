package de.zblubba.chestlocksystem.listeners;

import de.zblubba.chestlocksystem.Chestlocksystem;
import de.zblubba.chestlocksystem.util.MessageCollection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class AvoidLockedExplosion implements Listener {

    FileConfiguration locksConfig = Chestlocksystem.locksConfig;

    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {
        for(Block block : event.blockList()) {
            if(block.getType() == Material.CHEST || block.getType() == Material.BARREL) {
                event.setCancelled(true);
                Bukkit.broadcastMessage(MessageCollection.getExplosionMessage(block.getType().toString()));
            }
        }
    }

    @EventHandler
    public void onInventoryMoveItem(InventoryMoveItemEvent event) {
        Inventory source = event.getSource();
        Inventory destination = event.getDestination();
        Location block = event.getSource().getLocation();

        if(source.getType() == InventoryType.CHEST || source.getType() == InventoryType.BARREL) {
            int x = (int) block.getX();
            int y = (int) block.getY();
            int z = (int) block.getZ();
            String path = "chest." + x + "_" + y + "_" + z;

            Material blockType = Bukkit.getWorlds().get(0).getBlockAt(block).getType();

            if(locksConfig.getBoolean(path + ".locked")) {
                event.setCancelled(true);
                Bukkit.broadcastMessage(Chestlocksystem.getPrefix + "Es versucht gerade jemand §c mit einem " + destination.getType() + " Sachen aus einer gelockten " + blockType + " zu nehmen");
                Bukkit.broadcastMessage(MessageCollection.getHopperMoveMessage(block.getBlock().getType().toString()));
            }
        }
    }
}
