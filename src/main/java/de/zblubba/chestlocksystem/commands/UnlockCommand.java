package de.zblubba.chestlocksystem.commands;

import de.zblubba.chestlocksystem.Chestlocksystem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;

import java.io.IOException;

public class UnlockCommand implements CommandExecutor {

    FileConfiguration locksConfig = Chestlocksystem.locksConfig;
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player p) {

            int maxDistance = 10;
            Block block = p.getTargetBlock(null, maxDistance);

            String path = "chest." + block.getX() + "_" + block.getY() + "_" + block.getZ();

            if(block.getType() == Material.CHEST || block.getType() == Material.BARREL) {
                if(locksConfig.contains(path)) {
                    BlockState chestState = block.getState();
                    if(chestState instanceof Chest) {
                        Chest chest = (Chest) chestState;
                        InventoryHolder holder = chest.getInventory().getHolder();
                        if(holder instanceof DoubleChest) {
                            DoubleChest doubleChest = (DoubleChest) holder;

                            Chest rightChest = (Chest) doubleChest.getRightSide();
                            Chest leftChest = (Chest) doubleChest.getLeftSide();

                            Location rightSide = rightChest.getLocation();
                            Location leftSide = leftChest.getLocation();

                            String leftPath = "chest." + (int)leftSide.getX() + "_" + (int)leftSide.getY() + "_" + (int)leftSide.getZ();
                            String rightPath = "chest." + (int)rightSide.getX() + "_" + (int)rightSide.getY() + "_" + (int)rightSide.getZ();

                            String leftOwnerUUID = locksConfig.getString(rightPath + ".owner");
                            String rightOwnerUUID = locksConfig.getString(rightPath + ".owner");
                            if(leftOwnerUUID == rightOwnerUUID) {
                                if(leftOwnerUUID.equals(p.getUniqueId().toString())) {

                                    locksConfig.set(leftPath + ".locked", false);
                                    locksConfig.set(rightPath + ".locked", false);

                                    p.sendMessage(Chestlocksystem.getPrefix + "§7Diese §aDoppelchest §7wurde §aerfolgreich §7entlocked!");
                                } else {
                                    p.sendMessage("Du nicht owner sein");
                                }
                            } else {
                                p.sendMessage("beide owner nicht gleich lol");
                            }
                        } else {
                            String ownerUUID = locksConfig.getString(path + ".owner");

                            if(ownerUUID.equals(p.getUniqueId().toString())) {

                                locksConfig.set(path + ".locked", false);
                                p.sendMessage(Chestlocksystem.getPrefix + "§7Diese " + block.getType() + " wurde §aerfolgreich §7entlocked!");

                            } else {
                                p.sendMessage(Chestlocksystem.getPrefix + "§cDu bist nicht der Owner dieses " + block.getType() + "!");
                            }
                        }
                    } else if(chestState instanceof Barrel) {
                        String ownerUUID = locksConfig.getString(path + ".owner");

                        if(ownerUUID.equals(p.getUniqueId().toString())) {
                            locksConfig.set(path + ".locked", false);
                            p.sendMessage(Chestlocksystem.getPrefix + "§7Diese " + block.getType() + " wurde §aerfolgreich §7entlocked!");
                        } else {
                            p.sendMessage(Chestlocksystem.getPrefix + "§cDu bist nicht der Owner dieses " + block.getType() + "!");
                        }
                    }

                }
            }

        }
        try {
            locksConfig.save(Chestlocksystem.locksFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}