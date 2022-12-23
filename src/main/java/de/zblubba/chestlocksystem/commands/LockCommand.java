package de.zblubba.chestlocksystem.commands;

import de.zblubba.chestlocksystem.Chestlocksystem;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.InventoryHolder;

import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

public class LockCommand implements CommandExecutor {

    FileConfiguration locksConfig = Chestlocksystem.locksConfig;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player p) {

            int maxDistance = 10;
            Block block = p.getTargetBlock(null, maxDistance);
            //block the player locks at

            String path = "chest." + block.getX() + "_" + block.getY() + "_" + block.getZ();

            if(block.getType() == Material.CHEST || block.getType() == Material.BARREL) {
                if(args.length == 0) {
                    if(!locksConfig.contains(path)) { //if chest isn't already locked
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

                                locksConfig.set(leftPath + ".owner", p.getUniqueId().toString()); //log in lockedChests.yml
                                locksConfig.set(leftPath + ".locked", true);
                                locksConfig.set(leftPath + ".team." + p.getUniqueId(), true); // log into lockedChests.yml
                                locksConfig.set(rightPath + ".owner", p.getUniqueId().toString()); //log in lockedChests.yml
                                locksConfig.set(rightPath + ".locked", true);
                                locksConfig.set(rightPath + ".team." + p.getUniqueId(), true); // log into lockedChests.yml

                                p.sendMessage(Chestlocksystem.getPrefix + "§7Diese §aDoppelchest §7wurde §aerfolgreich gelockt!");
                            } else {
                                locksConfig.set(path + ".owner", p.getUniqueId().toString()); //log in lockedChests.yml
                                locksConfig.set(path + ".locked", true);
                                locksConfig.set(path + ".team." + p.getUniqueId(), true); // log into lockedChests.yml
                                p.sendMessage(Chestlocksystem.getPrefix + "§7Diese " + block.getType() + " wurde §aerfolgreich gelockt!");
                            }
                        } else if(chestState instanceof Barrel) {
                            locksConfig.set(path + ".owner", p.getUniqueId().toString()); //log in lockedChests.yml
                            locksConfig.set(path + ".locked", true);
                            locksConfig.set(path + ".team." + p.getUniqueId(), true); // log into lockedChests.yml
                            p.sendMessage(Chestlocksystem.getPrefix + "§7Diese " + block.getType() + " wurde §aerfolgreich gelockt!");
                        }

                    } else {
                        if(locksConfig.getBoolean(path + ".locked")) {
                            p.sendMessage(Chestlocksystem.getPrefix + "§cDiese " + block.getType() + " ist bereits gelockt!");
                        } else {
                            locksConfig.set(path + ".locked", true);
                            p.sendMessage(Chestlocksystem.getPrefix + "§7Diese " + block.getType() + " wurde §aerfolgreich §7erneut §agelockt!");
                        }
                    }
                } else {
                    //if add or remove is given
                    if(args.length == 2) {
                        //if user is owner of chest && add
                        if(args[0].equalsIgnoreCase("add") && Objects.equals(locksConfig.get(path + ".owner"), p.getUniqueId().toString())) {
                            Player player = Bukkit.getPlayer(args[1]); //given Player
                            OfflinePlayer playerOffline = Bukkit.getOfflinePlayer(args[1]);

                            UUID targetUUID;
                            String targetName;
                            if(player == null) {
                                targetUUID = playerOffline.getUniqueId();
                                targetName = playerOffline.getName();
                            } else {
                                targetUUID = player.getUniqueId();
                                targetName = player.getName();
                            }
                            if(targetUUID == null) p.sendMessage(Chestlocksystem.getPrefix + "§cungültiger Spieler!");


                            BlockState chestState = block.getState();
                            if(chestState instanceof Chest) {
                                Chest chest = (Chest) chestState;
                                InventoryHolder holder = chest.getInventory().getHolder();
                                if(holder instanceof DoubleChest) {
                                    addTeamToDoubleChest(block, p, true, targetUUID, targetName);
                                } else {
                                    locksConfig.set(path + ".team." + targetUUID, true); // log into lockedChests.yml
                                    p.sendMessage(Chestlocksystem.getPrefix + "§7Der Spieler §a" + targetName + " §7wurde §aerfolgreich §7zur " + block.getType() + " §chinzugefügt!"); //done message
                                    //instead of add remove
                                }
                            } else if(chestState instanceof Barrel) {
                                locksConfig.set(path + ".team." + targetUUID, true); // log into lockedChests.yml
                                p.sendMessage(Chestlocksystem.getPrefix + "§7Der Spieler §a" + targetName + " §7wurde §aerfolgreich §7zur " + block.getType() + " §chinzugefügt!"); //done message
                            }

                        } else if(args[0].equalsIgnoreCase("remove") && Objects.equals(locksConfig.get(path + ".owner"), p.getUniqueId().toString())) {
                            Player player = Bukkit.getPlayer(args[1]); //given player
                            OfflinePlayer playerOffline = Bukkit.getOfflinePlayer(args[1]);

                            UUID targetUUID;
                            String targetName;
                            if(player == null) {
                                targetUUID = playerOffline.getUniqueId();
                                targetName = playerOffline.getName();
                            } else {
                                targetUUID = player.getUniqueId();
                                targetName = player.getName();
                            }
                            if(targetUUID == null) p.sendMessage(Chestlocksystem.getPrefix + "§cungültiger Spieler!");
                            BlockState chestState = block.getState();
                            if(chestState instanceof Chest) {
                                Chest chest = (Chest) chestState;
                                InventoryHolder holder = chest.getInventory().getHolder();
                                if(holder instanceof DoubleChest) {
                                    addTeamToDoubleChest(block, p, false, targetUUID, targetName);
                                } else {
                                    locksConfig.set(path + ".team." + targetUUID, false); // log into lockedChests.yml
                                    p.sendMessage(Chestlocksystem.getPrefix + "§7Der Spieler §a" + targetName + " §7wurde §aerfolgreich §7zur " + block.getType() + " §chinzugefügt!"); //done message
                                    //instead of add remove
                                }
                            } else if(chestState instanceof Barrel) {
                                locksConfig.set(path + ".team." + targetUUID, false); // log into lockedChests.yml
                                p.sendMessage(Chestlocksystem.getPrefix + "§7Der Spieler §a" + targetName + " §7wurde §aerfolgreich §7zur " + block.getType() + " §chinzugefügt!"); //done message
                            }

                        } else { //if not owner or alias wrong
                            p.sendMessage(Chestlocksystem.getPrefix + "§cNutze /lock <add | remove>, oder du bist nicht der Owner dieser " + block.getType() + "!");
                        }
                    }
                }
            } else {
                p.sendMessage(Chestlocksystem.getPrefix + "§cBitte schau eine Kiste oder Barrel an!");
            }

        }

        //save the configuration
        try {
            locksConfig.save(Chestlocksystem.locksFile);
        } catch (IOException e) {
            e.printStackTrace();
        }


        return false;
    }

    private void addTeamToDoubleChest(Block block, Player p, boolean state, UUID targetUUID, String targetName) {
        if(block.getType() == Material.CHEST) {
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

                    locksConfig.set(rightPath + ".team." + targetUUID, state); // log into lockedChests.yml
                    locksConfig.set(leftPath + ".team." + targetUUID, state); // log into lockedChests.yml

                    if(state) { p.sendMessage(Chestlocksystem.getPrefix + "§7Der Spieler §a" + targetName + " §7wurde §aerfolgreich §7zur §aDoppelchest §chinzugefügt!"); //done message
                    } else {p.sendMessage(Chestlocksystem.getPrefix + "§7Der Spieler §a" + targetName + " §7wurde §aerfolgreich §7von der §aDoppelchest §centfernt!");} //done message

                }
            }
        }
    }
}