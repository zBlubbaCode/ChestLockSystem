package de.zblubba.chestlocksystem.listeners;

import de.zblubba.chestlocksystem.Chestlocksystem;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

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
                p.sendMessage(Chestlocksystem.getPrefix + "§cDiese " + block.getType() + " ist gelockt!");
                event.setCancelled(true);
            }
        }
    }
}
