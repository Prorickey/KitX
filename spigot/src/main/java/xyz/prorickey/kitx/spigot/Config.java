package xyz.prorickey.kitx.spigot;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.*;
import org.bukkit.plugin.java.*;

import java.io.*;

public class Config {
    private static File file;
    private static FileConfiguration config;
    public Config(JavaPlugin p) {
        file = new File(p.getDataFolder() + "/config.yml");
        if(!file.exists()) { p.saveResource("config.yml", false); }
        config = YamlConfiguration.loadConfiguration(file);
    }
    public static FileConfiguration getConfig() { return config; }
    public static void saveConfig() {
        try {
            getConfig().save(file);
        } catch (IOException e) {
            Bukkit.getLogger().severe("Could not save config file! Details below, please report this to the developer: https://github.com/Prorickey/KitX/issues");
            Bukkit.getLogger().severe(e.getMessage());
        }
    }
    public static void reloadConfig(String name) { config = YamlConfiguration.loadConfiguration(file); }

}
