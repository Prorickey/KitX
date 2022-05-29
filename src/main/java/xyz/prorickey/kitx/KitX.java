package xyz.prorickey.kitx;

import org.bukkit.*;
import org.bukkit.plugin.java.*;
import xyz.prorickey.kitx.cmds.*;
import xyz.prorickey.kitx.database.*;

public class KitX extends JavaPlugin {

    public static JavaPlugin plugin;
    public static Database database;

    @Override
    public void onEnable() {
        Bukkit.getLogger().info("Enabling KitX v" + getDescription().getVersion());
        plugin = this;
        new Metrics(this, 14534);
        new Config(this);
        new CmdKitX(this);
        this.getCommand("kit").setExecutor(new CmdKit());
        this.getCommand("kit").setTabCompleter(new CmdKit());

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

    }

    public static Database getDataManager() { return database; }
    public static JavaPlugin getPlugin() { return plugin; }
}
