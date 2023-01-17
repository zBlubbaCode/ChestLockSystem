package de.zblubba.chestlocksystem;

import de.zblubba.chestlocksystem.commands.AutoLockCommand;
import de.zblubba.chestlocksystem.commands.LockCommand;
import de.zblubba.chestlocksystem.commands.ReloadCommand;
import de.zblubba.chestlocksystem.commands.UnlockCommand;
import de.zblubba.chestlocksystem.listeners.AvoidLockedExplosion;
import de.zblubba.chestlocksystem.listeners.BreakListener;
import de.zblubba.chestlocksystem.listeners.InteractionListener;
import de.zblubba.chestlocksystem.util.MessageCollection;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;

public final class Chestlocksystem extends JavaPlugin {
    public static Chestlocksystem instance;

    public static File locksFile = new File("plugins/chestlocksystem", "lockedBlocks.yml");
    public static FileConfiguration locksConfig = new YamlConfiguration().loadConfiguration(locksFile);
    public static File configFile = new File("plugins/chestlocksystem", "config.yml");
    public static FileConfiguration config = new YamlConfiguration().loadConfiguration(configFile);

    @Override
    public void onEnable() {
        instance = this;

        Bukkit.getConsoleSender().sendMessage("ChestLockSystem §aactivated");

        createFiles();
        loadConfigFiles();

        registerCommands();
        registerListeners();
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("ChestLockSystem §cdeactivated");
    }

    public void registerCommands() {
        getCommand("lock").setExecutor(new LockCommand());
        getCommand("unlock").setExecutor(new UnlockCommand());
        getCommand("reloadchestlocksystem").setExecutor(new ReloadCommand());
        getCommand("autolock").setExecutor(new AutoLockCommand());
    }

    public void registerListeners() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new AvoidLockedExplosion(), this);
        pm.registerEvents(new BreakListener(), this);
        pm.registerEvents(new InteractionListener(), this);
    }

    public static void createFiles() {
        if(!Chestlocksystem.locksFile.exists() || !Chestlocksystem.configFile.exists()) {
            Chestlocksystem.getInstance().getLogger().info("One or more files were not found. Creating...");
            if(!Chestlocksystem.locksFile.exists()) {
                Chestlocksystem.locksFile.getParentFile().mkdirs();
                Chestlocksystem.getInstance().saveResource("lockedBlocks.yml", false);
            }
            if(!Chestlocksystem.configFile.exists()) {
                Chestlocksystem.configFile.getParentFile().mkdirs();
                Chestlocksystem.getInstance().saveResource("config.yml", false);
            }
        }
    }

    public static void loadConfigFiles() {
        Chestlocksystem.getInstance().getLogger().info("Loading config files...");
        try {
            Chestlocksystem.locksConfig.load(locksFile);
            Chestlocksystem.config.load(configFile);
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        } catch (InvalidConfigurationException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static String getPrefix = getPrefix();

    public static String getPrefix() {
        String prefix = Chestlocksystem.config.getString("prefix");
        if(prefix != null) {
            prefix = prefix.replace("&", "§");
        } else prefix = "§cERROR §8|";
        return prefix;
    }

    public static Chestlocksystem getInstance() {
        return instance;
    }
}


// TODO: Add infinite lockable blocks
// TODO: Add all inventories of lockable blocks to interactionListener
// TODO: IN PROGRESS - Add Command to autolock on place
// TODO: Add command to auto-add default team
// TODO: Add Command to modify default team of player