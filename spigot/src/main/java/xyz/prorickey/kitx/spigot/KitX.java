package xyz.prorickey.kitx.spigot;

import cloud.commandframework.execution.CommandExecutionCoordinator;
import cloud.commandframework.paper.PaperCommandManager;
import net.cybercake.cyberapi.common.builders.settings.FeatureSupport;
import net.cybercake.cyberapi.common.builders.settings.Settings;
import net.cybercake.cyberapi.spigot.CyberAPI;
import org.bukkit.*;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.*;
import xyz.prorickey.kitx.api.KitManager;
import xyz.prorickey.kitx.api.KitXAPI;
import xyz.prorickey.kitx.spigot.cmds.CmdKit;
import xyz.prorickey.kitx.spigot.cmds.CmdKitX;
import xyz.prorickey.kitx.spigot.database.Database;
import xyz.prorickey.kitx.spigot.database.YAML;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Function;

public class KitX extends CyberAPI {

    public static JavaPlugin plugin;
    public static Database database;
    public static KitManagerImpl kitManager;

    @Override
    public void onEnable() {
        Bukkit.getLogger().info("Enabling KitX v" + getDescription().getVersion());
        plugin = this;

        startCyberAPI(
                Settings.builder()
                        .mainPackage("xyz.prorickey.kitx.spigot.KitX")
                        .build()
        );

        try {
            final PaperCommandManager<CommandSender> paperCommandManager = new PaperCommandManager<>(
                this,
                    CommandExecutionCoordinator.simpleCoordinator(),
                    Function.identity(),
                    Function.identity()
            );
            paperCommandManager.registerBrigadier();
            CmdKit.registerCommand(paperCommandManager);
            CmdKitX.registerCommand(paperCommandManager);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

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

    }

    public static KitManager getKitManager() { return kitManager; }
    public static JavaPlugin getPlugin() { return plugin; }
}
