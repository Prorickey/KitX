package xyz.prorickey.kitx.spigot;

import org.bukkit.configuration.file.*;
import org.bukkit.plugin.java.*;

import java.io.*;

public class Config {
    private static JavaPlugin plugin;
    private static File file;
    private static FileConfiguration config;
    public Config(JavaPlugin p) {
        plugin = p;
        file = new File(p.getDataFolder() + "/config.yml");
        if(!file.exists()) { p.saveResource("config.yml", false); }
        config = YamlConfiguration.loadConfiguration(file);
    }
    public static FileConfiguration getConfig() { return config; }
    public static void saveConfig() {
        try {
            getConfig().save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void reloadConfig(String name) { config = YamlConfiguration.loadConfiguration(file); }

}
