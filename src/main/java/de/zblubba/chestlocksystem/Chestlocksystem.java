package de.zblubba.chestlocksystem;

import de.zblubba.chestlocksystem.commands.LockCommand;
import de.zblubba.chestlocksystem.commands.ReloadCommand;
import de.zblubba.chestlocksystem.commands.UnlockCommand;
import de.zblubba.chestlocksystem.listeners.AvoidLockedExplosion;
import de.zblubba.chestlocksystem.listeners.BreakListener;
import de.zblubba.chestlocksystem.listeners.InteractionListener;
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

    public static String getPrefix = "§eTerribian §8| §7";

    public static File locksFile = new File("plugins/chestlocksystem", "lockedChests.yml");
    public static FileConfiguration locksConfig = new YamlConfiguration().loadConfiguration(locksFile);

    @Override
    public void onEnable() {
        instance = this;

        Bukkit.getConsoleSender().sendMessage("ChestLockSystem §aaktiviert");

        createFiles();
        loadConfigFiles();

        registerCommands();
        registerListeners();
    }

    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage("ChestLockSystem §cdeaktiviert");
    }

    public void registerCommands() {
        getCommand("lock").setExecutor(new LockCommand());
        getCommand("unlock").setExecutor(new UnlockCommand());
        getCommand("reloadchestlocksystem").setExecutor(new ReloadCommand());
    }

    public void registerListeners() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new AvoidLockedExplosion(), this);
        pm.registerEvents(new BreakListener(), this);
        pm.registerEvents(new InteractionListener(), this);
    }

    public static void createFiles() {
        if(!Chestlocksystem.locksFile.exists()) {
            Chestlocksystem.getInstance().getLogger().info("One or more files were not found. Creating...");
            Chestlocksystem.locksFile.getParentFile().mkdirs();
            Chestlocksystem.getInstance().saveResource("lockedChests.yml", false);
        }
    }

    public static void loadConfigFiles() {
        Chestlocksystem.getInstance().getLogger().info("Loading config files...");
        try {
            Chestlocksystem.locksConfig.load(locksFile);

        } catch (IOException exception) {
            throw new RuntimeException(exception);
        } catch (InvalidConfigurationException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static Chestlocksystem getInstance() {
        return instance;
    }
}
