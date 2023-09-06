package xyz.prorickey.kitx.spigot;

import org.bukkit.*;
import org.bukkit.plugin.java.*;
import xyz.prorickey.kitx.api.KitManager;
import xyz.prorickey.kitx.api.KitXAPI;
import xyz.prorickey.kitx.spigot.cmds.CmdKit;
import xyz.prorickey.kitx.spigot.cmds.CmdKitX;
import xyz.prorickey.kitx.spigot.database.Database;
import xyz.prorickey.kitx.spigot.database.YAML;
import xyz.prorickey.kitx.spigot.cmds.*;
import xyz.prorickey.kitx.spigot.database.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class KitX extends JavaPlugin {

    public static JavaPlugin plugin;
    public static Database database;
    public static KitManagerImpl kitManager;

    @Override
    public void onEnable() {
        Bukkit.getLogger().info("Enabling KitX v" + getDescription().getVersion());
        plugin = this;
        new Metrics(this, 14534);
        new Config(this);
        switch(Config.getConfig().getString("database.type")) {
            case "yaml": {
                database = new YAML();
                break;
            }
            default: {
                Bukkit.getLogger().warning("Database type is not listed correctly! Shutting down plugin.");
                getServer().getPluginManager().disablePlugin(this);
            }
        }
        kitManager = new KitManagerImpl(database);
        KitXAPI.setInstance(kitManager);
        new CmdKitX(this);
        this.getCommand("kit").setExecutor(new CmdKit());
        this.getCommand("kit").setTabCompleter(new CmdKit());

    }

    public static KitManager getKitManager() { return kitManager; }
    public static JavaPlugin getPlugin() { return plugin; }

    public static String format(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static List<String> tabCompletions(String currentArg, List<String> completions) {
        if (currentArg.length() == 0) { return completions; }
        currentArg = currentArg.toLowerCase(Locale.ROOT);
        List<String> returnedCompletions = new ArrayList<>();
        for (String str : completions) {
            if (str.toLowerCase(Locale.ROOT).startsWith(currentArg)) {
                returnedCompletions.add(str);
            }
        }
        return returnedCompletions;
    }
}
