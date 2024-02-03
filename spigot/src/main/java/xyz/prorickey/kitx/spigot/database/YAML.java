package xyz.prorickey.kitx.spigot.database;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.*;
import org.bukkit.inventory.ItemStack;
import xyz.prorickey.kitx.api.Kit;
import xyz.prorickey.kitx.spigot.KitX;

import java.io.*;
import java.util.*;

public class YAML implements Database {

    public YAML() {
        File dir = new File(KitX.getPlugin().getDataFolder() + "/yaml");
        dir.mkdir();
    }

    @Override
    public Map<String, Kit> getKits() {
        Map<String, Kit> kits = new HashMap<>();
        File dir = new File(KitX.getPlugin().getDataFolder() + "/yaml");
        File[] files = dir.listFiles();
        if(files != null) {
            for (File file : files) {
                if(file.getName().startsWith("kit_")) {
                    FileConfiguration yaml = YamlConfiguration.loadConfiguration(file);
                    String permission = null;
                    if (yaml.get("permission") != null) permission = yaml.getString("permission");
                    int limit = 0;
                    if (yaml.get("limit") != null) limit = yaml.getInt("limit");
                    Kit kit = new Kit(yaml.getString("name").toLowerCase(), permission, yaml.getInt("cooldown"), limit, (List<ItemStack>) yaml.getList("items"));
                    kits.put(yaml.getString("name").toLowerCase(), kit);
                }
            }
        }
        return kits;
    }

    @Override
    public Kit getKit(String name) {
        File file = new File(KitX.getPlugin().getDataFolder() + "/yaml/kit_" + name + ".yml");
        if(file.exists()) {
            FileConfiguration yaml = YamlConfiguration.loadConfiguration(file);
            String permission = null;
            if (yaml.get("permission") != null) permission = yaml.getString("permission");
            int limit = 0;
            if (yaml.get("limit") != null) limit = yaml.getInt("limit");
            return new Kit(yaml.getString("name").toLowerCase(), permission, yaml.getInt("cooldown"), limit, (List<ItemStack>) yaml.getList("items"));
        } else return null;
    }

    @Override
    public void saveKit(String name, Kit kit) {
        File file = new File(KitX.getPlugin().getDataFolder() + "/yaml/kit_" + name + ".yml");
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                Bukkit.getLogger().severe("Could not create the kit file! Details below, please report this to the developer: https://github.com/Prorickey/KitX/issues");
                Bukkit.getLogger().severe(e.getMessage());
            }
        }
        FileConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        yaml.set("name", kit.name());
        yaml.set("permission", kit.permission());
        yaml.set("cooldown", kit.cooldown());
        yaml.set("limit", kit.limit());
        yaml.set("items", kit.items());
        try {
            yaml.save(file);
        } catch (IOException e) {
            Bukkit.getLogger().severe("Could not save config file! Details below, please report this to the developer: https://github.com/Prorickey/KitX/issues");
            Bukkit.getLogger().severe(e.getMessage());
        }
    }

    @Override
    public void updateKit(String name, Kit kit) {
        File file = new File(KitX.getPlugin().getDataFolder() + "/yaml/kit_" + name + ".yml");
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                Bukkit.getLogger().severe("Could not create the kit file! Details below, please report this to the developer: https://github.com/Prorickey/KitX/issues");
                Bukkit.getLogger().severe(e.getMessage());
            }
        }
        FileConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        yaml.set("name", kit.name());
        yaml.set("permission", kit.permission());
        yaml.set("cooldown", kit.cooldown());
        yaml.set("limit", kit.limit());
        yaml.set("items", kit.items());
        try {
            yaml.save(file);
        } catch (IOException e) {
            Bukkit.getLogger().severe("Could not update the kit file! Details below, please report this to the developer: https://github.com/Prorickey/KitX/issues");
            Bukkit.getLogger().severe(e.getMessage());
        }
        if(!Objects.equals(name, kit.name())) deleteKit(name);
    }

    @Override
    public void deleteKit(String name) {
        File file = new File(KitX.getPlugin().getDataFolder() + "/yaml/kit_" + name + ".yml");
        file.delete();
    }

    @Override
    public void putCooldownForKit(String name, UUID uuid, Long nextUseTime) {
        Bukkit.getScheduler().runTaskAsynchronously(KitX.getPlugin(), () -> {
            File file = new File(KitX.getPlugin().getDataFolder() + "/yaml/player_" + uuid.toString() + ".yml");
            if(!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    Bukkit.getLogger().severe("Could not create the player file! Details below, please report this to the developer: https://github.com/Prorickey/KitX/issues");
                    Bukkit.getLogger().severe(e.getMessage());
                }
            }
            YamlConfiguration player = YamlConfiguration.loadConfiguration(file);
            player.set("cooldown." + name, nextUseTime);
            try {
                player.save(file);
            } catch (IOException e) {
                Bukkit.getLogger().severe("Could not update the player file! Details below, please report this to the developer: https://github.com/Prorickey/KitX/issues");
                Bukkit.getLogger().severe(e.getMessage());
            }
        });
    }

    @Override
    public Map<String, Long> getCooldownsForPlayer(UUID uuid) {
        File file = new File(KitX.getPlugin().getDataFolder() + "/yaml/player_" + uuid.toString() + ".yml");
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                Bukkit.getLogger().severe("Could not create the player file! Details below, please report this to the developer: https://github.com/Prorickey/KitX/issues");
                Bukkit.getLogger().severe(e.getMessage());
            }
        }
        YamlConfiguration player = YamlConfiguration.loadConfiguration(file);
        Map<String, Long> cooldowns = new HashMap<>();
        for(String kit : player.getConfigurationSection("cooldown").getKeys(false)) {
            cooldowns.put(kit, player.getLong("cooldown." + kit));
        }
        return cooldowns;
    }

    @Override
    public Map<String, Integer> getLimitsForPlayer(UUID uuid) {
        File file = new File(KitX.getPlugin().getDataFolder() + "/yaml/player_" + uuid.toString() + ".yml");
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                Bukkit.getLogger().severe("Could not create the player file! Details below, please report this to the developer: https://github.com/Prorickey/KitX/issues");
                Bukkit.getLogger().severe(e.getMessage());
            }
        }
        YamlConfiguration player = YamlConfiguration.loadConfiguration(file);
        Map<String, Integer> limits = new HashMap<>();
        for(String kit : player.getConfigurationSection("limit").getKeys(false)) {
            limits.put(kit, player.getInt("limit." + kit));
        }
        return limits;
    }

    @Override
    public void changeLimitForKit(String name, UUID uuid, int change) {
        File file = new File(KitX.getPlugin().getDataFolder() + "/yaml/player_" + uuid.toString() + ".yml");
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                Bukkit.getLogger().severe("Could not create the player file! Details below, please report this to the developer: https://github.com/Prorickey/KitX/issues");
                Bukkit.getLogger().severe(e.getMessage());
            }
        }
        YamlConfiguration player = YamlConfiguration.loadConfiguration(file);
        int current = player.get("limit." + name) != null ? player.getInt("limit." + name) : 0;
        player.set("limit." + name, current + change);
        try {
            player.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
