package de.zblubba.chestlocksystem.listeners;

import de.zblubba.chestlocksystem.Chestlocksystem;
import de.zblubba.chestlocksystem.util.MessageCollection;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;

public class BreakListener implements Listener {

    FileConfiguration locksConfig = Chestlocksystem.locksConfig;

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        Player p = event.getPlayer();
        Block block = event.getBlock();

        String path = "chest." + block.getX() + "_" + block.getY() + "_" + block.getZ();
        boolean isLocked = locksConfig.getBoolean(path + ".locked");

        if(locksConfig.contains(path)) {
            if(isLocked) {
                p.sendMessage(MessageCollection.getBlockLocked(p.getName(), block.getType().toString()));
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player p = event.getPlayer();
        if(MessageCollection.getBlockList().contains(event.getBlock().getType().toString())) {
            if(locksConfig.getBoolean("players." + p.getUniqueId() + ".autolock")) {
                p.performCommand("lock");
            }
        }
    }
}
