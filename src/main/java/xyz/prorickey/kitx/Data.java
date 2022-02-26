package xyz.prorickey.kitx;

import org.bukkit.configuration.*;
import org.bukkit.configuration.file.*;
import org.bukkit.entity.*;
import org.bukkit.event.*;
import org.bukkit.event.player.*;
import org.bukkit.plugin.java.*;
import xyz.prorickey.kitx.cmds.*;

import java.io.*;
import java.time.*;
import java.util.*;

public class Data implements Listener {

    public static void setCoolDown(Player player, String kitName) throws IOException {
        JavaPlugin p = KitX.getPlugin();
        File file = new File(p.getDataFolder() + "/playerdata/" + player.getUniqueId().toString() + ".yml");
        YamlConfiguration config;
        if(!file.exists()) {
            file.getParentFile().mkdir();
            file.createNewFile();
        }
        config = YamlConfiguration.loadConfiguration(file);
        config.set("uuid", player.getUniqueId().toString());
        config.set("name", player.getName());
        config.set("cooldowns." + kitName, Instant.now().getEpochSecond());
        config.save(file);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        JavaPlugin pl = KitX.getPlugin();
        File file = new File(pl.getDataFolder() + "/playerdata/" + p.getUniqueId().toString() + ".yml");
        if(!file.exists()) return;
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection sec = (ConfigurationSection) config.get("cooldowns");
        CmdKit.cooldown.put(p.getUniqueId().toString(), new HashMap<>());
        sec.getKeys(false).forEach(key -> {
            Long seconds = config.getLong("cooldowns." + key);
            CmdKit.cooldown.get(p.getUniqueId().toString()).put(key, seconds);
        });
    }

}
