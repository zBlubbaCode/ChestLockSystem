package de.zblubba.chestlocksystem.util;

import de.zblubba.chestlocksystem.Chestlocksystem;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.UUID;

public class MessageCollection {

    public static Configuration config = Chestlocksystem.config;

    // ------------------------------------------------------------------------

    //locked blocklist
    public static ArrayList<String> getBlockList() {
        ArrayList<String> blockList = (ArrayList<String>) config.getList("locked_blocks");
        return blockList;
    }

    // ------------------------------------------------------------------------
    //general
    public static String getUnkownplayer(String playername, String targetName) {
        String unknownplayer = config.getString("unknown_player");
        unknownplayer = replaceWithVariables(unknownplayer,playername , targetName, null);
        return unknownplayer;
    }
    public static String getAlreadyLocked(String playername, String blockName) {
        String alreadyLocked = config.getString("messages.already_locked");
        alreadyLocked = replaceWithVariables(alreadyLocked, playername, null, blockName);
        return alreadyLocked;
    }
    public static String getNotOwner(String playername, String blockName) {
        String notOwner = config.getString("messages.not_owner");
        notOwner = replaceWithVariables(notOwner, playername, null, blockName);
        return notOwner;
    }
    public static String getBlockLocked(String playername, String blockName) {
        String locked = config.getString("messages.locked");
        locked = replaceWithVariables(locked, playername, null, blockName);
        return locked;
    }
    public static String lookAtLockableBlock(String playername, String blockName) {
        String lookAt = config.getString("messages.look_at_lockable_block");
        lookAt = replaceWithVariables(lookAt, playername, null, blockName);
        return lookAt;
    }

    // ------------------------------------------------------------------------
    // lock
    public static String getLockSuccess(String playername, String blockName) {
        String lockSuccess = config.getString("messages.lock.success");
        lockSuccess = replaceWithVariables(lockSuccess, playername, null, blockName);
        return lockSuccess;
    }
    public static String getLockPlayerAdded(String playername, String targetName, String blockName) {
        String playerAdded = config.getString("messages.lock.player_added");
        playerAdded = replaceWithVariables(playerAdded, playername, targetName, blockName);
        return playerAdded;
    }
    public static String getLockPlayerRemoved(String playername, String targetName, String blockName) {
        String playerRemoved = config.getString("messages.lock.player_removed");
        playerRemoved = replaceWithVariables(playerRemoved, playername, targetName, blockName);
        return playerRemoved;
    }
    public static String getUnlockSuccess(String playername, String blockName) {
        String unlockSuccess = config.getString("messages.unlock.success");
        unlockSuccess = replaceWithVariables(unlockSuccess, playername, null, blockName);
        return unlockSuccess;
    }

    // ------------------------------------------------------------------------
    //security
    public static String getExplosionMessage(String blockType) {
        String message = config.getString("security.explosion.message");
        message = replaceWithVariables(message, null, null, blockType);
        return message;
    }
    public static String getHopperMoveMessage(String blockType) {
        String message = config.getString("security.hoppers.message");
        message = replaceWithVariables(message, null, null, blockType);
        return message;
    }

    // ------------------------------------------------------------------------
    // commands

    //autolock
    public static String getAutolockColor(String state) {
        String color = config.getString("commands.autolock."+ state +"_color");
        color = replaceWithVariables(color, null, null, null);
        return color;
    }

    public static String getAutolockStateEdited(UUID uuid) {
        String stateEdited = getAutolockStateEditedNoColor(uuid);
        String statemessage = getAutolockColor(stateEdited) + stateEdited;
        statemessage = replaceWithVariables(statemessage, null, null, null);
        return statemessage;
    }

    public static String getAutolockStateEditedNoColor(UUID uuid) {
        boolean state = getAutoLockState(uuid);
        if(state) {return "on";} else return "off";
    }

    public static boolean getAutoLockState(UUID uuid) {
        boolean state = Chestlocksystem.locksConfig.getBoolean("players." + uuid.toString() + ".autolock");
        return state;
    }

    public static String getAutoLockStateString(Player player) {
        String stateString = config.getString("commands.autolock.state");
        stateString = replaceWithVariables(stateString, player.getName(), null, null);
        stateString = stateString.replace("%state%", getAutolockStateEdited(player.getUniqueId()));
        return stateString;
    }

    public static String getAutoLockToggleMessage(Player p) {
        String message = config.getString("commands.autolock.toggle");
        message = replaceWithVariables(message, p.getName(), null, null);

        message = message.replace("%state%", getAutolockStateEdited(p.getUniqueId()));
        return message;
    }


    // ------------------------------------------------------------------------

    public static String replaceWithVariables(String input, String playername, String targetname, String blocktype) {
        if(input == null) {
            Chestlocksystem.getInstance().getLogger().info("ERROR | On message send");
            return "§cERROR";
        }
        String prefix = config.getString("prefix");
        prefix = prefix.replace("&", "§");

        input = input.replace("&", "§");
        input = input.replace("%prefix%", prefix);
        input = input.replace("%n%", "\n");

        if(playername != null) input = input.replace("%player%", playername);
        if(targetname != null) input = input.replace("%target%", targetname);
        if(blocktype != null) input = input.replace("%block%", blocktype.toLowerCase());

        return input;
    }

}
