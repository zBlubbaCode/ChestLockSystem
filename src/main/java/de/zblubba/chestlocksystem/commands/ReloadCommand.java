package de.zblubba.chestlocksystem.commands;

import de.zblubba.chestlocksystem.Chestlocksystem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;

import java.io.IOException;

public class ReloadCommand implements CommandExecutor  {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender.hasPermission("locksystem.admin")) {

            // for the message at the end how long the reload took
            long timerStart = System.currentTimeMillis();
            sender.sendMessage(Chestlocksystem.getPrefix + "reloading...");

            Chestlocksystem.createFiles();
            Chestlocksystem.loadConfigFiles();

            // end the timer
            long timerEnd = System.currentTimeMillis();
            long duration = timerEnd - timerStart;

            sender.sendMessage(Chestlocksystem.getPrefix + "§aReload complete! Duration: " + duration + "ms");

        } else sender.sendMessage("§cKeine Rechte!");
        return false;
    }
}
