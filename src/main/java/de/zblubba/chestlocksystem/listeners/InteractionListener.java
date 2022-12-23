package de.zblubba.chestlocksystem.listeners;

import de.zblubba.chestlocksystem.Chestlocksystem;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;

public class InteractionListener implements Listener {

    FileConfiguration locksConfig = Chestlocksystem.locksConfig;

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        Player p = (Player) event.getPlayer();

        if(event.getInventory().getType().equals(InventoryType.CHEST) || event.getInventory().getType().equals(InventoryType.BARREL)) {
            Block block;
            if(event.getInventory().getLocation() != null) {
                block = event.getInventory().getLocation().getBlock();
            } else return;


            String path = "chest." + block.getX() + "_" + block.getY() + "_" + block.getZ();

            final boolean isInTeam = locksConfig.getBoolean(path + ".team." + p.getUniqueId());

            if(locksConfig.contains(path) && !p.hasPermission("locksystem.admin")) {
                if(!isInTeam && locksConfig.getBoolean(path + ".locked")) {
                    p.sendMessage(Chestlocksystem.getPrefix + "§cDiese " + block.getType() + " ist gelockt!");
                    event.setCancelled(true);
                }
            }
        }

    }
}
