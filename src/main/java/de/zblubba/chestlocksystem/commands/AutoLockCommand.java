package de.zblubba.chestlocksystem.commands;

import de.zblubba.chestlocksystem.Chestlocksystem;
import de.zblubba.chestlocksystem.util.MessageCollection;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.Configuration;
import org.bukkit.entity.Player;

public class AutoLockCommand implements CommandExecutor {

    Configuration config = Chestlocksystem.locksConfig;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player p) {
            if(args.length == 0) {
                p.sendMessage(MessageCollection.getAutoLockStateString(p));
            } else {
                if(args[0].equalsIgnoreCase("toggle")) {
                    boolean state = config.getBoolean("players." + p.getUniqueId() + ".autolock");
                    config.set("players." + p.getUniqueId() + ".autolock", !state);

                    p.sendMessage(MessageCollection.getAutoLockToggleMessage(p));

                } else if(args[0].equalsIgnoreCase("on")) {
                    config.set("players." + p.getUniqueId() + ".autolock", true);
                    p.sendMessage(MessageCollection.getAutoLockToggleMessage(p));

                } else if(args[0].equalsIgnoreCase("off")) {
                    config.set("players." + p.getUniqueId() + ".autolock", false);
                    p.sendMessage(MessageCollection.getAutoLockToggleMessage(p));

                } else {
                    p.sendMessage("§cAlias: /autolock <toggle | on | off>");
                }
            }
        }
        return false;
    }
}
