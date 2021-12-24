package xyz.prorickey.kitx;

import org.bukkit.*;
import org.bukkit.configuration.file.*;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;
import org.bukkit.plugin.java.*;
import xyz.prorickey.kitx.builders.*;
import xyz.prorickey.kitx.cmds.*;

import java.io.*;
import java.util.*;

public class KitX extends JavaPlugin {

    public static JavaPlugin plugin;

    public static Map<String, Kit> kits = new HashMap<>();

    @Override
    public void onEnable() {
        Bukkit.getLogger().info("Enabling KitX v" + getDescription().getVersion());
        plugin = this;
        new Config(this);
        new CmdKitX(this);
        this.getCommand("kit").setExecutor(new CmdKit());
        this.getCommand("kit").setTabCompleter(new CmdKit());

        File dir = new File(this.getDataFolder() + "/kits/");
        if(dir.exists()) {
            File[] files = dir.listFiles();
            if(files != null) {
                for (File file : files) {
                    FileConfiguration yaml = YamlConfiguration.loadConfiguration(file);
                    Inventory inv = Bukkit.createInventory(null, InventoryType.PLAYER, "yamlInventory");
                    yaml.getList("items").forEach(i -> {
                        ItemStack item = (ItemStack) i;
                        inv.addItem(item);
                    });
                    String permission = null;
                    if(yaml.get("permission") != null) {
                        permission = yaml.getString("permission");
                    }
                    new Kit(yaml.getString("name"), permission, inv, yaml.getInt("cooldown"));
                }
            }
        }

    }

    public static void addKit(String name, Kit kit) { kits.put(name, kit); }
    public static void removeKit(String name) { kits.remove(name); }
    public static Kit getKit(String name) { return kits.get(name); }
    public static Map<String, Kit> getKits() { return kits; }
    public static Boolean kitExists(String name) { return kits.containsKey(name); }

    public static JavaPlugin getPlugin() { return plugin; }
}
