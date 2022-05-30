package xyz.prorickey.kitx.database;

import org.bukkit.*;
import org.bukkit.configuration.file.*;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;
import xyz.prorickey.kitx.*;
import xyz.prorickey.kitx.builders.*;

import java.io.*;
import java.time.*;
import java.util.*;

public class YAML implements Database {

    private static final Map<String, Kit> kits = new HashMap<>();

    public YAML() {
        File dir = new File(KitX.getPlugin().getDataFolder() + "/yaml");
        dir.mkdir();
        File[] files = dir.listFiles();
        if(files != null) {
            for (File file : files) {
                if(file.getName().startsWith("kit_")) {
                    FileConfiguration yaml = YamlConfiguration.loadConfiguration(file);
                    Inventory inv = Bukkit.createInventory(null, InventoryType.PLAYER, "yamlInventory");
                    yaml.getList("items").forEach(i -> {
                        ItemStack item = (ItemStack) i;
                        inv.addItem(item);
                    });
                    String permission = null;
                    if (yaml.get("permission") != null) permission = yaml.getString("permission");
                    int limit = 0;
                    if (yaml.get("limit") != null) limit = yaml.getInt("limit");
                    Kit kit = new Kit(yaml.getString("name").toLowerCase(), permission, limit, inv, yaml.getInt("cooldown"));
                    kits.put(yaml.getString("name").toLowerCase(), kit);
                }
            }
        }
    }

    @Override
    public Map<String, Kit> getKits() {
        return kits;
    }

    @Override
    public Kit getKit(String name) {
        if(kits.containsKey(name)) return kits.get(name);
        else {
            File file = new File(KitX.getPlugin().getDataFolder() + "/yaml/kit_" + name + ".yml");
            if(file.exists()) {
                FileConfiguration yaml = YamlConfiguration.loadConfiguration(file);
                Inventory inv = Bukkit.createInventory(null, InventoryType.PLAYER, "yamlInventory");
                yaml.getList("items").forEach(i -> {
                    ItemStack item = (ItemStack) i;
                    inv.addItem(item);
                });
                String permission = null;
                if (yaml.get("permission") != null) permission = yaml.getString("permission");
                Integer limit = 0;
                if (yaml.get("limit") != null) limit = yaml.getInt("limit");
                Kit kit = new Kit(yaml.getString("name").toLowerCase(), permission, limit, inv, yaml.getInt("cooldown"));
                kits.put(yaml.getString("name").toLowerCase(), kit);
                return kit;
            } else return null;
        }
    }

    @Override
    public void saveKit(String name, Kit kit) {
        File file = new File(KitX.getPlugin().getDataFolder() + "/yaml/kit_" + name + ".yml");
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        FileConfiguration yaml = YamlConfiguration.loadConfiguration(file);
        yaml.set("name", kit.getName());
        yaml.set("permission", kit.getPermission());
        yaml.set("cooldown", kit.getCooldown());
        yaml.set("limit", kit.getLimit());
        yaml.set("items", kit.getItems());
        try {
            yaml.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        kits.put(name, kit);
    }

    @Override
    public void deleteKit(String name) {
        kits.remove(name);
        File file = new File(KitX.getPlugin().getDataFolder() + "/yaml/kit_" + name + ".yml");
        file.delete();
    }

    @Override
    public Boolean onCooldownForKit(String name, UUID uuid) {
        File file = new File(KitX.getPlugin().getDataFolder() + "/yaml/player_" + uuid.toString() + ".yml");
        if(file.exists()) {
            YamlConfiguration player = YamlConfiguration.loadConfiguration(file);
            long time = player.getLong("cooldown." + name);
            return time > Instant.now().getEpochSecond();
        } else return false;
    }

    @Override
    public void putCooldownForKit(String name, UUID uuid) {
        File file = new File(KitX.getPlugin().getDataFolder() + "/yaml/player_" + uuid.toString() + ".yml");
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        YamlConfiguration player = YamlConfiguration.loadConfiguration(file);
        Kit kit = kits.get(name);
        if(kit != null) player.set("cooldown." + name, kit.getCooldown() + Instant.now().getEpochSecond());
        try {
            player.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Boolean onLimitForKit(String name, UUID uuid) {
        File file = new File(KitX.getPlugin().getDataFolder() + "/yaml/player_" + uuid.toString() + ".yml");
        if(file.exists()) {
            YamlConfiguration player = YamlConfiguration.loadConfiguration(file);
            kits.get(name).getLimit();
            int limit = player.getInt("limit." + name);
            return kits.get(name).getLimit() <= limit;
        } else return false;
    }

    @Override
    public void addLimitForKit(String name, UUID uuid) {
        File file = new File(KitX.getPlugin().getDataFolder() + "/yaml/player_" + uuid.toString() + ".yml");
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        YamlConfiguration player = YamlConfiguration.loadConfiguration(file);
        int current = player.get("limit." + name) != null ? player.getInt("limit." + name) : 0;
        player.set("limit." + name, current + 1);
        try {
            player.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
